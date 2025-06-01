package com.encoria.api.controller;

import com.encoria.api.dto.UserFollowerResponse;
import com.encoria.api.service.UserFollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile/social")
@RequiredArgsConstructor
public class UserFollowerController {

    private final UserFollowerService userFollowerService;

    @GetMapping("/followers")
    public ResponseEntity<List<UserFollowerResponse>> getFollowers(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                userFollowerService.getFollowers(jwt, null),
                HttpStatus.OK);
    }

    @GetMapping("/following")
    public ResponseEntity<List<UserFollowerResponse>> getFollowing(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                userFollowerService.getFollowing(jwt, null),
                HttpStatus.OK);
    }

    @GetMapping("/follow-status/{targetUuid}")
    public ResponseEntity<UserFollowerResponse> getFollowStatus(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID targetUuid) {
        return new ResponseEntity<>(
                userFollowerService.getFollowStatus(jwt, targetUuid),
                HttpStatus.OK);
    }

    @PostMapping("/following/{targetUuid}")
    public ResponseEntity<UserFollowerResponse> followUser(@AuthenticationPrincipal Jwt jwt,
                                                           @PathVariable UUID targetUuid) {
        return new ResponseEntity<>(
                userFollowerService.followUser(jwt, targetUuid),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/following/{targetUuid}")
    public ResponseEntity<Void> unfollowUser(@AuthenticationPrincipal Jwt jwt,
                                             @PathVariable UUID targetUuid) {
        userFollowerService.unfollowUser(jwt, targetUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/followers/pending/approve/{followerUuid}")
    public ResponseEntity<UserFollowerResponse> approveFollow(@AuthenticationPrincipal Jwt jwt,
                                                              @PathVariable UUID followerUuid) {
        return new ResponseEntity<>(
                userFollowerService.approveFollow(jwt, followerUuid),
                HttpStatus.OK);
    }

    @GetMapping("/followers/pending")
    public ResponseEntity<List<UserFollowerResponse>> getPendingFollowers(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                userFollowerService.getPendingFollowers(jwt),
                HttpStatus.OK);
    }

    @GetMapping("/followers/{targetUuid}")
    public ResponseEntity<List<UserFollowerResponse>> getFollowers(@AuthenticationPrincipal Jwt jwt,
                                                                   @PathVariable UUID targetUuid) {
        return new ResponseEntity<>(
                userFollowerService.getFollowers(jwt, targetUuid),
                HttpStatus.OK);
    }

    @GetMapping("/following/{targetUuid}")
    public ResponseEntity<List<UserFollowerResponse>> getFollowing(@AuthenticationPrincipal Jwt jwt,
                                                                   @PathVariable UUID targetUuid) {
        return new ResponseEntity<>(
                userFollowerService.getFollowing(jwt, targetUuid),
                HttpStatus.OK);
    }
}
