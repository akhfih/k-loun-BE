package com.kelompok1.kloun.service;

import com.kelompok1.kloun.entity.AppUser;
import com.kelompok1.kloun.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser loadUserByUserId(String id);
    User getUserInfo(String userCredentialId);
    User getUserById(String userId);
}
