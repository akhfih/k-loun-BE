package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.constant.ERole;
import com.kelompok1.kloun.dto.request.LoginRequest;
import com.kelompok1.kloun.dto.request.RegisterRequest;
import com.kelompok1.kloun.dto.response.LoginResponse;
import com.kelompok1.kloun.dto.response.RegisterResponse;
import com.kelompok1.kloun.entity.AppUser;
import com.kelompok1.kloun.entity.Role;
import com.kelompok1.kloun.entity.User;
import com.kelompok1.kloun.entity.UserCredential;
import com.kelompok1.kloun.repository.UserCredentialRepository;
import com.kelompok1.kloun.repository.UserRepository;
import com.kelompok1.kloun.security.JwtUtil;
import com.kelompok1.kloun.service.AuthService;
import com.kelompok1.kloun.service.RoleService;
import com.kelompok1.kloun.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;

    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse register(RegisterRequest registerRequest, ERole userRole) {
        try {
            validationUtil.validate(registerRequest);
            Role role = Role.builder()
                    .name(userRole)
                    .build();
            Role savedRole = roleService.getOrSave(role);

            UserCredential userCredential = UserCredential.builder()
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(savedRole)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            User user = User.builder()
                    .name(registerRequest.getName())
                    .email(registerRequest.getEmail())
                    .phone(registerRequest.getPhone())
                    .address(registerRequest.getAddress())
                    .userCredential(userCredential)
                    .isActive(true)
                    .build();
            User savedUser = userRepository.save(user);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .email(savedUser.getEmail())
                    .name(savedUser.getName())
                    .phone(savedUser.getPhone())
                    .address(savedUser.getAddress())
                    .role(userCredential.getRole().getName().toString())
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exist, Please Try Again");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        validationUtil.validate(loginRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = (AppUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);

        return LoginResponse.builder()
                .token(token)
                .role(appUser.getRole().name())
                .build();
    }
}
