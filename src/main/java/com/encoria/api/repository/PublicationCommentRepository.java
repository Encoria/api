package com.encoria.api.repository;

import com.encoria.api.model.publications.PublicationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PublicationCommentRepository extends JpaRepository<PublicationComment, Long> {

    @Query ("SELECT pc FROM PublicationComment pc " +
            "WHERE pc.publication.uuid = :publicationUuid")
    List<PublicationComment> findAllByPublicationUuid(@Param("publicationUuid") UUID publicationUuid);

}
