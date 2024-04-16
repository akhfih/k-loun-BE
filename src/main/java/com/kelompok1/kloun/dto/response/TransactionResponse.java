package com.kelompok1.kloun.dto.response;

import com.kelompok1.kloun.entity.Service;
import com.kelompok1.kloun.entity.Status;
import com.kelompok1.kloun.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionResponse {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Double weight;
    private Long totalPrice;
    private boolean paid;
    private User user;
    private Service service;
    private Status status;
}
