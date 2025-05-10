package com.encoria.api.mapper;

import com.encoria.api.dto.MomentMediaDto;
import com.encoria.api.model.moments.MomentMedia;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MomentMediaMapper {
    MomentMediaDto toDto(MomentMedia momentMedia);
    MomentMedia toEntity(MomentMediaDto momentMediaDto);
    List<MomentMediaDto> toDtoList(List<MomentMedia> momentMediaList);
    List<MomentMedia> toEntityList(List<MomentMediaDto> momentMediaList);
}
