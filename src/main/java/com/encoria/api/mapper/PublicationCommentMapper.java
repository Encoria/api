package com.encoria.api.mapper;

import com.encoria.api.dto.PublicationCommentResponse;
import com.encoria.api.model.publications.PublicationComment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PublicationCommentMapper {
    PublicationCommentResponse toDto(PublicationComment publicationComment);

}
