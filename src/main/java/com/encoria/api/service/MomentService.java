package com.encoria.api.service;

import com.encoria.api.dto.MomentDto;
import com.encoria.api.exception.MomentNotFoundException;
import com.encoria.api.exception.UserNotFoundException;
import com.encoria.api.mapper.MomentMapper;
import com.encoria.api.mapper.MomentMediaMapper;
import com.encoria.api.model.users.User;
import com.encoria.api.repository.MomentRepository;
import com.encoria.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.encoria.api.model.moments.Moment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class MomentService {
    private final MomentRepository momentRepository;
    private final UserRepository userRepository;
    private final MomentMapper momentMapper;
    private final MomentMediaMapper momentMediaMapper;

    public List<MomentDto> getUserMoments(Jwt jwt){
        Optional<User> user = userRepository.findByExternalAuthId(jwt.getClaim("sub"));
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        Long userId = user.get().getId();
        return momentRepository.findAllByUserIdOrderByCreatedAt(userId).stream()
                .map(momentMapper::toDto).collect(Collectors.toList());
    }

    public MomentDto createMoment(Jwt jwt, MomentDto momentDto) {
        Optional<User> user = userRepository.findByExternalAuthId(jwt.getClaim("sub"));
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        Moment moment = momentMapper.toEntity(momentDto);
        moment.setUser(user.get());

        return momentMapper.toDto(momentRepository.save(moment));
    }

    public void deleteMoment(UUID uuid) {
        Moment moment = momentRepository.findByUuid(uuid);
        if (moment.getCreatedAt() == null) {
            throw new MomentNotFoundException("Moment not found");
        }
        momentRepository.delete(moment);
    }

    public MomentDto updateMoment(UUID uuid, MomentDto momentDto) {
        Moment updatedMoment = momentMapper.toEntity(momentDto);
        Moment moment = momentRepository.findByUuid(uuid);

        if(moment.getCreatedAt() == null){
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
