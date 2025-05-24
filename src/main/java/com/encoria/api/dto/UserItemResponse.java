package com.encoria.api.dto;

import java.util.UUID;

public record UserItemResponse(
        UUID uuid,
        String username,
        String pictureUrl) {
}
