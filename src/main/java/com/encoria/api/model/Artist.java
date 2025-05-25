package com.encoria.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "artist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String spotifyId;

    @Column(name = "artist_name", nullable = false)
    private String name;

    private String pictureUrl;
}
