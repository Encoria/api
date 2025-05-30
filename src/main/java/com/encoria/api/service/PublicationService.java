package com.encoria.api.service;

import com.encoria.api.dto.PublicationCommentResponse;
import com.encoria.api.dto.PublicationResponse;
import com.encoria.api.dto.UserItemResponse;
import com.encoria.api.exception.PublicationNotFoundException;
import com.encoria.api.exception.UserNotFoundException;
import com.encoria.api.mapper.PublicationCommentMapper;
import com.encoria.api.mapper.PublicationMapper;
import com.encoria.api.mapper.UserMapper;
import com.encoria.api.repository.PublicationCommentRepository;
import com.encoria.api.repository.PublicationLikeRepository;
import com.encoria.api.repository.PublicationRepository;
import com.encoria.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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

    public List<UserItemResponse> getPublicationLikes(UUID publicationUuid){

        if(!publicationRepository.existsByUuid(publicationUuid)){
            throw new PublicationNotFoundException();
        }

        return  publicationLikeRepository.findAllByPublicationUuid(publicationUuid)
                .stream().map(publicationLike ->
                        userMapper.toItemDto(publicationLike.getUser())
                ).toList();
    }

    public List<PublicationCommentResponse> getPublicationComments(UUID publicationUuid){

        if(!publicationRepository.existsByUuid(publicationUuid)){
            throw new PublicationNotFoundException();
        }

        return  publicationCommentRepository.findAllByPublicationUuid(publicationUuid)
                .stream().map(publicationCommentMapper::toDto).toList();
    }
}
