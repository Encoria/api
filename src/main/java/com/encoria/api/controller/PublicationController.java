package com.encoria.api.controller;

import com.encoria.api.dto.PublicationCommentResponse;
import com.encoria.api.dto.PublicationResponse;
import com.encoria.api.dto.UserItemResponse;
import com.encoria.api.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{publicationUuid}/likes")
    public ResponseEntity<List<UserItemResponse>> getPublicationLikes(@PathVariable UUID publicationUuid){
        return new ResponseEntity<>(
                publicationService.getPublicationLikes(publicationUuid),
                HttpStatus.OK);
    }

    @GetMapping("/{publicationUuid}/comments")
    public ResponseEntity<List<PublicationCommentResponse>> getPublicationComments(@PathVariable UUID publicationUuid){
        return new ResponseEntity<>(
                publicationService.getPublicationComments(publicationUuid),
                HttpStatus.OK);
    }

}
