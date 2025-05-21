package com.encoria.api.service;

import com.encoria.api.dto.MomentRequest;
import com.encoria.api.dto.MomentResponse;
import com.encoria.api.exception.MomentNotFoundException;
import com.encoria.api.exception.ResourceOwnershipException;
import com.encoria.api.exception.UserNotFoundException;
import com.encoria.api.mapper.MomentMapper;
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

    public List<MomentResponse> getUserMoments(Jwt jwt) {
        Long userId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        return momentRepository.findAllByUserIdOrderByCreatedAt(userId).stream()
                .map(momentMapper::toDto).toList();
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
        Long userId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        Boolean momentExists = momentRepository.existsByUuid(uuid);

        if (Boolean.FALSE.equals(momentExists)) {
            throw new MomentNotFoundException("Moment not found with uuid: " + uuid);
        }

        Boolean userIsOwner = momentRepository.existsByUuidAndUserId(uuid, userId);

        if (Boolean.FALSE.equals(userIsOwner)) {
            throw new ResourceOwnershipException();
        }
        momentRepository.deleteByUuid(uuid);
    }

    public MomentResponse updateMoment(UUID uuid, MomentRequest momentRequest) {
        Moment updatedMoment = momentMapper.toEntity(momentRequest);
        Moment moment = momentRepository.findByUuid(uuid);

        if (moment.getCreatedAt() == null) {
            throw new MomentNotFoundException("Moment not found");
        }

        moment.setTitle(updatedMoment.getTitle());
        moment.setDescription(updatedMoment.getDescription());
        moment.setLocation(updatedMoment.getLocation());
        moment.setDate(updatedMoment.getDate());
        moment.setMedia(updatedMoment.getMedia());
        moment.setArtists(updatedMoment.getArtists());

        return momentMapper.toDto(moment);


    }


}
