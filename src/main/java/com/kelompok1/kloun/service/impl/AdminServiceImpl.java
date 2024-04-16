package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.constant.ERole;
import com.kelompok1.kloun.dto.request.AdminRequest;
import com.kelompok1.kloun.dto.response.AdminResponse;
import com.kelompok1.kloun.dto.response.ImagesProfileResponse;
import com.kelompok1.kloun.entity.ImagesProfile;
import com.kelompok1.kloun.entity.User;
import com.kelompok1.kloun.entity.UserCredential;
import com.kelompok1.kloun.repository.UserCredentialRepository;
import com.kelompok1.kloun.repository.UserRepository;
import com.kelompok1.kloun.service.AdminService;
import com.kelompok1.kloun.service.ImagesProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final ImagesProfileService imagesProfileService;

    @Override
    public List<AdminResponse> getAll() {
        try {
            List<User> users = userRepository.findAllByIsActiveTrueAndUserCredential_Role_Name(ERole.ROLE_ADMIN);

            return users
                    .stream()
                    .map(user -> AdminResponse.builder()
                            .id(user.getId())
                            .username(user.getUserCredential().getUsername())
                            .password(user.getUserCredential().getPassword())
                            .email(user.getEmail())
                            .name(user.getName())
                            .phone(user.getPhone())
                            .address(user.getAddress())
                            .userCredential(user.getUserCredential())
                            .profileImage(user.getImageProfile())
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Get All Admin Failed, Please Try Again");
        }

    }

    @Override
    public AdminResponse getAdminById(String id) {
        User admin = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found, Please Try Again"));
        return AdminResponse.builder()
                .id(admin.getId())
                .username(admin.getUserCredential().getUsername())
                .password(admin.getUserCredential().getPassword())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .address(admin.getAddress())
                .userCredential(admin.getUserCredential())
                .profileImage(admin.getImageProfile())
                .build();
    }

    @Override
    public AdminResponse updateAdminWithImage(AdminRequest adminRequest) {
        try {
            AdminResponse admin = getAdminById(adminRequest.getId());
            ImagesProfileResponse imagesProfileResponse;

            User user = User.builder()
                    .id(adminRequest.getId())
                    .name(adminRequest.getName())
                    .email(adminRequest.getEmail())
                    .address(adminRequest.getAddress())
                    .phone(adminRequest.getPhone())
                    .isActive(true)
                    .userCredential(admin.getUserCredential())
                    .build();
            if (admin.getProfileImage() != null && adminRequest.getProfileImage() == null){
                imagesProfileResponse = imagesProfileService.getProfileImageById(admin.getProfileImage().getId());
                ImagesProfile imagesProfile = ImagesProfile.builder()
                        .id(imagesProfileResponse.getId())
                        .name(imagesProfileResponse.getName())
                        .url(imagesProfileResponse.getUrl())
                        .build();
                user.setImageProfile(imagesProfile);
            }

            if (adminRequest.getProfileImage() != null) {
                imagesProfileResponse = imagesProfileService.uploadProfileImage(adminRequest.getProfileImage());
                ImagesProfile imagesProfile = ImagesProfile.builder()
                        .id(imagesProfileResponse.getId())
                        .name(imagesProfileResponse.getName())
                        .url(imagesProfileResponse.getUrl())
                        .build();
                user.setImageProfile(imagesProfile);
            }

            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(admin.getUserCredential().getId())
                    .username(adminRequest.getUsername())
                    .password(admin.getUserCredential().getPassword())
                    .role(admin.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);

            if (adminRequest.getProfileImage() != null && admin.getProfileImage() != null) {
                imagesProfileService.deleteImageProfile(admin.getProfileImage().getId());
            }

            return AdminResponse.builder()
                    .id(user.getId())
                    .username(user.getUserCredential().getUsername())
                    .password(user.getUserCredential().getPassword())
                    .email(user.getEmail())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .userCredential(user.getUserCredential())
                    .profileImage(user.getImageProfile())
                    .build();

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Admin With Profile Image Failed, Please Try Again");
        }
    }

    @Override
    public AdminResponse updateAdmin(AdminRequest adminRequest) {
        try {
            AdminResponse admin = getAdminById(adminRequest.getId());

            User user = User.builder()
                    .id(adminRequest.getId())
                    .name(adminRequest.getName())
                    .email(adminRequest.getEmail())
                    .address(adminRequest.getAddress())
                    .phone(adminRequest.getPhone())
                    .isActive(true)
                    .userCredential(admin.getUserCredential())
                    .build();

            ImagesProfileResponse imagesProfileResponse;
            if (admin.getProfileImage() != null) {
                imagesProfileResponse = imagesProfileService.getProfileImageById(admin.getProfileImage().getId());

                ImagesProfile imagesProfile = ImagesProfile.builder()
                        .id(imagesProfileResponse.getId())
                        .name(imagesProfileResponse.getName())
                        .url(imagesProfileResponse.getUrl())
                        .build();
                user.setImageProfile(imagesProfile);
            }

            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(admin.getUserCredential().getId())
                    .username(adminRequest.getUsername())
                    .password(admin.getUserCredential().getPassword())
                    .role(admin.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);

            return AdminResponse.builder()
                    .id(user.getId())
                    .username(user.getUserCredential().getUsername())
                    .password(user.getUserCredential().getPassword())
                    .email(user.getEmail())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .userCredential(user.getUserCredential())
                    .profileImage(user.getImageProfile())
                    .build();

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Admin Failed, Please Try Again");
        }
    }

    @Override
    public AdminResponse updateImageProfileAdmin(MultipartFile profile, String userId) {
        try {
            AdminResponse adminResponse = getAdminById(userId);

            ImagesProfileResponse imagesProfileResponse = imagesProfileService.uploadProfileImage(profile);

            ImagesProfile imagesProfile = ImagesProfile.builder()
                    .id(imagesProfileResponse.getId())
                    .name(imagesProfileResponse.getName())
                    .url(imagesProfileResponse.getUrl())
                    .build();

            User user = User.builder()
                    .id(adminResponse.getId())
                    .name(adminResponse.getName())
                    .email(adminResponse.getEmail())
                    .address(adminResponse.getAddress())
                    .phone(adminResponse.getPhone())
                    .isActive(true)
                    .userCredential(adminResponse.getUserCredential())
                    .imageProfile(imagesProfile)
                    .build();
            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(adminResponse.getUserCredential().getId())
                    .username(adminResponse.getUsername())
                    .password(adminResponse.getUserCredential().getPassword())
                    .role(adminResponse.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);

            if (adminResponse.getProfileImage() != null) {
                imagesProfileService.deleteImageProfile(adminResponse.getProfileImage().getId());
            }

            return AdminResponse.builder()
                    .id(user.getId())
                    .username(user.getUserCredential().getUsername())
                    .password(user.getUserCredential().getPassword())
                    .email(user.getEmail())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .userCredential(user.getUserCredential())
                    .profileImage(user.getImageProfile())
                    .build();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Profile Image Admin Failed, Please Try Again");
        }
    }

    @Override
    public Page<AdminResponse> getAllByPage(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<User> users = userRepository.findByIsActiveTrueAndUserCredential_Role_Name(ERole.ROLE_ADMIN, pageable);

            List<AdminResponse> adminResponses = users.getContent()
                    .stream()
                    .map(user -> AdminResponse.builder()
                            .id(user.getId())
                            .username(user.getUserCredential().getUsername())
                            .password(user.getUserCredential().getPassword())
                            .email(user.getEmail())
                            .name(user.getName())
                            .phone(user.getPhone())
                            .address(user.getAddress())
                            .userCredential(user.getUserCredential())
                            .profileImage(user.getImageProfile())
                            .build())
                    .collect(Collectors.toList());
            return new PageImpl<>(adminResponses, pageable, users.getTotalElements());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Get All Admin Failed, Please Try Again");
        }
    }

    @Override
    public void deleteAdmin(String id) {
        try {
            AdminResponse admin = getAdminById(id);
            User user = User.builder()
                    .id(admin.getId())
                    .name(admin.getName())
                    .email(admin.getEmail())
                    .address(admin.getAddress())
                    .phone(admin.getPhone())
                    .isActive(false)
                    .userCredential(admin.getUserCredential())
                    .imageProfile(admin.getProfileImage())
                    .build();
            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(admin.getUserCredential().getId())
                    .username(admin.getUsername())
                    .password(admin.getPassword())
                    .role(admin.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Delete Admin Failed, Please Try Again");
        }
    }
}
