package com.encoria.api.service;

import com.encoria.api.dto.*;
import com.encoria.api.exception.*;
import com.encoria.api.mapper.PublicationCommentMapper;
import com.encoria.api.mapper.PublicationMapper;
import com.encoria.api.mapper.UserMapper;
import com.encoria.api.model.publications.Publication;
import com.encoria.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final PublicationCommentRepository publicationCommentRepository;
    private final PublicationLikeRepository publicationLikeRepository;
    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final UserFollowerRepository userFollowerRepository;
    private final MomentRepository momentRepository;
    private final PublicationMapper publicationMapper;
    private final PublicationCommentMapper publicationCommentMapper;
    private final UserMapper userMapper;

    @Transactional
    public List<PublicationResponse> getPublicationsFeed(Jwt jwt) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);


        return publicationRepository.findAllByFollowerId(currentUserId)
                .stream().map(publication -> publicationMapper.toDto(publication).withCounts(
                        publicationCommentRepository.countByPublicationUuid(publication.getUuid()).orElseThrow(PublicationNotFoundException::new),
                        publicationLikeRepository.countByPublicationUuid(publication.getUuid()).orElseThrow(PublicationNotFoundException::new)
                ))
                .toList();
    }

    @Transactional
    public List<PublicationItemResponse> getPublicationsByUser(Jwt jwt, UUID userUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);
        Long targetUserId = userRepository.findIdByUuid(userUuid)
                .orElseThrow(UserNotFoundException::new);

        if (userSettingsRepository.isPrivateProfileByUserId(targetUserId) && !userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetUserId, currentUserId)) {
            throw new PrivateProfileException();
        }

        return publicationRepository.findAllByUserIdOrderByCreatedAt(targetUserId);
    }

    @Transactional
    public PublicationResponse getPublication(Jwt jwt, UUID publicationUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);
        Long targetUserId = publicationRepository.getPublicationOwnerByUuid(publicationUuid)
                .orElseThrow(PublicationNotFoundException::new);

        if (userSettingsRepository.isPrivateProfileByUserId(targetUserId)
                && !userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetUserId, currentUserId)) {
            throw new PrivateProfileException();
        }

        return publicationMapper.toDto(
                publicationRepository.findByUuid(publicationUuid)
                        .orElseThrow(PublicationNotFoundException::new));
    }

    @Transactional
    public List<UserItemResponse> getPublicationLikes(Jwt jwt, UUID publicationUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);
        Long targetUserId = publicationRepository.getPublicationOwnerByUuid(publicationUuid)
                .orElseThrow(PublicationNotFoundException::new);

        if (userSettingsRepository.isPrivateProfileByUserId(targetUserId) && !userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetUserId, currentUserId)) {
            throw new PrivateProfileException();
        }

        return publicationLikeRepository.findAllByPublicationUuid(publicationUuid)
                .stream().map(publicationLike ->
                        userMapper.toItemDto(publicationLike.getUser())
                ).toList();
    }

    @Transactional
    public List<PublicationCommentResponse> getPublicationComments(Jwt jwt, UUID publicationUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);
        Long targetUserId = publicationRepository.getPublicationOwnerByUuid(publicationUuid)
                .orElseThrow(PublicationNotFoundException::new);

        if (userSettingsRepository.isPrivateProfileByUserId(targetUserId) && !userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetUserId, currentUserId)) {
            throw new PrivateProfileException();
        }

        return publicationCommentRepository.findAllByPublicationUuid(publicationUuid)
                .stream().map(publicationCommentMapper::toDto).toList();
    }

    @Transactional
    public PublicationResponse createPublication(Jwt jwt, UUID momentUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        if (!momentRepository.existsByUserIdAndUuid(currentUserId, momentUuid)) {
            throw new ResourceOwnershipException();
        }

        return publicationMapper.toDto(
                publicationRepository.save(Publication.builder()
                        .moment(momentRepository.findByUuid(momentUuid).orElseThrow(MomentNotFoundException::new))
                        .user(userRepository.findByExternalAuthId(jwt.getSubject()).orElseThrow(UserNotFoundException::new))
                        .build()));
    }

    @Transactional
    public void deletePublication(Jwt jwt, UUID publicationUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        if (!publicationRepository.existsByUserIdAndUuid(currentUserId, publicationUuid)) {
            throw new ResourceOwnershipException();
        }

        publicationRepository.deleteByUuid(publicationUuid);
    }

    @Transactional
    public List<MapMarkerResponse> getPublicationsWithinBounds(Jwt jwt,
                                                               Float latNE, Float lonNE,
                                                               Float latSW, Float lonSW) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);
        return publicationRepository.findAllByUserIdWithinBounds(currentUserId, latNE, lonNE, latSW, lonSW);
    }

}
