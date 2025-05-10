package com.encoria.api.repository;

import com.encoria.api.model.users.UserFollower;
import com.encoria.api.model.users.UserFollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, UserFollowerId> {
    Optional<Long> countByUserId(Long userId);

    Optional<Long> countByFollowerId(Long followerId);
}
