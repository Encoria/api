package com.encoria.api.mapper;

import com.encoria.api.dto.PublicationResponse;
import com.encoria.api.model.publications.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class,MomentMapper.class, PublicationCommentMapper.class})
public interface PublicationMapper {

    @Mapping(target = "commentsCount", expression = "java((long)publication.getComments().size())")
    @Mapping(target = "likesCount", expression = "java((long)publication.getLikes().size())")
    PublicationResponse toDto(Publication publication);
}
