package com.kelompok1.kloun.repository;

import com.kelompok1.kloun.constant.ERole;
import com.kelompok1.kloun.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
