package com.encoria.api.service;

import com.encoria.api.dto.CreateUserProfileDto;
import com.encoria.api.dto.UserProfileDto;
import com.encoria.api.exception.UserNotFoundException;
import com.encoria.api.exception.UsernameAlreadyExistsException;
import com.encoria.api.model.users.User;
import com.encoria.api.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserProfileDto getUserProfile(Jwt jwt) {
        return userRepository.findByExternalAuthId(jwt.getSubject())
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public UserProfileDto createUser(Jwt jwt, @Valid CreateUserProfileDto dto) {
        if (userExistsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        User saved = userRepository.save(toEntity(jwt, dto));
        return toDto(saved);
    }

    private User toEntity(Jwt jwt, CreateUserProfileDto dto) {
        return User.builder()
                .externalAuthId(jwt.getSubject())
                .email(jwt.getClaimAsString("email"))
                .username(dto.username())
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .birthdate(dto.birthdate())
                .pictureUrl(dto.pictureUrl())
                .build();
    }

    private UserProfileDto toDto(User user) {
        return new UserProfileDto(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getBirthdate(),
                user.getPictureUrl()
        );
    }
}