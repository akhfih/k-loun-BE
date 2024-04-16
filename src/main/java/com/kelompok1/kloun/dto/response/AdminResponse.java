package com.kelompok1.kloun.dto.response;

import com.kelompok1.kloun.entity.ImagesProfile;
import com.kelompok1.kloun.entity.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AdminResponse {
    private String id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String address;
    private UserCredential userCredential;
    private ImagesProfile profileImage;
}
