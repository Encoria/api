package com.encoria.api.mapper;

import com.encoria.api.dto.UserItemResponse;
import com.encoria.api.dto.UserProfileRequest;
import com.encoria.api.dto.UserProfileResponse;
import com.encoria.api.model.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "countryName", expression = "java(user.getCountry() != null ? user.getCountry().getName() : null)")
    @Mapping(target ="approved", constant = "true" )
    UserProfileResponse toDto(User user);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "firstname", ignore = true)
    @Mapping(target = "lastname", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "countryName", ignore = true)
    @Mapping(target = "followerCount", ignore = true)
    @Mapping(target = "followingCount", ignore = true)
    @Mapping(target = "momentCount", ignore = true)
    @Mapping(target ="approved", constant = "false" )
    UserProfileResponse toReducedDto(User user);

    @Mapping(target = "externalAuthId", expression = "java(jwt.getSubject())")
    @Mapping(target = "email", expression = "java(jwt.getClaimAsString(\"email\"))")
    User toEntity(UserProfileRequest dto, Jwt jwt);

    UserItemResponse toItemDto(User user);
}
