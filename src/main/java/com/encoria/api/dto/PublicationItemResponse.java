package com.encoria.api.dto;

import java.util.UUID;

public record PublicationItemResponse(
        UUID uuid,
        String mediaUrl,
        String mediaType
) {
}
