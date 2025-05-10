package com.encoria.api.repository;

import com.encoria.api.model.moments.Moment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {
    List<Moment> findAllByUserIdOrderByCreatedAt(Long userId);
    Moment findByUuid(UUID uuid);
    long countByUserId(Long userId);

}
