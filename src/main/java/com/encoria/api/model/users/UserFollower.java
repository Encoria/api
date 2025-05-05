package com.encoria.api.model.users;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_followers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollower {

    @EmbeddedId
    private UserFollowerId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private User follower;

    @Column(nullable = false, updatable = false)
    private Instant followsSince;

    @Column(nullable = false)
    private boolean approved;

    @PrePersist
    public void prePersist() {
        if (followsSince == null) {
            followsSince = Instant.now();
        }
    }
}

