package com.kelompok1.kloun.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResetPasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
