package com.encoria.api.service;

import com.encoria.api.dto.UserItemResponse;
import com.encoria.api.dto.UserProfileRequest;
import com.encoria.api.dto.UserProfileResponse;
import com.encoria.api.dto.UserSettingsResponse;
import com.encoria.api.exception.CountryNotFoundException;
import com.encoria.api.exception.UserNotFoundException;
import com.encoria.api.exception.UserProfileAlreadyExistsException;
import com.encoria.api.exception.UsernameAlreadyExistsException;
import com.encoria.api.mapper.UserMapper;
import com.encoria.api.mapper.UserSettingsMapper;
import com.encoria.api.model.users.Country;
import com.encoria.api.model.users.User;
import com.encoria.api.model.users.UserSettings;
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

    @Transactional
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

    public List<UserItemResponse> searchUsers(String query) {
        return userRepository.searchByUsername(query);
    }

    @Transactional
    public UserSettingsResponse getUserSettings(Jwt jwt) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        return userSettingsMapper.toDto(userSettingsRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new));
    }

    @Transactional
    public UserSettingsResponse updateUserSettings(Jwt jwt, UserSettingsResponse userSettingsResponse) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        UserSettings updatedSettings = userSettingsRepository.save(userSettingsMapper.toEntity(userId, userSettingsResponse));

        return userSettingsMapper.toDto(updatedSettings);
    }
}
