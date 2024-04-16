package com.kelompok1.kloun.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String address;
}
