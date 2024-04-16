package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.request.AdminRequest;
import com.kelompok1.kloun.dto.response.AdminResponse;
import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.dto.response.PagingResponse;
import com.kelompok1.kloun.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<?> getAllAdmin() {
        List<AdminResponse> adminResponses = adminService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Admin Success")
                        .data(adminResponses)
                        .paging(null)
                        .build());
    }

    @GetMapping("/page")
    public ResponseEntity<?> getAllAdminPage(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
        Page<AdminResponse> adminResponses = adminService.getAllByPage(page, size);

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(adminResponses.getTotalPages())
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Admin Success")
                        .data(adminResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable String id) {
        AdminResponse adminResponse = adminService.getAdminById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Admin By Id Success")
                        .data(adminResponse)
                        .paging(null)
                        .build());
    }

    @PutMapping("/with-image")
    public ResponseEntity<?> updateAdminWithImage(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "image", required = false) MultipartFile profileImage) {
        AdminRequest adminRequest = AdminRequest.builder()
                .id(id)
                .username(username)
                .email(email)
                .name(name)
                .phone(phone)
                .address(address)
                .profileImage(profileImage)
                .build();

        AdminResponse adminResponse = adminService.updateAdminWithImage(adminRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Admin With Image Success")
                        .data(adminResponse)
                        .paging(null)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateAdmin(@RequestBody AdminRequest adminRequest){
        AdminResponse adminResponse = adminService.updateAdmin(adminRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Admin Success")
                        .data(adminResponse)
                        .paging(null)
                        .build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateImageProfileAdmin(
            @PathVariable String userId,
            @RequestParam(name = "imageProfile") MultipartFile imageProfile) {

        AdminResponse adminResponse = adminService.updateImageProfileAdmin(imageProfile, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Profile Image Admin Success")
                        .data(adminResponse)
                        .paging(null)
                        .build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String id) {
        adminService.deleteAdmin(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Delete Admin Success")
                        .data(null)
                        .paging(null)
                        .build());
    }
}
