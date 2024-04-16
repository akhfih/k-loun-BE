package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.request.CustomerRequest;
import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.dto.response.CustomerResponse;
import com.kelompok1.kloun.dto.response.PagingResponse;
import com.kelompok1.kloun.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAllCustomer(){
        List<CustomerResponse> customerResponses = customerService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Customer Success")
                        .data(customerResponses)
                        .paging(null)
                        .build());
    }
    @GetMapping("/page")
    public ResponseEntity<?> getAllCustomerPage(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size){
        Page<CustomerResponse> customerResponses = customerService.getAllCustomerByPage(page, size);

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(customerResponses.getTotalPages())
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Customer Success")
                        .data(customerResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id){
        CustomerResponse customerResponse = customerService.getCustomerById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Customer By Id Success")
                        .data(customerResponse)
                        .paging(null)
                        .build());
    }

    @PutMapping("/with-image")
    public ResponseEntity<?> updateCustomerWithImage(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "image", required = false) MultipartFile profileImage){
        CustomerRequest customerRequest = CustomerRequest.builder()
                .id(id)
                .username(username)
                .email(email)
                .name(name)
                .phone(phone)
                .address(address)
                .profileImage(profileImage)
                .build();
        CustomerResponse customerResponse = customerService.updateCustomerWithImage(customerRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Customer Success")
                        .data(customerResponse)
                        .paging(null)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerService.updateCustomer(customerRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Customer Success")
                        .data(customerResponse)
                        .paging(null)
                        .build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateImageProfileCustomer(
            @PathVariable String userId,
            @RequestParam(name = "imageProfile") MultipartFile imageProfile){
        CustomerResponse customerResponse = customerService.updateImageProfileCustomer(imageProfile, userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Profile Image Customer Success")
                        .data(customerResponse)
                        .paging(null)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id){
        customerService.deleteCustomer(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Delete Customer Success")
                        .data(null)
                        .paging(null)
                        .build());
    }

}
