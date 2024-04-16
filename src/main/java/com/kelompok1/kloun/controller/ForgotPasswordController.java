package com.kelompok1.kloun.controller;


import com.kelompok1.kloun.dto.request.ForgotPasswordRequest;
import com.kelompok1.kloun.dto.response.ForgotPasswordResponse;
import com.kelompok1.kloun.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        return forgotPasswordService.forgotPassword(forgotPasswordRequest);
    }
}
