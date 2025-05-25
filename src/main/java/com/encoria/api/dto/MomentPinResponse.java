package com.encoria.api.dto;

import java.util.UUID;

public record MomentPinResponse(
        UUID uuid,
        Float latitude,
        Float longitude) {
}
