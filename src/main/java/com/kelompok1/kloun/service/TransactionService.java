package com.kelompok1.kloun.service;

import com.kelompok1.kloun.dto.request.TransactionRequest;
import com.kelompok1.kloun.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    Page<TransactionResponse> getAllTransactionByPage(Integer page, Integer size);
    Page<TransactionResponse> getAllTransactionByStatusIdAndByPage(String statusId, Integer page, Integer size);
    Page<TransactionResponse> getAllTransactionByUserIdAndByPage(String userId, Integer page, Integer size);
    Page<TransactionResponse> getAllTransactionByUserIdAndStatusIdAndByPage(String userId, String statusId, Integer page, Integer size);
    Page<TransactionResponse> getAllTransactionByStatusId1AndStatusId2AndByPage(String userId, Integer page, Integer size);
    Page<TransactionResponse> getAllTransactionByIsActiveAndByPage(String userId, Integer page, Integer size);
    TransactionResponse updateStatusTransaction(TransactionRequest transactionRequest);
    TransactionResponse updatePaidTransaction(TransactionRequest transactionRequest);
    TransactionResponse cancelTransaction(TransactionRequest transactionRequest);
    TransactionResponse orderNewTransaction(TransactionRequest transactionRequest);
    TransactionResponse customerConfirmAcceptLaundry(TransactionRequest transactionRequest);
    TransactionResponse getTransactionById(String id);
}
