package com.encoria.api.mapper;

import com.encoria.api.dto.ArtistDto;
import com.encoria.api.model.Artist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    ArtistDto toDto(Artist artist);
    Artist toEntity(ArtistDto artistDto);
}
