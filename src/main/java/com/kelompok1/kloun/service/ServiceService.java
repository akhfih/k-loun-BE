package com.kelompok1.kloun.service;

import com.kelompok1.kloun.entity.Service;

import java.util.List;

public interface ServiceService {
    List<Service> getAll();
    Service getById(String id);
    Service save(Service service);
    Service update(Service service);
    void delete(String id);
}
