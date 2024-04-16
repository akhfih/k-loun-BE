package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.request.ResetPasswordRequest;
import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.dto.response.ResetPasswordResponse;
import com.kelompok1.kloun.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reset-password")
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;

    @PutMapping
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        ResetPasswordResponse resetPasswordResponse = resetPasswordService.resetPassword(resetPasswordRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Reset Password Success")
                        .data(resetPasswordResponse)
                        .paging(null)
                        .build());
    }
}
