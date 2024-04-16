package com.kelompok1.kloun.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerRequest {
    private String id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String address;
    private MultipartFile profileImage;
}
