package com.encoria.api.repository;

import com.encoria.api.dto.PublicationItemResponse;
import com.encoria.api.model.publications.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query("SELECT p FROM Publication p " +
            "WHERE p.user.id = :currentUserId OR p.user.id IN ((SELECT uf.user.id FROM UserFollower uf WHERE uf.follower.id = :currentUserId AND uf.approved = true))" +
            "ORDER BY p.createdAt DESC")
    List<Publication> findAllByFollowerId(Long currentUserId);

    @Query("SELECT new com.encoria.api.dto.PublicationItemResponse(" +
            "p.uuid, CONCAT('', mm.mediaUrl), CONCAT('', mm.mediaType)) " +
            "FROM Publication p LEFT JOIN MomentMedia mm ON mm.moment.id = p.moment.id AND mm.position = 0 " +
            "WHERE p.user.id = :userId " +
            "ORDER BY p.createdAt DESC")
    List<PublicationItemResponse> findAllByUserIdOrderByCreatedAt(Long userId);

    boolean existsByUuid(UUID uuid);

    boolean existsByUserIdAndUuid(Long userId, UUID uuid);

    void deleteByUuid(UUID uuid);

    @Query("SELECT p.user.id FROM Publication p WHERE p.uuid = :publicationUuid")
    Optional<Long> getPublicationOwnerByUuid(UUID publicationUuid);
}
