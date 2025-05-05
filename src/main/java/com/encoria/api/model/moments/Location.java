package com.encoria.api.model.moments;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Location {

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;
}
