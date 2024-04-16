package com.kelompok1.kloun.service;

import com.kelompok1.kloun.dto.request.ResetPasswordRequest;
import com.kelompok1.kloun.dto.response.ResetPasswordResponse;

public interface ResetPasswordService {
    ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
}
