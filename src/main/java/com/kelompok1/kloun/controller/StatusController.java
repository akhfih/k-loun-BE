package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.StatusDTO;
import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.entity.Status;
import com.kelompok1.kloun.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statuses")
public class StatusController {
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<?> getStatuses() {
        List<Status> statuses = statusService.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<List<Status>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Status Success")
                        .data(statuses)
                        .build());
    }

    @GetMapping("/{id_status}")
    public ResponseEntity<?> getStatusById(@PathVariable String id_status) {
        Status status = statusService.getById(id_status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<Status>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Status By Id Success")
                        .data(status)
                        .build());
    }

    @GetMapping("/by-name")
    public ResponseEntity<?> getStatusByName(@RequestParam String name) {
        Status status = statusService.getByName(name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<Status>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Status By Name Success")
                        .data(status)
                        .build());
    }

    @PostMapping
    public ResponseEntity<?> createStatus(@RequestBody StatusDTO statusRequest) {
        Status status = statusService.save(Status.builder()
                        .name(statusRequest.getName())
                        .indicator(statusRequest.getIndicator())
                        .isActive(true)
                .build());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.<StatusDTO>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Create Status Success")
                        .data(StatusDTO.builder()
                                .name(status.getName())
                                .indicator(status.getIndicator())
                                .build())
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateStatus(@RequestBody Status statusRequest) {
        Status status = statusService.update(Status.builder()
                        .id(statusRequest.getId())
                        .name(statusRequest.getName())
                        .indicator(statusRequest.getIndicator())
                .build());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<Status>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Status Success")
                        .data(status)
                        .build());
    }

    @DeleteMapping("/{id_status}")
    public ResponseEntity<?> deleteStatusById(@PathVariable String id_status) {
        statusService.delete(id_status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.<Object>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Delete Status Success")
                        .data(null)
                        .build());
    }
}
