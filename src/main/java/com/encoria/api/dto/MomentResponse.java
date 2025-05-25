package com.encoria.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MomentResponse(
        UUID uuid,
        String title,
        String description,
        Float latitude,
        Float longitude,
        LocalDate date,
        List<MomentMediaDto> media,
        ArtistDto artist
) {

}
