package com.encoria.api.dto;

import java.time.Instant;
import java.util.UUID;

public record UserFollowerDto(
        UUID uuid,
        String username,
        String pictureUrl,
        Instant followsSince,
        Boolean approved) {
}
