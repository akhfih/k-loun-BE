package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.dto.request.ResetPasswordRequest;
import com.kelompok1.kloun.dto.response.ResetPasswordResponse;
import com.kelompok1.kloun.entity.UserCredential;
import com.kelompok1.kloun.repository.UserCredentialRepository;
import com.kelompok1.kloun.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        UserCredential user = userCredentialRepository.findByUsername(resetPasswordRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found, Please Try Again"));

        if (passwordEncoder.matches(resetPasswordRequest.getOldPassword(), user.getPassword())) {
            UserCredential userCredential = UserCredential.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(resetPasswordRequest.getNewPassword()))
                    .role(user.getRole())
                    .build();
            userCredentialRepository.save(userCredential);
            return ResetPasswordResponse.builder()
                    .username(user.getUsername())
                    .newPassword(user.getPassword())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password Wrong, Please Try Again");
        }
    }
}
