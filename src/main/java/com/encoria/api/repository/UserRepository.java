package com.encoria.api.repository;

import com.encoria.api.dto.UserItemResponse;
import com.encoria.api.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByExternalAuthId(String externalAuthId);

    @Query("SELECT u.id FROM User u WHERE u.externalAuthId = :externalAuthId")
    Optional<Long> findIdByExternalAuthId(@Param("externalAuthId") String externalAuthId);

    @Query("SELECT u.id FROM User u WHERE u.uuid = :uuid")
    Optional<Long> findIdByUuid(@Param("uuid") UUID uuid);

    boolean existsByUsername(String username);

    boolean existsByExternalAuthId(String subject);

    @Query("SELECT new com.encoria.api.dto.UserItemResponse(u.uuid, u.username,u.pictureUrl) " +
            "FROM User u " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<UserItemResponse> searchByUsername(@Param("username") String username);
}
