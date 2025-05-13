package com.encoria.api.mapper;

import com.encoria.api.dto.MomentDto;
import com.encoria.api.model.moments.Moment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MomentMediaMapper.class, ArtistMapper.class})
public interface MomentMapper {

    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.longitude", target = "longitude")
    MomentDto toDto(Moment moment);

    @Mapping(target = "location", expression = "java(toLocation(momentDto.latitude(), momentDto.longitude()))")
    Moment toEntity(MomentDto momentDto);

    default com.encoria.api.model.moments.Location toLocation(Float latitude, Float longitude) {
        if (latitude == null || longitude == null) return null;
        return new com.encoria.api.model.moments.Location(latitude, longitude);
    }
}
