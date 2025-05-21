package com.encoria.api.controller;

import com.encoria.api.dto.UserProfileRequest;
import com.encoria.api.dto.UserFollowerResponse;
import com.encoria.api.dto.UserProfileResponse;
import com.encoria.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/onboard")
    public ResponseEntity<UserProfileResponse> createProfile(@AuthenticationPrincipal Jwt jwt,
                                                             @Valid @RequestBody UserProfileRequest userProfileDto) {
        return new ResponseEntity<>(
                userService.createUser(jwt, userProfileDto),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getUserProfile(jwt), HttpStatus.OK);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return new ResponseEntity<>(
                userService.userExistsByUsername(username),
                HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> followUser(@AuthenticationPrincipal Jwt jwt,
                                           @RequestParam UUID uuid) {
        userService.followUser(jwt, uuid);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<Void> unfollowUser(@AuthenticationPrincipal Jwt jwt,
                                             @RequestParam UUID uuid) {
        userService.unfollowUser(jwt, uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserFollowerResponse>> getFollowers(@AuthenticationPrincipal Jwt jwt,
                                                                   @Nullable @RequestParam UUID uuid) {
        return new ResponseEntity<>(userService.getFollowers(jwt, uuid), HttpStatus.OK);
    }

    @GetMapping("/following")
    public ResponseEntity<List<UserFollowerResponse>> getFollowing(@AuthenticationPrincipal Jwt jwt,
                                                                   @Nullable @RequestParam UUID uuid) {
        return new ResponseEntity<>(userService.getFollowing(jwt, uuid), HttpStatus.OK);
    }

}
