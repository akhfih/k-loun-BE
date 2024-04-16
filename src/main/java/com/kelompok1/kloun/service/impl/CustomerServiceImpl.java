package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.constant.ERole;
import com.kelompok1.kloun.dto.request.CustomerRequest;
import com.kelompok1.kloun.dto.response.CustomerResponse;
import com.kelompok1.kloun.dto.response.ImagesProfileResponse;
import com.kelompok1.kloun.entity.ImagesProfile;
import com.kelompok1.kloun.entity.User;
import com.kelompok1.kloun.entity.UserCredential;
import com.kelompok1.kloun.repository.UserCredentialRepository;
import com.kelompok1.kloun.repository.UserRepository;
import com.kelompok1.kloun.service.CustomerService;
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
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final ImagesProfileService imagesProfileService;

    @Override
    public List<CustomerResponse> getAll() {
        try {
            List<User> users = userRepository.findAllByIsActiveTrueAndUserCredential_Role_Name(ERole.ROLE_CUSTOMER);

            return users
                    .stream()
                    .map(user -> CustomerResponse.builder()
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Get All Customer Failed, Please Try Again");
        }
    }

    @Override
    public Page<CustomerResponse> getAllCustomerByPage(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<User> users = userRepository.findByIsActiveTrueAndUserCredential_Role_Name(ERole.ROLE_CUSTOMER, pageable);

            List<CustomerResponse> customerResponses = users.getContent()
                    .stream()
                    .map(user -> CustomerResponse.builder()
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
            return new PageImpl<>(customerResponses, pageable, users.getTotalElements());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Get All Customer Failed, Please Try Again");
        }
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        User customer = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found, Please Try Again"));

        return CustomerResponse.builder()
                .id(customer.getId())
                .username(customer.getUserCredential().getUsername())
                .password(customer.getUserCredential().getPassword())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .userCredential(customer.getUserCredential())
                .profileImage(customer.getImageProfile())
                .build();
    }

    @Override
    public CustomerResponse updateCustomerWithImage(CustomerRequest customerRequest) {
        try {
            CustomerResponse customer = getCustomerById(customerRequest.getId());
            ImagesProfileResponse imagesProfileResponse;

            User user = User.builder()
                    .id(customerRequest.getId())
                    .name(customerRequest.getName())
                    .email(customerRequest.getEmail())
                    .address(customerRequest.getAddress())
                    .phone(customerRequest.getPhone())
                    .isActive(true)
                    .userCredential(customer.getUserCredential())
                    .build();
            if (customer.getProfileImage() != null && customerRequest.getProfileImage() == null){
                imagesProfileResponse = imagesProfileService.getProfileImageById(customer.getProfileImage().getId());
                ImagesProfile imagesProfile = ImagesProfile.builder()
                        .id(imagesProfileResponse.getId())
                        .name(imagesProfileResponse.getName())
                        .url(imagesProfileResponse.getUrl())
                        .build();
                user.setImageProfile(imagesProfile);
            }

            if (customerRequest.getProfileImage() != null) {
                imagesProfileResponse = imagesProfileService.uploadProfileImage(customerRequest.getProfileImage());
                ImagesProfile imagesProfile = ImagesProfile.builder()
                        .id(imagesProfileResponse.getId())
                        .name(imagesProfileResponse.getName())
                        .url(imagesProfileResponse.getUrl())
                        .build();
                user.setImageProfile(imagesProfile);
            }

            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(customer.getUserCredential().getId())
                    .username(customerRequest.getUsername())
                    .password(customer.getUserCredential().getPassword())
                    .role(customer.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);

            if (customerRequest.getProfileImage() != null && customer.getProfileImage() != null) {
                imagesProfileService.deleteImageProfile(customer.getProfileImage().getId());
            }

            return CustomerResponse.builder()
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Customer With Image Failed, Please Try Again");
        }
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customerRequest) {
        try {
            CustomerResponse customer = getCustomerById(customerRequest.getId());

            User user = User.builder()
                    .id(customerRequest.getId())
                    .name(customerRequest.getName())
                    .email(customerRequest.getEmail())
                    .address(customerRequest.getAddress())
                    .phone(customerRequest.getPhone())
                    .isActive(true)
                    .userCredential(customer.getUserCredential())
                    .build();

            ImagesProfileResponse imagesProfileResponse;
            if (customer.getProfileImage() != null) {
                imagesProfileResponse = imagesProfileService.getProfileImageById(customer.getProfileImage().getId());

                ImagesProfile imagesProfile = ImagesProfile.builder()
                        .id(imagesProfileResponse.getId())
                        .name(imagesProfileResponse.getName())
                        .url(imagesProfileResponse.getUrl())
                        .build();
                user.setImageProfile(imagesProfile);
            }
            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(customer.getUserCredential().getId())
                    .username(customerRequest.getUsername())
                    .password(customer.getUserCredential().getPassword())
                    .role(customer.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);

            return CustomerResponse.builder()
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Customer Failed, Please Try Again");
        }
    }

    @Override
    public CustomerResponse updateImageProfileCustomer(MultipartFile profile, String userId) {
        try {
            CustomerResponse customerResponse = getCustomerById(userId);

            ImagesProfileResponse imagesProfileResponse = imagesProfileService.uploadProfileImage(profile);

            ImagesProfile imagesProfile = ImagesProfile.builder()
                    .id(imagesProfileResponse.getId())
                    .name(imagesProfileResponse.getName())
                    .url(imagesProfileResponse.getUrl())
                    .build();

            User user = User.builder()
                    .id(customerResponse.getId())
                    .name(customerResponse.getName())
                    .email(customerResponse.getEmail())
                    .address(customerResponse.getAddress())
                    .phone(customerResponse.getPhone())
                    .isActive(true)
                    .userCredential(customerResponse.getUserCredential())
                    .imageProfile(imagesProfile)
                    .build();
            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(customerResponse.getUserCredential().getId())
                    .username(customerResponse.getUsername())
                    .password(customerResponse.getUserCredential().getPassword())
                    .role(customerResponse.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);

            if (customerResponse.getProfileImage() != null) {
                imagesProfileService.deleteImageProfile(customerResponse.getProfileImage().getId());
            }

            return CustomerResponse.builder()
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Profile Image Customer Failed, Please Try Again");
        }
    }

    @Override
    public void deleteCustomer(String id) {
        try {
            CustomerResponse customer = getCustomerById(id);

            User user = User.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .address(customer.getAddress())
                    .phone(customer.getPhone())
                    .isActive(false)
                    .userCredential(customer.getUserCredential())
                    .imageProfile(customer.getProfileImage())
                    .build();
            userRepository.save(user);
            UserCredential userCredential = UserCredential.builder()
                    .id(customer.getUserCredential().getId())
                    .username(customer.getUsername())
                    .password(customer.getPassword())
                    .role(customer.getUserCredential().getRole())
                    .build();
            userCredentialRepository.save(userCredential);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Delete Customer Failed, Please Try Again");
        }
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        User customer = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found, Please Try Again"));

        return CustomerResponse.builder()
                .id(customer.getId())
                .username(customer.getUserCredential().getUsername())
                .password(customer.getUserCredential().getPassword())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .userCredential(customer.getUserCredential())
                .profileImage(customer.getImageProfile())
                .build();
    }
}
