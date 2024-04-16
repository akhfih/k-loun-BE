package com.kelompok1.kloun.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ServiceRequest {
    private String id;
    private String name;
    private String description;
    private Integer duration;
    private Long price;
    private Boolean isActive;
    private String image;
}
