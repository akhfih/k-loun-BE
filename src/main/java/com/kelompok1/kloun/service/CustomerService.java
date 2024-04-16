package com.kelompok1.kloun.service;

import com.kelompok1.kloun.dto.request.CustomerRequest;
import com.kelompok1.kloun.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> getAll();
    CustomerResponse getCustomerById(String id);
    CustomerResponse updateCustomer(CustomerRequest customerRequest);
    CustomerResponse updateCustomerWithImage(CustomerRequest customerRequest);
    CustomerResponse updateImageProfileCustomer(MultipartFile profile, String userId);
    void deleteCustomer(String id);
    Page<CustomerResponse> getAllCustomerByPage(Integer page, Integer size);
    CustomerResponse getCustomerByEmail(String email);
}
