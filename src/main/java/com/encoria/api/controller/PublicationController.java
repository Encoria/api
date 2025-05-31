package com.encoria.api.controller;

import com.encoria.api.dto.PublicationCommentResponse;
import com.encoria.api.dto.PublicationItemResponse;
import com.encoria.api.dto.PublicationResponse;
import com.encoria.api.dto.UserItemResponse;
import com.encoria.api.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publications")
public class PublicationController {
    private final PublicationService publicationService;

    @GetMapping
    public ResponseEntity<List<PublicationResponse>> getPublicationsFeed(@AuthenticationPrincipal Jwt jwt){
        return new ResponseEntity<>(
                publicationService.getPublicationsFeed(jwt),
                HttpStatus.OK);
    }

    @GetMapping("/by/{userUuid}")
    public ResponseEntity<List<PublicationItemResponse>> getPublicationsByUser(@AuthenticationPrincipal Jwt jwt,
                                                                               @PathVariable UUID userUuid){
        return new ResponseEntity<>(
                publicationService.getPublicationsByUser(jwt, userUuid),
                HttpStatus.OK);
    }

    @GetMapping("/{publicationUuid}/likes")
    public ResponseEntity<List<UserItemResponse>> getPublicationLikes(@AuthenticationPrincipal Jwt jwt,
                                                                      @PathVariable UUID publicationUuid){
        return new ResponseEntity<>(
                publicationService.getPublicationLikes(jwt, publicationUuid),
                HttpStatus.OK);
    }

    @GetMapping("/{publicationUuid}/comments")
    public ResponseEntity<List<PublicationCommentResponse>> getPublicationComments(@AuthenticationPrincipal Jwt jwt,
                                                                                   @PathVariable UUID publicationUuid){
        return new ResponseEntity<>(
                publicationService.getPublicationComments(jwt, publicationUuid),
                HttpStatus.OK);
    }

    @PostMapping("/publish/{momentUuid}")
    public ResponseEntity<PublicationResponse> createPublication(@AuthenticationPrincipal Jwt jwt,
                                                                 @PathVariable UUID momentUuid) {
        return new ResponseEntity<>(
                publicationService.createPublication(jwt, momentUuid),
                HttpStatus.OK);
    }

    @DeleteMapping("/{publicationUuid}")
    public ResponseEntity<Void> deletePublication(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable UUID publicationUuid) {
        publicationService.deletePublication(jwt, publicationUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
