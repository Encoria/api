package com.encoria.api.mapper;

import com.encoria.api.dto.UserProfileRequest;
import com.encoria.api.dto.UserProfileResponse;
import com.encoria.api.model.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "countryCode", expression = "java(user.getCountry() != null ? user.getCountry().getCode() : null)")
    UserProfileResponse toDto(User user);

    @Mapping(target = "externalAuthId", expression = "java(jwt.getSubject())")
    @Mapping(target = "email", expression = "java(jwt.getClaimAsString(\"email\"))")
    User toEntity(UserProfileRequest dto, Jwt jwt);
}
