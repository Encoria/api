package com.encoria.api.dto;


import com.encoria.api.model.moments.Location;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public record MomentDto(
        UUID uuid,
        String title,
        String description,
        Location location,
        LocalDate date,
        List<MomentMediaDto> media,
        Set<ArtistDto> artists
) {

}
