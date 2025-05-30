package com.encoria.api.dto;

import java.util.UUID;

public record PublicationResponse(
        UUID uuid,
        MomentResponse moment,
        UserItemResponse user,
        Long commentsCount,
        Long likesCount
) {
    public PublicationResponse withCounts(Long commentsCount, Long likesCount) {
        return new PublicationResponse(
                this.uuid,
                this.moment,
                this.user,
                commentsCount,
                likesCount);
    }
}
