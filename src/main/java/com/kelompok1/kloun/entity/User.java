package com.kelompok1.kloun.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "m_user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    private String address;
    private String phone;
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne
    @JoinColumn(name = "image_profile_id")
    private ImagesProfile imageProfile;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Transaction> transactions;
}