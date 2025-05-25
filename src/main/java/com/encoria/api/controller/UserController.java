package com.encoria.api.controller;

import com.encoria.api.dto.*;
import com.encoria.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new ResponseEntity<>(
                userService.getOwnProfile(jwt),
                HttpStatus.OK);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return new ResponseEntity<>(
                userService.userExistsByUsername(username),
                HttpStatus.OK);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserFollowerResponse>> getFollowers(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                userService.getFollowers(jwt, null),
                HttpStatus.OK);
    }

    @GetMapping("/following")
    public ResponseEntity<List<UserFollowerResponse>> getFollowing(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                userService.getFollowing(jwt, null),
                HttpStatus.OK);
    }

    @PostMapping("/following")
    public ResponseEntity<UserFollowerResponse> followUser(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestBody UUID targetUuid) {
        return new ResponseEntity<>(
                userService.followUser(jwt, targetUuid),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/following")
    public ResponseEntity<Void> unfollowUser(@AuthenticationPrincipal Jwt jwt,
                                             @RequestBody UUID targetUuid) {
        userService.unfollowUser(jwt, targetUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/following/approve")
    public ResponseEntity<UserFollowerResponse> approveFollow(@AuthenticationPrincipal Jwt jwt,
                                                              @RequestBody UUID followerUuid) {
        return new ResponseEntity<>(
                userService.approveFollow(jwt, followerUuid),
                HttpStatus.OK);
    }

    @GetMapping("/{targetUuid}")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable UUID targetUuid) {
        return new ResponseEntity<>(
                userService.getUserProfile(jwt, targetUuid),
                HttpStatus.OK);
    }

    @GetMapping("/followers/{targetUuid}")
    public ResponseEntity<List<UserFollowerResponse>> getFollowers(@AuthenticationPrincipal Jwt jwt,
                                                                   @PathVariable UUID targetUuid) {
        return new ResponseEntity<>(
                userService.getFollowers(jwt, targetUuid),
                HttpStatus.OK);
    }

    @GetMapping("/following/{targetUuid}")
    public ResponseEntity<List<UserFollowerResponse>> getFollowing(@AuthenticationPrincipal Jwt jwt,
                                                                   @PathVariable UUID targetUuid) {
        return new ResponseEntity<>(
                userService.getFollowing(jwt, targetUuid),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserItemResponse>> searchUsersByUsername(@RequestParam String username) {
        return new ResponseEntity<>(
                userService.searchUsers(username),
                HttpStatus.OK);
    }

    @GetMapping("/settings")
    public ResponseEntity<UserSettingsResponse> getUserSettings(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                userService.getUserSettings(jwt),
                HttpStatus.OK);
    }

    @PutMapping("/settings")
    public ResponseEntity<UserSettingsResponse> updateUserSettings(@AuthenticationPrincipal Jwt jwt,
                                                                   @RequestBody UserSettingsResponse userSettingsResponse) {
        return new ResponseEntity<>(
                userService.updateUserSettings(jwt, userSettingsResponse),
                HttpStatus.OK);
    }
}
