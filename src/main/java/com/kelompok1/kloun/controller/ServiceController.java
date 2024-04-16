package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.request.ServiceRequest;
import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.entity.Service;
import com.kelompok1.kloun.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping("/api/services")
    public ResponseEntity<?> getServices() {
        List<Service> services = serviceService.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<List<Service>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Service Success")
                        .data(services)
                        .build());
    }

    @GetMapping("/services/{idService}")
    public ResponseEntity<?> getServiceById(@PathVariable String idService) {
        Service service = serviceService.getById(idService);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<Service>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Service By Id Success")
                        .data(service)
                        .build());
    }

    @PostMapping("/services")
    public ResponseEntity<?> createService(@RequestBody ServiceRequest serviceRequest) {
        Service service = serviceService.save(Service.builder()
                        .name(serviceRequest.getName())
                        .description(serviceRequest.getDescription())
                        .price(serviceRequest.getPrice())
                        .duration(serviceRequest.getDuration())
                        .isActive(true)
                        .image(serviceRequest.getImage())
                .build());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.<Service>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Create Service Success")
                        .data(service)
                        .build());
    }

    @PutMapping("/services")
    public ResponseEntity<?> updateService(@RequestBody ServiceRequest serviceRequest) {
        Service service = serviceService.update(Service.builder()
                        .id(serviceRequest.getId())
                        .name(serviceRequest.getName())
                        .description(serviceRequest.getDescription())
                        .duration(serviceRequest.getDuration())
                        .price(serviceRequest.getPrice())
                        .isActive(serviceRequest.getIsActive())
                        .image(serviceRequest.getImage())
                .build());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<Service>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Service Success")
                        .data(service)
                        .build());
    }

    @DeleteMapping("/services/{idService}")
    public ResponseEntity<?> deleteServiceById(@PathVariable String idService) {
        serviceService.delete(idService);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<Object>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Delete Service Success")
                        .data(null)
                        .build());
    }
}
