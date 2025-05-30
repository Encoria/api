package com.encoria.api.repository;

import com.encoria.api.model.publications.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicationRepository extends JpaRepository<Publication,Long> {

    @Query("SELECT p FROM Publication p " +
            "WHERE p.user.id IN (" +
            "SELECT uf.user.id FROM UserFollower uf WHERE uf.follower.id = :currentUserId) " +
            "ORDER BY p.createdAt")
    List<Publication> findAllByFollowerId(Long currentUserId);

    boolean existsByUuid(UUID uuid);
}
