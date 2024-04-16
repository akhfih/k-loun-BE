package com.kelompok1.kloun.repository;

import com.kelompok1.kloun.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    List<Service> findAllByIsActiveTrue();
}
