package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.entity.Role;
import com.kelompok1.kloun.repository.RoleRepository;
import com.kelompok1.kloun.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getOrSave(Role role) {
        Optional<Role> optionalRole = roleRepository.findByName(role.getName());
        return optionalRole.orElseGet(() -> roleRepository.save(role));
    }
}
