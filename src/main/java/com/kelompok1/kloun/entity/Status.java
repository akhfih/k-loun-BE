package com.kelompok1.kloun.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "m_status")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String indicator;
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "status")
    @JsonBackReference
    private List<Transaction> transactions;
}
