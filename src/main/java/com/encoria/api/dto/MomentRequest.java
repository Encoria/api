package com.encoria.api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record MomentRequest(
        @NotBlank
        @Size(min = 3, max = 25)
        @Pattern(regexp = "^\\P{C}+$", message = "title cannot contain strange characters")
        String title,

        @Pattern(regexp = "^\\P{C}+$", message = "description cannot contain strange characters")
        String description,

        @NotNull
        @DecimalMin(value = "-90.0", message = "latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "latitude must be between -90 and 90")
        Float latitude,

        @NotNull
        @DecimalMin(value = "-180.0", message = "longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "longitude must be between -180 and 180")
        Float longitude,

        @NotNull
        @Past(message = "moment date must be in the past")
        LocalDate date,

        @Size(max = 10)// STC: provisional limit for demo
        List<MomentMediaDto> media,

        ArtistDto artist
) {
}
