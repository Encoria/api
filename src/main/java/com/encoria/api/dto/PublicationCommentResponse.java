package com.encoria.api.dto;

import java.util.UUID;

public record PublicationCommentResponse(
        UUID  uuid,
        String content,
        UserItemResponse user
) {
}
