package com.encoria.api.dto;

import java.util.UUID;

public record MapMarkerResponse(
        UUID uuid,
        Float latitude,
        Float longitude) {
}
