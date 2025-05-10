package com.encoria.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserProfileDto(
        UUID uuid,
        String username,
        String email,
        String firstname,
        String lastname,
        LocalDate birthdate,
        String pictureUrl,
        String countryCode,
        Long followerCount,
        Long followingCount,
        Long momentCount) {

    public UserProfileDto withCounts(Long followerCount, Long followingCount, Long momentCount) {
        return new UserProfileDto(
                this.uuid,
                this.username,
                this.email,
                this.firstname,
                this.lastname,
                this.birthdate,
                this.pictureUrl,
                this.countryCode,
                followerCount,
                followingCount,
                momentCount);
    }
}
