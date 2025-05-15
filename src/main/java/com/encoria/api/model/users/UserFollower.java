package com.encoria.api.model.users;

import com.encoria.api.exception.FollowSelfException;
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

    @Column(nullable = false)
    private Instant followsSince;

    @Column(nullable = false)
    private Boolean approved;

    @PrePersist
    public void prePersist() {
        if (this.id.getUserId().equals(this.id.getFollowerId())) {
            throw new FollowSelfException();
        }
        if (followsSince == null) {
            followsSince = Instant.now();
        }
        approved = approved != null && approved;
    }

    @PreUpdate
    public void preUpdate() {
        if (approved) {
            followsSince = Instant.now();
        }
    }
}

