package com.encoria.api.repository;

import com.encoria.api.model.users.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    Optional<UserSettings> findByUserId(Long userId);

    Boolean isPrivateProfileByUserId(Long userId);
}
