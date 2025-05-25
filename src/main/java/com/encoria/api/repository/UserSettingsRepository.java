package com.encoria.api.repository;

import com.encoria.api.model.users.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    Optional<UserSettings> findByUserId(Long userId);

    @Query("SELECT u.isPrivateProfile FROM UserSettings u WHERE u.user.id = :userId")
    Boolean isPrivateProfileByUserId(Long userId);
}
