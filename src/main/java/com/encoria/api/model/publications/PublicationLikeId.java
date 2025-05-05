package com.encoria.api.model.publications;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PublicationLikeId {
    private Long userId;
    private Long publicationId;
}
