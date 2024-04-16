package com.kelompok1.kloun.service;

import com.kelompok1.kloun.entity.Status;

import java.util.List;

public interface StatusService {
    List<Status> getAll();
    Status getById(String id);
    Status getByName(String name);
    Status save(Status status);
    Status update(Status status);
    void delete(String id);
}
