package com.encoria.api.mapper;

import com.encoria.api.dto.CreateUserProfileDto;
import com.encoria.api.dto.UserProfileDto;
import com.encoria.api.model.users.User;
import org.mapstruct.Mapper;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileDto toDto(User user);
    User toEntity(UserProfileDto userProfileDto);
    User toEntity(CreateUserProfileDto createUserProfileDto);
    default User toEntity(CreateUserProfileDto createUserProfileDto,Jwt jwt) {
        User user = toEntity(createUserProfileDto);
        user.setExternalAuthId(jwt.getSubject());
        return user;
    }
}
