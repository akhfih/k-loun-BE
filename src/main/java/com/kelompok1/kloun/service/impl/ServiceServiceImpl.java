package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.repository.ServiceRepository;
import com.kelompok1.kloun.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Override
    public List<com.kelompok1.kloun.entity.Service> getAll() {
        return serviceRepository.findAllByIsActiveTrue();
    }

    @Override
    public com.kelompok1.kloun.entity.Service getById(String id) {
        return serviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service Doesn't Exist, Please Try Again"));
    }

    @Override
    public com.kelompok1.kloun.entity.Service save(com.kelompok1.kloun.entity.Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public com.kelompok1.kloun.entity.Service update(com.kelompok1.kloun.entity.Service service) {
        getById(service.getId());
        return serviceRepository.save(service);
    }

    @Override
    public void delete(String id) {
        com.kelompok1.kloun.entity.Service service = getById(id);
        service.setIsActive(false);
        serviceRepository.save(service);
    }
}
