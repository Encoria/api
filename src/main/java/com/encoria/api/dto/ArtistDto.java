package com.encoria.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistDto(

        @NotBlank
        @NotNull
        String spotifyId,
        String name,
        String pictureUrl) {
}
