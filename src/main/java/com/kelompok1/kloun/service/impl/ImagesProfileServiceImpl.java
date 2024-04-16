package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.dto.response.ImagesProfileResponse;
import com.kelompok1.kloun.entity.ImagesProfile;
import com.kelompok1.kloun.repository.ImagesProfileRepository;
import com.kelompok1.kloun.service.ImagesProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImagesProfileServiceImpl implements ImagesProfileService {
    private final ImagesProfileRepository imagesProfileRepository;
    private final FirebaseImageService firebaseImageService;

    @Override
    public ImagesProfileResponse uploadProfileImage(MultipartFile file) {
        try {
            String urlImage = firebaseImageService.upload(file);
            String imageName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            ImagesProfile imagesProfile = ImagesProfile.builder()
                    .name(imageName)
                    .url(urlImage)
                    .build();

            imagesProfileRepository.save(imagesProfile);
            return ImagesProfileResponse.builder()
                    .id(imagesProfile.getId())
                    .name(imagesProfile.getName())
                    .url(imagesProfile.getUrl())
                    .build();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload Profile Image Failed, Please Try Again");
        }
    }

    @Override
    public ImagesProfileResponse getProfileImageById(String id) {
        ImagesProfile imagesProfile = imagesProfileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile Image Not Found, Please Try Again"));
        return ImagesProfileResponse.builder()
                .id(imagesProfile.getId())
                .name(imagesProfile.getName())
                .url(imagesProfile.getUrl())
                .build();
    }

    @Override
    public void deleteImageProfile(String id) {
        try {
            ImagesProfileResponse imagesProfileResponse = getProfileImageById(id);
            ImagesProfile imagesProfile = ImagesProfile.builder()
                    .id(imagesProfileResponse.getId())
                    .name(imagesProfileResponse.getName())
                    .url(imagesProfileResponse.getUrl())
                    .build();
            firebaseImageService.deleteFile(getFileNameFromUrl(imagesProfile.getUrl()));
            imagesProfileRepository.delete(imagesProfile);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Delete Profile Image Failed, Please Try Again");
        }
    }

    public String getFileNameFromUrl(String url) {
        int lastIndexOfSlash = url.lastIndexOf("/");
        String fileNameWithExtension = url.substring(lastIndexOfSlash + 1);
        int lastIndexOfQuestionMark = fileNameWithExtension.lastIndexOf("?");
        if (lastIndexOfQuestionMark != -1) {
            fileNameWithExtension = fileNameWithExtension.substring(0, lastIndexOfQuestionMark);
        }
        return fileNameWithExtension;
    }
}
