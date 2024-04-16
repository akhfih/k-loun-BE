package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.entity.Status;
import com.kelompok1.kloun.repository.StatusRepository;
import com.kelompok1.kloun.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    @Override
    public List<Status> getAll() {
        return statusRepository.findAllByIsActiveTrue();
    }

    @Override
    public Status getById(String id) {
        try {
            return statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status Doesn't Exist, Please Try Again"));
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching status: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Status save(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public Status update(Status status) {
        getById(status.getId());
        return statusRepository.save(status);
    }

    @Override
    public void delete(String id) {
        Status status = getById(id);
        status.setIsActive(false);
        statusRepository.save(status);
    }

    @Override
    public Status getByName(String name) {
        try {
            return statusRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status Doesn't Exist, Please Try Again"));
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching status: " + ex.getMessage(), ex);
        }
    }
}
