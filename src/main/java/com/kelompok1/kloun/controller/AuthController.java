package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.constant.ERole;
import com.kelompok1.kloun.dto.request.LoginRequest;
import com.kelompok1.kloun.dto.request.RegisterRequest;
import com.kelompok1.kloun.dto.response.LoginResponse;
import com.kelompok1.kloun.dto.response.RegisterResponse;
import com.kelompok1.kloun.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public RegisterResponse registerCustomer(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest, ERole.ROLE_CUSTOMER);
    }
    @PostMapping("/register-admin")
    public RegisterResponse registerAdmin(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest, ERole.ROLE_ADMIN);
    }

    @PostMapping("/register-super-admin")
    public RegisterResponse registerSuperAdmin(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest, ERole.ROLE_SUPER_ADMIN);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

}
