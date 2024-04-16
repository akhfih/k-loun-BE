package com.kelompok1.kloun.repository;

import com.kelompok1.kloun.constant.ERole;
import com.kelompok1.kloun.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserCredentialId(String userCredentialId);
    Page<User> findByIsActiveTrueAndUserCredential_Role_Name(ERole role, Pageable pageable);
    Optional<User> findByEmail(String email);
    List<User> findAllByIsActiveTrueAndUserCredential_Role_Name(ERole role);
}
