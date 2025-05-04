package com.encoria.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserProfileDto(
        UUID uuid,
        String username,
        String email,
        String firstname,
        String lastname,
        LocalDate birthdate,
        String pictureUrl) {
}
