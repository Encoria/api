package com.encoria.api.repository;

import com.encoria.api.dto.UserFollowerResponse;
import com.encoria.api.model.users.UserFollower;
import com.encoria.api.model.users.UserFollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, UserFollowerId> {

    @Query("SELECT COUNT(uf.id) FROM UserFollower uf WHERE uf.user.id = :userId AND uf.approved = true")
    Optional<Long> countByUserId(Long userId);

    Optional<Long> countByFollowerId(Long followerId);

    @Query("SELECT uf FROM UserFollower uf WHERE uf.user.id = :userId AND uf.follower.id = :followerId")
    Optional<UserFollower> findByUserIdAndFollowerId(@Param("userId") Long userId, @Param("followerId") Long followerId);

    @Query("SELECT new com.encoria.api.dto.UserFollowerResponse(" +
            "uf.follower.uuid, uf.follower.username, uf.follower.pictureUrl, uf.followsSince, " +
            // approved - indicates if there's an approved relationship between the current user and this follower
            "CASE WHEN EXISTS (SELECT 1 FROM UserFollower uf4 WHERE uf4.follower.id = uf.follower.id AND uf4.user.id = :currentUserId) THEN " +
            "   (SELECT (uf5.approved) FROM UserFollower uf5 WHERE uf5.follower.id = uf.follower.id AND uf5.user.id = :currentUserId) " +
            "ELSE false END, " +
            // isFollowed - whether the current user follows this person
            "CASE WHEN (SELECT COUNT(uf2) FROM UserFollower uf2 WHERE uf2.follower.id = :currentUserId AND uf2.user.id = uf.follower.id) > 0 THEN true ELSE false END, " +
            // isFollower - whether each person in the list follows the current user
            "CASE WHEN (SELECT COUNT(uf3) FROM UserFollower uf3 WHERE uf3.follower.id = uf.follower.id AND uf3.user.id = :currentUserId) > 0 THEN true ELSE false END) " +
            "FROM UserFollower uf " +
            "WHERE uf.user.id = :userId AND uf.approved = true")
    List<UserFollowerResponse> findFollowersWithRelationStatus(@Param("userId") Long userId,
                                                               @Param("currentUserId") Long currentUserId);

    @Query("SELECT new com.encoria.api.dto.UserFollowerResponse(" +
            "uf.user.uuid, uf.user.username, uf.user.pictureUrl, uf.followsSince, " +
            // approved - indicates if there's an approved relationship between the current user and this follower'
            "CASE WHEN EXISTS (SELECT 1 FROM UserFollower uf4 WHERE uf4.follower.id = :currentUserId AND uf4.user.id = uf.user.id) THEN " +
            "   (SELECT (uf5.approved) FROM UserFollower uf5 WHERE uf5.follower.id = :currentUserId AND uf5.user.id = uf.user.id) " +
            "ELSE false END, " +
            // isFollowed - whether the current user follows each person in the list
            "CASE WHEN (SELECT COUNT(uf2) FROM UserFollower uf2 WHERE uf2.follower.id = :currentUserId AND uf2.user.id = uf.user.id) > 0 THEN true ELSE false END, " +
            // isFollower - whether each person in the list follows the current user
            "CASE WHEN (SELECT COUNT(uf3) FROM UserFollower uf3 WHERE uf3.follower.id = uf.user.id AND uf3.user.id = :currentUserId) > 0 THEN true ELSE false END) " +
            "FROM UserFollower uf " +
            "WHERE uf.follower.id = :followerId AND uf.approved = true")
    List<UserFollowerResponse> findFollowingWithRelationStatus(@Param("followerId") Long followerId,
                                                               @Param("currentUserId") Long currentUserId);

    @Query("SELECT new com.encoria.api.dto.UserFollowerResponse(" +
            "u.uuid, " +
            "u.username, " +
            "u.pictureUrl, " +
            "(SELECT rel.followsSince FROM UserFollower rel WHERE rel.user.id = :targetUserId AND rel.follower.id = :requestingUserId), " +
            "COALESCE((SELECT uf.approved FROM UserFollower uf WHERE uf.user.id = :targetUserId AND uf.follower.id = :requestingUserId), false), " +
            "CASE WHEN EXISTS (" +
            "   SELECT 1 FROM UserFollower is_fwd WHERE is_fwd.user.id = :targetUserId AND is_fwd.follower.id = :requestingUserId) " +
            "THEN true ELSE false END, " +
            "CASE WHEN EXISTS (" +
            "   SELECT 1 FROM UserFollower is_fer WHERE is_fer.user.id = :requestingUserId AND is_fer.follower.id = :targetUserId) " +
            "THEN true ELSE false END) " +
            "FROM User u " +
            "WHERE u.id = :targetUserId")
    UserFollowerResponse findUserFollowerRelation(@Param("requestingUserId") Long requestingUserId,
                                                  @Param("targetUserId") Long targetUserId);

    @Query("SELECT new com.encoria.api.dto.UserFollowerResponse(" +
            "uf.follower.uuid, uf.follower.username, uf.follower.pictureUrl, uf.followsSince, uf.approved," +
            "CASE WHEN EXISTS (" +
            "   SELECT 1 FROM UserFollower is_fwd WHERE is_fwd.user.id = uf.follower.id AND is_fwd.follower.id = :currentUserId) " +
            "THEN true ELSE false END, " +
            "CASE WHEN EXISTS (" +
            "   SELECT 1 FROM UserFollower is_fer WHERE is_fer.user.id = :currentUserId AND is_fer.follower.id = uf.follower.id) " +
            "THEN true ELSE false END) " +
            "FROM UserFollower uf " +
            "WHERE uf.user.id = :currentUserId AND uf.approved = false")
    List<UserFollowerResponse> findPendingFollowers(@Param("currentUserId") Long currentUserId);

    boolean existsByUserIdAndFollowerIdAndApprovedIsTrue(Long userId, Long followerId);

    boolean existsByUserIdAndFollowerId(Long userId, Long followerId);
}
