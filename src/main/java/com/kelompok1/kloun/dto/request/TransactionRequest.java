package com.kelompok1.kloun.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionRequest {
    private String id;
    private String userId;
    private String serviceId;
    private String statusId;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Double weight;
    private Long totalPrice;
    private boolean paid;
}
