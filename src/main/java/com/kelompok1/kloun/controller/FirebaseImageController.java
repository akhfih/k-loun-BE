package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.service.impl.FirebaseImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/firebase-image")
public class FirebaseImageController {
    private final FirebaseImageService firebaseImageService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile multipartFile){
        String url = firebaseImageService.upload(multipartFile);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Upload Image Success")
                        .data(url)
                        .build());
    }
}
