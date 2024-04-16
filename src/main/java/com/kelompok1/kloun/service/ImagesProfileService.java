package com.kelompok1.kloun.service;

import com.kelompok1.kloun.dto.response.ImagesProfileResponse;
import com.kelompok1.kloun.entity.ImagesProfile;
import org.springframework.web.multipart.MultipartFile;

public interface ImagesProfileService {
    ImagesProfileResponse uploadProfileImage(MultipartFile file);
    ImagesProfileResponse getProfileImageById(String id);
    void deleteImageProfile(String id);
}
