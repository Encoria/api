package com.encoria.api.mapper;

import com.encoria.api.dto.PublicationResponse;
import com.encoria.api.model.publications.Publication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class,MomentMapper.class, PublicationCommentMapper.class})
public interface PublicationMapper {

    PublicationResponse toDto(Publication publication);
}
