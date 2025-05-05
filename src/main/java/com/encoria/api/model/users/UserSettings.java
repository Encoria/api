package com.encoria.api.model.users;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettings {

    @Id
    private Long userId;

    private boolean isPrivateProfile;
    private boolean notifyComments;
    private boolean notifyLikes;
    private boolean notifyFollow;
    private Instant updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
