package com.encoria.api.repository;

import com.encoria.api.model.users.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    Boolean isPrivateProfileByUserId(Long userId);
}
