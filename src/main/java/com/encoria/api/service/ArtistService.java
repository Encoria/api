package com.encoria.api.service;

import com.encoria.api.dto.ArtistDto;
import com.encoria.api.model.Artist;
import com.encoria.api.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Transactional
    public Artist getOrCreateArtist(ArtistDto artistDto) {
        if (artistDto == null) {
            return null;
        }

        return artistRepository.findBySpotifyId(artistDto.spotifyId())
                .map(existingArtist -> {
                    existingArtist.setName(artistDto.name());
                    existingArtist.setPictureUrl(artistDto.pictureUrl());
                    return artistRepository.save(existingArtist);
                })
                .orElseGet(() -> artistRepository.save(Artist.builder()
                        .spotifyId(artistDto.spotifyId())
                        .name(artistDto.name())
                        .pictureUrl(artistDto.pictureUrl())
                        .build()));
    }
}
