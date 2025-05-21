package com.encoria.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record MomentRequest(
        String title,
        String description,
        Float latitude,
        Float longitude,
        LocalDate date,
        List<MomentMediaDto> media,
        Set<ArtistDto> artists
) {
}
