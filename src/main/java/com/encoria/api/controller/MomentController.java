package com.encoria.api.controller;


import com.encoria.api.dto.MomentRequest;
import com.encoria.api.dto.MomentResponse;
import com.encoria.api.service.MomentService;
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
@RequestMapping("/api/moments")
@RequiredArgsConstructor
public class MomentController {

    private final MomentService momentService;

    @GetMapping
    public ResponseEntity<List<MomentResponse>> getMoments(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(momentService.getUserMoments(jwt), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MomentResponse> createMoment(@AuthenticationPrincipal Jwt jwt,
                                                       @Valid @RequestBody MomentRequest momentRequest) {
        return new ResponseEntity<>(momentService.createMoment(jwt, momentRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{momentUuid}")
    public ResponseEntity<Void> deleteMoment(@AuthenticationPrincipal Jwt jwt,
                                             @PathVariable UUID momentUuid) {
        momentService.deleteMoment(jwt, momentUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{momentUuid}")
    public ResponseEntity<MomentResponse> updateMoment(@AuthenticationPrincipal Jwt jwt,
                                                       @PathVariable UUID momentUuid,
                                                       @Valid @RequestBody MomentRequest momentRequest) {
        return new ResponseEntity<>(momentService.updateMoment(jwt, momentUuid, momentRequest), HttpStatus.OK);
    }

}
