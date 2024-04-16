package com.kelompok1.kloun.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "m_service")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private Integer duration;
    private Long price;
    @Column(name = "is_active")
    private Boolean isActive;

    private String image;

    @OneToMany(mappedBy = "service")
    @JsonBackReference
    private List<Transaction> transactions;
}
