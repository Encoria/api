package com.encoria.api.dto;

public record UserSettingsResponse(
        Boolean isPrivateProfile,
        Boolean notifyComments,
        Boolean notifyLikes,
        Boolean notifyFollow) {
}
