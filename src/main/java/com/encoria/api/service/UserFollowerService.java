package com.encoria.api.service;

import com.encoria.api.dto.UserFollowerResponse;
import com.encoria.api.exception.FollowSelfException;
import com.encoria.api.exception.PrivateProfileException;
import com.encoria.api.exception.UserAlreadyFollowedException;
import com.encoria.api.exception.UserNotFoundException;
import com.encoria.api.model.users.User;
import com.encoria.api.model.users.UserFollower;
import com.encoria.api.model.users.UserFollowerId;
import com.encoria.api.repository.UserFollowerRepository;
import com.encoria.api.repository.UserRepository;
import com.encoria.api.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFollowerService {

    private final UserRepository userRepository;
    private final UserFollowerRepository userFollowerRepository;
    private final UserSettingsRepository userSettingsRepository;

    @Transactional
    public List<UserFollowerResponse> getFollowers(Jwt jwt, UUID targetUuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long targetId = (targetUuid == null) ?
                userId :
                checkTargetUser(targetUuid, userId);
        return userFollowerRepository.findFollowersWithRelationStatus(targetId, userId);
    }

    @Transactional
    public List<UserFollowerResponse> getFollowing(Jwt jwt, UUID targetUuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long targetId = (targetUuid == null) ?
                userId :
                checkTargetUser(targetUuid, userId);
        return userFollowerRepository.findFollowingWithRelationStatus(targetId, userId);
    }

    private Long checkTargetUser(UUID targetUuid, Long userId) {
        Long targetId = userRepository.findIdByUuid(targetUuid)
                .orElseThrow(UserNotFoundException::new);

        if (targetId.equals(userId)) {
            return targetId;
        }
        if (Boolean.FALSE.equals(userSettingsRepository.isPrivateProfileByUserId(targetId))) {
            return targetId;
        }

        if (userFollowerRepository.existsByUserIdAndFollowerIdAndApprovedIsTrue(targetId, userId)) {
            return targetId;
        } else {
            throw new PrivateProfileException();
        }
    }

    @Transactional
    public UserFollowerResponse getFollowStatus(Jwt jwt, UUID targetUserUuid) {
        Long requestingUserId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);
        Long targetUserId = userRepository.findIdByUuid(targetUserUuid)
                .orElseThrow(UserNotFoundException::new);

        return userFollowerRepository.findUserFollowerRelation(requestingUserId, targetUserId);
    }

    @Transactional
    public UserFollowerResponse followUser(Jwt jwt, UUID targetUuid) {
        Long followerId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long followedId = userRepository.findIdByUuid(targetUuid)
                .orElseThrow(UserNotFoundException::new);

        if (followerId.equals(followedId)) {
            throw new FollowSelfException();
        }

        if (userFollowerRepository.existsByUserIdAndFollowerId(followedId, followerId)) {
            throw new UserAlreadyFollowedException();
        }

        UserFollower userFollower = userFollowerRepository.save(UserFollower.builder()
                .id(new UserFollowerId(followedId, followerId))
                .user(User.builder().id(followedId).build())
                .follower(User.builder().id(followerId).build())
                .approved(Boolean.FALSE.equals(userSettingsRepository.isPrivateProfileByUserId(followedId)))
                .build());

        User targetUser = userRepository.findById(followedId)
                .orElseThrow(UserNotFoundException::new);

        return new UserFollowerResponse(
                targetUser.getUuid(),
                targetUser.getUsername(),
                targetUser.getPictureUrl(),
                userFollower.getFollowsSince(),
                userFollower.getApproved(),
                true,  // isFollowed - we're following this user
                userFollowerRepository.existsByUserIdAndFollowerId(followerId, followedId)  // check if they follow us
        );
    }

    @Transactional
    public void unfollowUser(Jwt jwt, UUID targetUuid) {
        Long followerId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long userId = userRepository.findIdByUuid(targetUuid)
                .orElseThrow(UserNotFoundException::new);

        userFollowerRepository.deleteById(new UserFollowerId(userId, followerId));
    }

    @Transactional
    public UserFollowerResponse approveFollow(Jwt jwt, UUID followerUuid) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);

        Long followerId = userRepository.findIdByUuid(followerUuid)
                .orElseThrow(UserNotFoundException::new);

        UserFollower followRequest = userFollowerRepository.findByUserIdAndFollowerId(userId, followerId)
                .orElseThrow(() -> new UserNotFoundException("No request to approve."));

        if (Boolean.TRUE.equals(followRequest.getApproved())) {
            throw new UserAlreadyFollowedException("This follow request has already been approved");
        }

        followRequest.setApproved(true);
        userFollowerRepository.save(followRequest);

        User follower = userRepository.findById(followerId)
                .orElseThrow(UserNotFoundException::new);

        return new UserFollowerResponse(
                follower.getUuid(),
                follower.getUsername(),
                follower.getPictureUrl(),
                followRequest.getFollowsSince(),
                true,
                userFollowerRepository.existsByUserIdAndFollowerId(userId, followerId),
                true
        );
    }

    @Transactional
    public List<UserFollowerResponse> getPendingFollowers(Jwt jwt) {
        Long userId = userRepository.findIdByExternalAuthId(jwt.getSubject())
                .orElseThrow(UserNotFoundException::new);
        return userFollowerRepository.findPendingFollowers(userId);
    }
}
