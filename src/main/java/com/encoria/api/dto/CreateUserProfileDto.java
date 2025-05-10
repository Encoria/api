package com.encoria.api.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateUserProfileDto(
        @NotBlank
        @Size(min = 3, max = 25)
        @Pattern(regexp = "^\\w+$", message = "username can only contain letters, numbers, or underscores")
        String username,

        @Size(max = 50)
        @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "firstname can only contain letters, spaces, apostrophes or hyphens")
        String firstname,

        @Size(max = 50)
        @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "lastname can only contain letters, spaces, apostrophes or hyphens")
        String lastname,

        @Past(message = "birthdate must be in the past")
        LocalDate birthdate,

        @Pattern(regexp = "^(https?://).+$", message = "pictureUrl must be a valid URL")
        String pictureUrl,

        String countryCode) {
}
