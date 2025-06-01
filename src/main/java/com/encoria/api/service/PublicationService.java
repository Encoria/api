package com.encoria.api.service;

import com.encoria.api.dto.*;
import com.encoria.api.exception.*;
import com.encoria.api.mapper.PublicationCommentMapper;
import com.encoria.api.mapper.PublicationMapper;
import com.encoria.api.mapper.UserMapper;
import com.encoria.api.model.publications.Publication;
import com.encoria.api.model.publications.PublicationComment;
import com.encoria.api.model.publications.PublicationLike;
import com.encoria.api.model.publications.PublicationLikeId;
import com.encoria.api.model.users.User;
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
                .stream().map(publication ->
                        publicationMapper.toDto(publication)
                                .withIsLiked(publicationLikeRepository.existsByUserIdAndPublicationId(
                                        currentUserId, publication.getId()))).toList();
    }

    @Transactional
    public List<PublicationItemResponse> getPublicationsByUser(Jwt jwt, UUID targetUserUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);
        Long targetUserId = userRepository.findIdByUuid(targetUserUuid)
                .orElseThrow(UserNotFoundException::new);
        if (userSettingsRepository.isPrivateProfileByUserId(targetUserId) &&
                !userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetUserId, currentUserId)) {
            throw new PrivateProfileException();
        }
        return publicationRepository.findAllByUserUuidOrderByCreatedAt(targetUserUuid);
    }

    @Transactional
    public PublicationResponse getPublication(Jwt jwt, UUID publicationUuid) {
        checkPublicationAccess(jwt, publicationUuid);
        return publicationMapper.toDto(
                publicationRepository.findByUuid(publicationUuid)
                        .orElseThrow(PublicationNotFoundException::new));
    }

    @Transactional
    public List<UserItemResponse> getPublicationLikes(Jwt jwt, UUID publicationUuid) {
        checkPublicationAccess(jwt, publicationUuid);
        return publicationLikeRepository.findAllByPublicationUuid(publicationUuid)
                .stream().map(publicationLike ->
                        userMapper.toItemDto(publicationLike.getUser())).toList();
    }

    @Transactional
    public UserItemResponse likePublication(Jwt jwt, UUID publicationUuid) {
        checkPublicationAccess(jwt, publicationUuid);
        User currentUser = userRepository.findByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        Publication targetPublication = publicationRepository.findByUuid(publicationUuid)
                .orElseThrow(PublicationNotFoundException::new);

        PublicationLikeId likeId = new PublicationLikeId(currentUser.getId(), targetPublication.getId());
        if (publicationLikeRepository.existsById(likeId)) {
            throw new PublicationAlreadyLikedException();
        }

        PublicationLike newLike = PublicationLike.builder()
                .user(currentUser)
                .publication(targetPublication)
                .build();

        publicationLikeRepository.save(newLike);
        return userMapper.toItemDto(currentUser);
    }

    @Transactional
    public void unlikePublication(Jwt jwt, UUID publicationUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        Long publicationId = publicationRepository.findIdByPublicationUuid(publicationUuid)
                .orElseThrow(PublicationNotFoundException::new);

        publicationLikeRepository.deleteById(new PublicationLikeId(currentUserId, publicationId));
    }

    @Transactional
    public List<PublicationCommentResponse> getPublicationComments(Jwt jwt, UUID publicationUuid) {
        checkPublicationAccess(jwt, publicationUuid);
        return publicationCommentRepository.findAllByPublicationUuid(publicationUuid)
                .stream().map(publicationCommentMapper::toDto).toList();
    }

    @Transactional
    public PublicationCommentResponse commentOnPublication(Jwt jwt, UUID publicationUuid, String content) {
        checkPublicationAccess(jwt, publicationUuid);
        User currentUser = userRepository.findByExternalAuthId(
                jwt.getSubject()).orElseThrow(UserNotFoundException::new);

        Publication targetPublication = publicationRepository.findByUuid(publicationUuid)
                .orElseThrow(PublicationNotFoundException::new);

        PublicationComment comment = PublicationComment.builder()
                .user(currentUser)
                .publication(targetPublication)
                .content(content)
                .build();
        return publicationCommentMapper.toDto(publicationCommentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Jwt jwt, UUID publicationUuid, UUID commentUuid) {
        PublicationComment comment = publicationCommentRepository.findByUuid(commentUuid)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getPublication().getUuid().equals(publicationUuid) ||
                !comment.getUser().getExternalAuthId().equals(jwt.getSubject())) {
            throw new ResourceOwnershipException();
        }
        publicationCommentRepository.delete(comment);
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

    private void checkPublicationAccess(Jwt jwt, UUID publicationUuid) {
        Long currentUserId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);
        Long targetUserId = publicationRepository.getPublicationOwnerByUuid(publicationUuid)
                .orElseThrow(UserNotFoundException::new);
        if (userSettingsRepository.isPrivateProfileByUserId(targetUserId) &&
                !userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetUserId, currentUserId)) {
            throw new PrivateProfileException();
        }
    }
}
