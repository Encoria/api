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

    private Boolean isPrivateProfile;
    private Boolean notifyComments;
    private Boolean notifyLikes;
    private Boolean notifyFollow;
    private Instant updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if (isPrivateProfile == null) {
            isPrivateProfile = false;
        }
        if (notifyComments == null) {
            notifyComments = true;
        }
        if (notifyLikes == null) {
            notifyLikes = true;
        }
        if (notifyFollow == null) {
            notifyFollow = true;
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
