package com.kelompok1.kloun.service;

import com.kelompok1.kloun.dto.request.AdminRequest;
import com.kelompok1.kloun.dto.response.AdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {
    List<AdminResponse> getAll();
    AdminResponse getAdminById(String id);
    AdminResponse updateAdmin(AdminRequest adminRequest);
    AdminResponse updateAdminWithImage(AdminRequest adminRequest);
    AdminResponse updateImageProfileAdmin(MultipartFile profile, String userId);
    void deleteAdmin(String id);
    Page<AdminResponse> getAllByPage(Integer page, Integer size);
}
