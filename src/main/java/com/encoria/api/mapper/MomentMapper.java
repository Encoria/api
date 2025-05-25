package com.encoria.api.mapper;

import com.encoria.api.dto.MomentRequest;
import com.encoria.api.dto.MomentResponse;
import com.encoria.api.model.moments.Moment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MomentMediaMapper.class, ArtistMapper.class})
public interface MomentMapper {

    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.longitude", target = "longitude")
    MomentResponse toDto(Moment moment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "location", expression = "java(toLocation(momentRequest.latitude(), momentRequest.longitude()))")
    Moment toEntity(MomentRequest momentRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "media", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "location", expression = "java(toLocation(momentRequest.latitude(), momentRequest.longitude()))")
    void updateMomentFromDto(MomentRequest momentRequest, @MappingTarget Moment moment);

    default com.encoria.api.model.moments.Location toLocation(Float latitude, Float longitude) {
        if (latitude == null || longitude == null) return null;
        return new com.encoria.api.model.moments.Location(latitude, longitude);
    }
}
