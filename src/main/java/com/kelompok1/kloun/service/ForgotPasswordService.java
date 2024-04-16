package com.kelompok1.kloun.service;

import com.kelompok1.kloun.dto.request.ForgotPasswordRequest;
import com.kelompok1.kloun.dto.response.ForgotPasswordResponse;

public interface ForgotPasswordService {
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}
