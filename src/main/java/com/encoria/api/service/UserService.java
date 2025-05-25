package com.encoria.api.service;

import com.encoria.api.dto.*;
import com.encoria.api.exception.*;
import com.encoria.api.mapper.UserMapper;
import com.encoria.api.mapper.UserSettingsMapper;
import com.encoria.api.model.users.*;
import com.encoria.api.repository.*;
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
    private final UserSettingsRepository userSettingsRepository;
    private final MomentRepository momentRepository;
    private final CountryRepository countryRepository;
    private final UserMapper userMapper;
    private final UserSettingsMapper userSettingsMapper;

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public UserProfileResponse createUser(Jwt jwt, UserProfileRequest dto) {
        if (userRepository.existsByExternalAuthId(jwt.getSubject())) {
            throw new UserProfileAlreadyExistsException("Profile already exists for this user");
        }

        if (userRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User userToSave = userMapper.toEntity(dto, jwt);

        if (dto.countryCode() != null && !dto.countryCode().isBlank()) {
            Country country = countryRepository.findByCode(dto.countryCode()).orElseThrow(() ->
                    new CountryNotFoundException("Country not found with code: " + dto.countryCode()));
            userToSave.setCountry(country);
        } else {
            userToSave.setCountry(null);
        }

        User savedUser = userRepository.save(userToSave);
        return userMapper.toDto(savedUser).withCounts(0L, 0L, 0L);
    }

    @Transactional
    public UserProfileResponse getOwnProfile(Jwt jwt) {
        User user = userRepository.findByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toDto(user).withCounts(
                userFollowerRepository.countByUserId(user.getId()).orElse(0L),
                userFollowerRepository.countByFollowerId(user.getId()).orElse(0L),
                momentRepository.countByUserId(user.getId()).orElse(0L));
    }

    public UserProfileResponse getUserProfile(Jwt jwt, UUID targetUuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long targetId = userRepository.findIdByUuid(targetUuid)
                .orElseThrow(UserNotFoundException::new);

        User user = userRepository.findById(targetId)
                .orElseThrow(UserNotFoundException::new);

        Boolean isPrivate = userSettingsRepository.isPrivateProfileByUserId(targetId);
        if (Boolean.FALSE.equals(isPrivate) || userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetId, userId)) {
            return userMapper.toDto(user).withCounts(
                    userFollowerRepository.countByUserId(targetId).orElse(0L),
                    userFollowerRepository.countByFollowerId(targetId).orElse(0L),
                    momentRepository.countByUserId(targetId).orElse(0L));
        } else {
            return userMapper.toReducedDto(user);
        }
    }

    @Transactional
    public List<UserFollowerResponse> getFollowers(Jwt jwt, UUID targetUuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        if (targetUuid == null) return userFollowerRepository.findByUserId(userId);

        Long targetId = checkTargetUser(targetUuid, userId);
        return userFollowerRepository.findByUserId(targetId);
    }

    @Transactional
    public List<UserFollowerResponse> getFollowing(Jwt jwt, UUID targetUuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        if (targetUuid == null) return userFollowerRepository.findByFollowerId(userId);

        Long targetId = checkTargetUser(targetUuid, userId);
        return userFollowerRepository.findByFollowerId(targetId);
    }

    private Long checkTargetUser(UUID targetUuid, Long userId) {
        Long targetId = userRepository.findIdByUuid(targetUuid)
                .orElseThrow(UserNotFoundException::new);

        if (Boolean.FALSE.equals(userSettingsRepository.isPrivateProfileByUserId(targetId))) {
            return targetId;
        }

        if (userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetId, userId)) {
            return targetId;
        } else {
            throw new PrivateProfileException();
        }
    }

    @Transactional
    public UserFollowerResponse followUser(Jwt jwt, UUID targetUuid) {
        Long followerId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long followedId = userRepository.findIdByUuid(targetUuid)
                .orElseThrow(UserNotFoundException::new);

        if (followerId.equals(followedId)) {
            throw new FollowSelfException();
        }

        if (userFollowerRepository.existsByUserIdAndFollowerId(followedId, followerId)) {
            throw new UserAlreadyFollowedException();
        }

        UserFollower userFollower = userFollowerRepository.save(UserFollower.builder()
                .id(new UserFollowerId(followedId, followerId))
                .user(User.builder().id(followedId).build())
                .follower(User.builder().id(followerId).build())
                .approved(Boolean.FALSE.equals(userSettingsRepository.isPrivateProfileByUserId(followedId)))
                .build());

        User targetUser = userRepository.findById(followedId)
                .orElseThrow(UserNotFoundException::new);

        return new UserFollowerResponse(
                targetUser.getUuid(),
                targetUser.getUsername(),
                targetUser.getPictureUrl(),
                userFollower.getFollowsSince(),
                userFollower.getApproved()
        );
    }

    @Transactional
    public void unfollowUser(Jwt jwt, UUID targetUuid) {
        Long followerId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long userId = userRepository.findIdByUuid(targetUuid)
                .orElseThrow(UserNotFoundException::new);

        userFollowerRepository.deleteById(new UserFollowerId(userId, followerId));
    }

    @Transactional
    public UserFollowerResponse approveFollow(Jwt jwt, UUID followerUuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long followerId = userRepository.findIdByUuid(followerUuid)
                .orElseThrow(UserNotFoundException::new);

        UserFollower followRequest = userFollowerRepository.findById(new UserFollowerId(userId, followerId))
                .orElseThrow(() -> new UserNotFoundException("No request to approve."));

        if (Boolean.TRUE.equals(followRequest.getApproved())) {
            throw new UserAlreadyFollowedException("This follow request has already been approved");
        }

        followRequest.setApproved(true);
        userFollowerRepository.save(followRequest);

        User follower = userRepository.findById(followerId)
                .orElseThrow(UserNotFoundException::new);

        return new UserFollowerResponse(
                follower.getUuid(),
                follower.getUsername(),
                follower.getPictureUrl(),
                followRequest.getFollowsSince(),
                true
        );
    }

    public List<UserItemResponse> searchUsers(String query) {
        return userRepository.searchByUsername(query);
    }

    public UserSettingsResponse getUserSettings(Jwt jwt) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        return userSettingsMapper.toDto(userSettingsRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new));
    }

    public UserSettingsResponse updateUserSettings(Jwt jwt, UserSettingsResponse userSettingsResponse) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        UserSettings updatedSettings = userSettingsRepository.save(userSettingsMapper.toEntity(userId, userSettingsResponse));

        return userSettingsMapper.toDto(updatedSettings);
    }
}
