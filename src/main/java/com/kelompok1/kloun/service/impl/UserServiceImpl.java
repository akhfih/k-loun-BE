package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.entity.AppUser;
import com.kelompok1.kloun.entity.User;
import com.kelompok1.kloun.entity.UserCredential;
import com.kelompok1.kloun.repository.UserCredentialRepository;
import com.kelompok1.kloun.repository.UserRepository;
import com.kelompok1.kloun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public AppUser loadUserByUserId(String id) {
        UserCredential userCredential = userCredentialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Credential, Please Try Again"));
        return new AppUser(
                userCredential.getId(),
                userCredential.getUsername(),
                userCredential.getPassword(),
                userCredential.getRole().getName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Credential, Please Try Again"));
        return new AppUser(
                userCredential.getId(),
                userCredential.getUsername(),
                userCredential.getPassword(),
                userCredential.getRole().getName());
    }

    @Override
    public User getUserInfo(String userCredentialId) {
        return userRepository.findByUserCredentialId(userCredentialId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Invalid Credential, Please Try Again"));
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found, Please Try Again"));
    }
}
