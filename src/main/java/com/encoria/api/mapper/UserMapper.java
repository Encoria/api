package com.encoria.api.mapper;

import com.encoria.api.dto.CreateUserProfileDto;
import com.encoria.api.dto.UserProfileDto;
import com.encoria.api.model.users.Country;
import com.encoria.api.model.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "countryCode", expression = "java(user.getCountry() != null ? user.getCountry().getCode() : null)")
    UserProfileDto toDto(User user);

    default User toEntity(CreateUserProfileDto dto, Jwt jwt) {
        User user = new User();
        user.setExternalAuthId(jwt.getSubject());
        user.setEmail(jwt.getClaimAsString("email"));
        user.setUsername(dto.username());
        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setBirthdate(dto.birthdate());
        user.setPictureUrl(dto.pictureUrl());
        return user;
    }
}
