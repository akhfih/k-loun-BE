package com.kelompok1.kloun.repository;

import com.kelompok1.kloun.entity.ImagesProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesProfileRepository extends JpaRepository<ImagesProfile, String> {
}
