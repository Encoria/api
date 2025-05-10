package com.encoria.api.repository;

import com.encoria.api.model.users.UserFollower;
import com.encoria.api.model.users.UserFollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, UserFollowerId> {
    long countByUserId(Long userId);

    long countByFollowerId(Long followerId);
}
