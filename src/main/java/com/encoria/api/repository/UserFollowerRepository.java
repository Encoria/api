package com.encoria.api.repository;

import com.encoria.api.dto.UserFollowerDto;
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
    Optional<Long> countByUserId(Long userId);

    Optional<Long> countByFollowerId(Long followerId);

    @Query("SELECT new com.encoria.api.dto.UserFollowerDto(" +
            "uf.follower.uuid, uf.follower.username, uf.follower.pictureUrl, uf.followsSince, uf.approved) " +
            "FROM UserFollower uf " +
            "WHERE uf.user.id = :userId")
    List<UserFollowerDto> findByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.encoria.api.dto.UserFollowerDto(" +
            "uf.user.uuid, uf.user.username, uf.user.pictureUrl, uf.followsSince, uf.approved) " +
            "FROM UserFollower uf " +
            "WHERE uf.follower.id = :followerId")
    List<UserFollowerDto> findByFollowerId(@Param("followerId") Long followerId);

    boolean existsByUserIdAndFollowerId(Long userId, Long followerId);
}
