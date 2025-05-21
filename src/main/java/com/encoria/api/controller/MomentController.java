package com.encoria.api.controller;


import com.encoria.api.dto.MomentDto;
import com.encoria.api.service.MomentService;
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
    public ResponseEntity<List<MomentDto>> getMoments(@AuthenticationPrincipal Jwt jwt){
        return new ResponseEntity<>(momentService.getUserMoments(jwt), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<MomentDto> createMoment(@AuthenticationPrincipal Jwt jwt,
                                               @RequestBody MomentDto momentDto) {
        return new ResponseEntity<>(
                momentService.createMoment(jwt, momentDto)
                ,HttpStatus.CREATED);
    }

    @DeleteMapping("/{momentUuid}")
    public ResponseEntity<Void> deleteMoment(@AuthenticationPrincipal Jwt jwt,
                                             @PathVariable UUID momentUuid) {
        momentService.deleteMoment(jwt, momentUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{momentUuid}")
    public ResponseEntity<MomentDto> updateMoment(@PathVariable UUID momentUuid, @RequestBody MomentDto momentDto) {
        return new ResponseEntity<>(
                momentService.updateMoment(momentUuid,momentDto),
                HttpStatus.OK
        );
    }

}
