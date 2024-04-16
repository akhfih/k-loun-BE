package com.kelompok1.kloun.service;

import com.kelompok1.kloun.constant.ERole;
import com.kelompok1.kloun.dto.request.LoginRequest;
import com.kelompok1.kloun.dto.request.RegisterRequest;
import com.kelompok1.kloun.dto.response.LoginResponse;
import com.kelompok1.kloun.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest, ERole userRole);
    LoginResponse login(LoginRequest loginRequest);
}