package com.kelompok1.kloun.controller;

import com.kelompok1.kloun.dto.request.TransactionRequest;
import com.kelompok1.kloun.dto.response.CommonResponse;
import com.kelompok1.kloun.dto.response.PagingResponse;
import com.kelompok1.kloun.dto.response.TransactionResponse;
import com.kelompok1.kloun.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> order(@RequestBody TransactionRequest request) {
        TransactionResponse savedTransaction = transactionService.orderNewTransaction(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Transaction Success")
                        .data(savedTransaction)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactionWithPagination(
            @RequestParam(name = "userId", required = false) String userId,
            @RequestParam(name = "statusId", required = false) String statusId,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size ) {
        Page<TransactionResponse> transactions;
        if (userId != null && statusId != null) {
            transactions = transactionService.getAllTransactionByUserIdAndStatusIdAndByPage(userId, statusId, page, size);
        } else if (userId != null) {
            transactions = transactionService.getAllTransactionByUserIdAndByPage(userId, page, size);
        } else if (statusId != null) {
            transactions = transactionService.getAllTransactionByStatusIdAndByPage(statusId, page, size);
        } else {
            transactions = transactionService.getAllTransactionByPage(page, size);
        }

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(transactions.getTotalPages())
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Transaction Success")
                        .data(transactions.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @GetMapping("/active/{userId}")
    public ResponseEntity<?> getActiveTransactionCustomerWithPagination(
            @PathVariable String userId,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
        Page<TransactionResponse> transactions = transactionService.getAllTransactionByIsActiveAndByPage(userId, page, size);

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(transactions.getTotalPages())
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All Transaction Active Success")
                        .data(transactions.getContent())
                        .paging(pagingResponse)
                        .build());

    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getTransactionHistoryCustomerWithPagination(
            @PathVariable String userId,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
        Page<TransactionResponse> transactions = transactionService.getAllTransactionByStatusId1AndStatusId2AndByPage(userId, page, size);

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(transactions.getTotalPages())
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get All History Transaction Success")
                        .data(transactions.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        TransactionResponse transaction = transactionService.getTransactionById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Get Transaction By ID Success")
                        .data(transaction)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateStatus(@RequestBody TransactionRequest request) {
        TransactionResponse updatedTransaction = transactionService.updateStatusTransaction(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Transaction Status Success")
                        .data(updatedTransaction)
                        .build());
    }

    @PutMapping("/confirm-accept")
    public ResponseEntity<?> customerConfirmAccept(@RequestBody TransactionRequest request) {
        TransactionResponse updatedTransaction = transactionService.customerConfirmAcceptLaundry(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Confirm Accept Transaction Success")
                        .data(updatedTransaction)
                        .build());
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody TransactionRequest request) {
        TransactionResponse updatedTransaction = transactionService.cancelTransaction(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Cancel Transaction Success")
                        .data(updatedTransaction)
                        .build());
    }

    @PutMapping("/paid")
    public ResponseEntity<?> updatePaid(@RequestBody TransactionRequest request) {

        TransactionResponse updatedTransaction = transactionService.updatePaidTransaction(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Update Transaction Paid Status Success")
                        .data(updatedTransaction)
                        .build());
    }
}
