package com.kelompok1.kloun.repository;

import com.kelompok1.kloun.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, String> {
    Optional<Status> findByName(String name);
    List<Status> findAllByIsActiveTrue();
}
