package com.encoria.api.dto;

import java.util.UUID;

public record PublicationResponse(
        UUID uuid,
        MomentResponse moment,
        UserItemResponse user,
        Boolean isLiked,
        Long commentsCount,
        Long likesCount
) {
    public PublicationResponse withIsLiked(Boolean isLiked) {
        return new PublicationResponse(
                this.uuid,
                this.moment,
                this.user,
                isLiked,
                this.commentsCount,
                this.likesCount);
    }
}
