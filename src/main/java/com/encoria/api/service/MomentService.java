package com.encoria.api.service;

import com.encoria.api.dto.MomentPinResponse;
import com.encoria.api.dto.MomentRequest;
import com.encoria.api.dto.MomentResponse;
import com.encoria.api.exception.MomentNotFoundException;
import com.encoria.api.exception.ResourceOwnershipException;
import com.encoria.api.exception.UserNotFoundException;
import com.encoria.api.mapper.MomentMapper;
import com.encoria.api.mapper.MomentMediaMapper;
import com.encoria.api.model.moments.Moment;
import com.encoria.api.model.moments.MomentMedia;
import com.encoria.api.model.users.User;
import com.encoria.api.repository.MomentRepository;
import com.encoria.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class MomentService {
    private final MomentRepository momentRepository;
    private final UserRepository userRepository;
    private final MomentMapper momentMapper;
    private final MomentMediaMapper momentMediaMapper;

    public List<MomentResponse> getUserMoments(Jwt jwt) {
        Long userId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        return momentRepository.findAllByUserIdOrderByCreatedAt(userId).stream()
                .map(momentMapper::toDto).toList();
    }

    @Transactional
    public MomentResponse getMoment(Jwt jwt, UUID uuid) {
        if (!checkMomentOwnership(jwt, uuid)) {
            throw new ResourceOwnershipException();
        }
        return momentMapper.toDto(momentRepository.findByUuid(uuid)
                .orElseThrow(() -> new MomentNotFoundException("Moment not found with uuid: " + uuid)));
    }

    @Transactional
    public MomentResponse createMoment(Jwt jwt, MomentRequest momentRequest) {
        User user = userRepository.findByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        Moment moment = momentMapper.toEntity(momentRequest);
        moment.setUser(user);

        if (moment.getMedia() != null) {
            for (MomentMedia media : moment.getMedia()) {
                media.setMoment(moment);
            }
        }

        Moment saved = momentRepository.save(moment);
        return momentMapper.toDto(saved);
    }

    @Transactional
    public void deleteMoment(Jwt jwt, UUID uuid) {
        if (checkMomentOwnership(jwt, uuid)) {
            momentRepository.deleteByUuid(uuid);
        }
    }

    @Transactional
    public MomentResponse updateMoment(Jwt jwt, UUID uuid, MomentRequest momentRequest) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Moment existingMoment = momentRepository.findByUuid(uuid)
                .orElseThrow(() -> new MomentNotFoundException("Moment not found with UUID: " + uuid));

        if (!existingMoment.getUser().getId().equals(userId)) {
            throw new ResourceOwnershipException();
        }

        momentMapper.updateMomentFromDto(momentRequest, existingMoment);

        existingMoment.getMedia().clear();
        if (momentRequest.media() != null) {
            List<MomentMedia> newMediaEntities = momentRequest.media().stream()
                    .map(mediaDto -> {
                        MomentMedia media = momentMediaMapper.toEntity(mediaDto);
                        media.setMoment(existingMoment);
                        return media;
                    })
                    .toList();
            existingMoment.getMedia().addAll(newMediaEntities);
        }

        Moment updatedMoment = momentRepository.save(existingMoment);
        return momentMapper.toDto(updatedMoment);
    }

    @Transactional
    public List<MomentPinResponse> getMomentsWithinBounds(Jwt jwt,
                                                          Float latNE, Float lonNE,
                                                          Float latSW, Float lonSW) {
        Long userId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);
        return momentRepository.findAllByUserIdWithinBounds(userId, latNE, lonNE, latSW, lonSW);
    }

    private boolean checkMomentOwnership(Jwt jwt, UUID uuid) {
        Long userId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        Boolean momentExists = momentRepository.existsByUuid(uuid);

        if (Boolean.FALSE.equals(momentExists)) {
            throw new MomentNotFoundException("Moment not found with uuid: " + uuid);
        }

        return Boolean.TRUE.equals(momentRepository.existsByUuidAndUserId(uuid, userId));
    }

}
