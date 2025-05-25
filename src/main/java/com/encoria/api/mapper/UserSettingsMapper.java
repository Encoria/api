package com.encoria.api.mapper;

import com.encoria.api.dto.UserSettingsResponse;
import com.encoria.api.model.users.UserSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSettingsMapper {
    UserSettingsResponse toDto(UserSettings userSettings);

    @Mapping(target = "userId", expression = "java(userId)")
    UserSettings toEntity(Long userId, UserSettingsResponse userSettingsResponse);
}
