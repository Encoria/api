package com.encoria.api.service;

import com.encoria.api.dto.CreateUserProfileDto;
import com.encoria.api.dto.UserFollowerDto;
import com.encoria.api.dto.UserProfileDto;
import com.encoria.api.exception.*;
import com.encoria.api.mapper.UserMapper;
import com.encoria.api.model.users.Country;
import com.encoria.api.model.users.User;
import com.encoria.api.model.users.UserFollower;
import com.encoria.api.model.users.UserFollowerId;
import com.encoria.api.repository.CountryRepository;
import com.encoria.api.repository.MomentRepository;
import com.encoria.api.repository.UserFollowerRepository;
import com.encoria.api.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFollowerRepository userFollowerRepository;
    private final MomentRepository momentRepository;
    private final CountryRepository countryRepository;
    private final UserMapper userMapper;

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public UserProfileDto getUserProfile(Jwt jwt) {
        User user = userRepository.findByExternalAuthId(jwt.getSubject())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        long followerCount = userFollowerRepository.countByUserId(user.getId()).orElse(0L);
        long followingCount = userFollowerRepository.countByFollowerId(user.getId()).orElse(0L);
        long momentCount = momentRepository.countByUserId(user.getId()).orElse(0L);

        return userMapper.toDto(user).withCounts(followerCount, followingCount, momentCount);
    }

    @Transactional
    public UserProfileDto createUser(Jwt jwt, @Valid CreateUserProfileDto dto) {

        if (userRepository.existsByExternalAuthId(jwt.getSubject())) {
            throw new UserProfileAlreadyExistsException("Profile already exists for this user");
        }

        if (userRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User userToSave = userMapper.toEntity(dto, jwt);

        if (dto.countryCode() != null && !dto.countryCode().isBlank()) {
            Country country = countryRepository.findByCode(dto.countryCode())
                    .orElseThrow(() -> new CountryNotFoundException("Country not found with code: " + dto.countryCode()));

            userToSave.setCountry(country);
        } else {
            userToSave.setCountry(null);
        }

        User savedUser = userRepository.save(userToSave);

        return userMapper.toDto(savedUser).withCounts(0L, 0L, 0L);
    }

    @Transactional
    public void followUser(Jwt jwt, UUID uuid) {
        Long followerId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long followedId = userRepository.findIdByUuid(uuid)
                .orElseThrow(UserNotFoundException::new);

        if(followerId.equals(followedId)) {
            throw new FollowSelfException();
        }

        if (userFollowerRepository.existsByUserIdAndFollowerId(followedId, followerId)) {
            throw new UserAlreadyFollowedException();
        }

        userFollowerRepository.save(UserFollower.builder()
                .id(new UserFollowerId(followedId, followerId))
                .user(User.builder().id(followedId).build())
                .follower(User.builder().id(followerId).build())
                .approved(Boolean.FALSE.equals(userRepository.isPrivate(followedId)))
                .build());
    }

    @Transactional
    public void unfollowUser(Jwt jwt, UUID uuid) {
        Long followerId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long userId = userRepository.findIdByUuid(uuid)
                .orElseThrow(UserNotFoundException::new);

        userFollowerRepository.deleteById(new UserFollowerId(userId, followerId));
    }

    @Transactional
    public List<UserFollowerDto> getFollowers(Jwt jwt, UUID uuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        if (uuid == null) return userFollowerRepository.findByUserId(userId);

        Long targetId = checkTargetUser(uuid, userId);
        return userFollowerRepository.findByUserId(targetId);
    }

    @Transactional
    public List<UserFollowerDto> getFollowing(Jwt jwt, UUID uuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        if (uuid == null) return userFollowerRepository.findByFollowerId(userId);

        Long targetId = checkTargetUser(uuid, userId);
        return userFollowerRepository.findByFollowerId(targetId);
    }

    private Long checkTargetUser(UUID uuid, Long userId) {
        Long targetId = userRepository.findIdByUuid(uuid)
                .orElseThrow(UserNotFoundException::new);

        Boolean isPrivate = userRepository.isPrivate(targetId);
        if (Boolean.TRUE.equals(isPrivate)) {
            UserFollower existing = userFollowerRepository.findById(
                    new UserFollowerId(targetId, userId)).orElse(null);
            if (existing == null || !(existing.getApproved())) {
                throw new PrivateProfileException();
            }
        }
        return targetId;
    }
}
