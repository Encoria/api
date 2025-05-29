package com.encoria.api.dto;

import java.util.UUID;

public record MomentListResponse(
        UUID uuid,
        String title,
        Float latitude,
        Float longitude,
        String mediaUrl
) {
}
