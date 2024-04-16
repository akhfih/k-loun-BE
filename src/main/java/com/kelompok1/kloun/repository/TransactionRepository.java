package com.kelompok1.kloun.repository;

import com.kelompok1.kloun.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findAllByOrderByStartDateDesc(Pageable pageable);
    Page<Transaction> findByStatusIdOrderByStartDateDesc(String statusId, Pageable pageable);
    Page<Transaction> findByUserIdOrderByStartDateDesc(String userId, Pageable pageable);
    Page<Transaction> findByUserIdAndStatusIdOrderByStartDateDesc(String userId, String statusId, Pageable pageable);
    Page<Transaction> findByUserIdAndStatus_NameOrUserIdAndStatus_NameOrderByStartDateDesc(String userId, String status1, String userId2, String status2, Pageable pageable);
    Page<Transaction> findByUserIdAndIsActiveTrueOrderByStartDateDesc(String userId, Pageable pageable);
}
