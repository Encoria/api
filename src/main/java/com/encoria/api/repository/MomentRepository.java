package com.encoria.api.repository;

import com.encoria.api.model.moments.Moment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {
    List<Moment> findAllByUserIdOrderByCreatedAt(Long userId);

    Optional<Moment> findByUuid(UUID uuid);

    Optional<Long> countByUserId(Long userId);

    Boolean existsByUuid(UUID uuid);

    Boolean existsByUuidAndUserId(UUID uuid, Long userId);

    void deleteByUuid(UUID uuid);

}
