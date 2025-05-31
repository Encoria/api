package com.encoria.api.service;

import com.encoria.api.dto.PublicationCommentResponse;
import com.encoria.api.dto.PublicationResponse;
import com.encoria.api.dto.UserItemResponse;
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

    private final PublicationMapper publicationMapper;
    private final UserMapper userMapper;
    private final PublicationCommentMapper publicationCommentMapper;
    private final PublicationRepository publicationRepository;
    private final PublicationCommentRepository publicationCommentRepository;
    private final PublicationLikeRepository publicationLikeRepository;
    private final UserRepository userRepository;
    private final MomentRepository momentRepository;

    @Transactional
    public List<PublicationResponse> getPublicationsFeed(Jwt jwt){
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

}
