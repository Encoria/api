package com.encoria.api.repository;

import com.encoria.api.dto.MomentListResponse;
import com.encoria.api.dto.MomentPinResponse;
import com.encoria.api.model.moments.Moment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {

    @Query("SELECT new com.encoria.api.dto.MomentListResponse(" +
            "m.uuid, m.title, m.location.latitude, m.location.longitude, CONCAT('', mm.mediaUrl)) " +
            "FROM Moment m LEFT JOIN MomentMedia mm ON mm.moment.id = m.id AND mm.position = 0 " +
            "WHERE m.user.id = :userId " +
            "ORDER BY m.createdAt")
    List<MomentListResponse> findAllByUserIdOrderByCreatedAt(Long userId);

    Optional<Moment> findByUuid(UUID uuid);

    Optional<Long> countByUserId(Long userId);

    Boolean existsByUuid(UUID uuid);

    Boolean existsByUuidAndUserId(UUID uuid, Long userId);

    void deleteByUuid(UUID uuid);

    @Query("SELECT new com.encoria.api.dto.MomentPinResponse(m.uuid, m.location.latitude, m.location.longitude) " +
            "FROM Moment m " +
            "WHERE m.user.id = :userId " +
            "AND m.location.latitude BETWEEN :minLat AND :maxLat " +
            "AND m.location.longitude BETWEEN :minLon AND :maxLon")
    List<MomentPinResponse> findAllByUserIdWithinBounds(Long userId, Float maxLat, Float maxLon, Float minLat, Float minLon);

    boolean existsByUserIdAndUuid(Long userId, UUID uuid);
}
