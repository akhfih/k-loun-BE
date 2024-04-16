package com.kelompok1.kloun.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_images_profile")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ImagesProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String url;
}
