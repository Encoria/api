package com.encoria.api.repository;
import com.encoria.api.model.publications.PublicationLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicationLikeRepository extends JpaRepository<PublicationLike, Long> {

    @Query("SELECT pl FROM PublicationLike pl " +
            "WHERE pl.publication.uuid = :publicationUuid")
    List<PublicationLike> findAllByPublicationUuid(@Param("publicationUuid") UUID publicationUuid);

    @Query("SELECT COUNT(pl) FROM PublicationLike pl " +
            "WHERE pl.publication.uuid = :publicationUuid")
    Optional<Long> countByPublicationUuid(@Param("publicationUuid") UUID publicationUuid);
    boolean existsByUserIdAndPublicationId(Long userId, Long publicationId);

}
