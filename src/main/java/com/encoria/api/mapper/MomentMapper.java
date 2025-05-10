package com.encoria.api.mapper;

import com.encoria.api.dto.MomentDto;
import com.encoria.api.model.moments.Moment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses={MomentMediaMapper.class, ArtistMapper.class})
public interface MomentMapper {

    @Mapping(source="momentMedia",target="media")
    @Mapping(source="momentMedia",target="media")

    MomentDto toDto(Moment moment);
    @Mapping(source="media",target="momentMedia")
    @Mapping(source="momentMedia",target="media")

    Moment toEntity(MomentDto momentDto);

}
