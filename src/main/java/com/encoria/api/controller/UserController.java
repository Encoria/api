package com.encoria.api.controller;

import com.encoria.api.dto.CreateUserProfileDto;
import com.encoria.api.dto.UserProfileDto;
import com.encoria.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/onboard")
    public ResponseEntity<UserProfileDto> createProfile(@AuthenticationPrincipal Jwt jwt,
                                                        @Valid @RequestBody CreateUserProfileDto userProfileDto) {
        return new ResponseEntity<>(
                userService.createUser(jwt, userProfileDto),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getUserProfile(jwt), HttpStatus.OK);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return new ResponseEntity<>(
                userService.userExistsByUsername(username),
                HttpStatus.OK);
    }

}
