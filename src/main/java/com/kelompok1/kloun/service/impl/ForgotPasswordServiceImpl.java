package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.dto.request.ForgotPasswordRequest;
import com.kelompok1.kloun.dto.response.ForgotPasswordResponse;
import com.kelompok1.kloun.entity.User;
import com.kelompok1.kloun.entity.UserCredential;
import com.kelompok1.kloun.repository.UserCredentialRepository;
import com.kelompok1.kloun.repository.UserRepository;
import com.kelompok1.kloun.service.ForgotPasswordService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("$(spring.mail.username)")
    private String fromMail;

    @Override

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String newPassword = generateNewPassword();

        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found, Please Try Again"));

        UserCredential userCredential = user.getUserCredential();
        userCredential.setPassword(passwordEncoder.encode(newPassword));
        userCredentialRepository.save(userCredential);

        sendPasswordResetEmail(forgotPasswordRequest.getEmail(), newPassword);

        return ForgotPasswordResponse.builder()
                .email(forgotPasswordRequest.getEmail())
                .password(newPassword)
                .build();
    }

    private String generateNewPassword() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(randomIndex));
        }
        return sb.toString();
    }

    private void sendPasswordResetEmail(String email, String newPassword) {
        String htmlBody = "<p>Your new password: <b>" + newPassword + "</b></p>";
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromMail);
            helper.setTo(email);
            helper.setSubject("New Password");
            helper.setText(htmlBody, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}
