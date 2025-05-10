package com.encoria.api.dto;

import com.encoria.api.model.moments.MediaType;

import java.util.UUID;

public record MomentMediaDto(
        UUID uuid,
        String mediaUrl,
        MediaType mediaType,
        Integer position
) {


}
