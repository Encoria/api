package com.encoria.api.dto;

import com.encoria.api.model.moments.MediaType;

public record MomentMediaDto(
        String mediaUrl,
        MediaType mediaType,
        Integer position
) {
}
