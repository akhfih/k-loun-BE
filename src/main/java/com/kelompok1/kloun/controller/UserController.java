package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.entity.User;
import com.kelompok1.kloun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserInfoByCredential(
            @RequestParam(name = "userCredentialId") String userCredentialId
    ) {
        User user = userService.getUserInfo(userCredentialId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<User>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get User Info Success")
                        .data(user)
                        .build());
    }
}
