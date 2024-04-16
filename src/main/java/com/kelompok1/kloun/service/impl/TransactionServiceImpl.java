package com.kelompok1.kloun.service.impl;

import com.kelompok1.kloun.dto.request.TransactionRequest;
import com.kelompok1.kloun.dto.response.TransactionResponse;
import com.kelompok1.kloun.entity.Service;
import com.kelompok1.kloun.entity.Status;
import com.kelompok1.kloun.entity.Transaction;
import com.kelompok1.kloun.entity.User;
import com.kelompok1.kloun.repository.StatusRepository;
import com.kelompok1.kloun.repository.TransactionRepository;
import com.kelompok1.kloun.service.ServiceService;
import com.kelompok1.kloun.service.StatusService;
import com.kelompok1.kloun.service.TransactionService;
import com.kelompok1.kloun.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final StatusRepository statusRepository;
    private final ServiceService serviceService;
    private final UserService userService;
    private final StatusService statusService;

    @Override
    public TransactionResponse orderNewTransaction(TransactionRequest transactionRequest) {
        try {
            User user = userService.getUserById(transactionRequest.getUserId());
            Service service = serviceService.getById(transactionRequest.getServiceId());
            Status status = statusService.getByName("waiting");

            Double weight = (transactionRequest.getWeight() != null) ? transactionRequest.getWeight() : 0.0;
            Long totalPrice = (transactionRequest.getTotalPrice() != null ) ? transactionRequest.getTotalPrice() : 0L;

            Transaction transaction = Transaction.builder()
                    .user(user)
                    .service(service)
                    .status(status)
                    .startDate(LocalDateTime.now())
                    .finishDate(transactionRequest.getFinishDate())
                    .weight(weight)
                    .totalPrice(totalPrice)
                    .paid(transactionRequest.isPaid())
                    .isActive(true)
                    .build();
            transactionRepository.save(transaction);

            return transactionResponse(transaction);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Transaction Failed, Please Try Again");
        }
    }

    @Override
    public TransactionResponse getTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction Not Found,  Please Try Again"));

        return transactionResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> getAllTransactionByPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactions = transactionRepository.findAllByOrderByStartDateDesc(pageable);

        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Get All Transaction Failed,  Please Try Again");
        }
        return transactions.map(this::transactionResponse);
    }

    @Override
    public Page<TransactionResponse> getAllTransactionByStatusIdAndByPage(String statusId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactions = transactionRepository.findByStatusIdOrderByStartDateDesc(statusId, pageable);

        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Get All Transaction With This Status Not Found,  Please Try Again");
        }
        return transactions.map(this::transactionResponse);
    }

    @Override
    public Page<TransactionResponse> getAllTransactionByUserIdAndByPage(String userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactions = transactionRepository.findByUserIdOrderByStartDateDesc(userId, pageable);

        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Get All Transaction With This User Not Found,  Please Try Again");
        }
        return transactions.map(this::transactionResponse);
    }

    @Override
    public Page<TransactionResponse> getAllTransactionByUserIdAndStatusIdAndByPage(String userId, String statusId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactions = transactionRepository.findByUserIdAndStatusIdOrderByStartDateDesc(userId, statusId, pageable);

        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Get All Transaction With This User and Status Not Found,  Please Try Again");
        }
        return transactions.map(this::transactionResponse);
    }

    @Override
    public Page<TransactionResponse> getAllTransactionByStatusId1AndStatusId2AndByPage(String userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactions = transactionRepository.findByUserIdAndStatus_NameOrUserIdAndStatus_NameOrderByStartDateDesc(userId, "cancel", userId, "completed", pageable);

        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Get All History Transaction Not Found,  Please Try Again");
        }
        return transactions.map(this::transactionResponse);
    }

    @Override
    public Page<TransactionResponse> getAllTransactionByIsActiveAndByPage(String userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactions = transactionRepository.findByUserIdAndIsActiveTrueOrderByStartDateDesc(userId, pageable);

        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Get All Transaction Active Not Found,  Please Try Again");
        }
        return transactions.map(this::transactionResponse);
    }

    @Override
    public TransactionResponse updateStatusTransaction(TransactionRequest transactionRequest) {
        try {
            TransactionResponse transactionResponse = getTransactionById(transactionRequest.getId());
            User user = userService.getUserById(transactionResponse.getUser().getId());
            Service service = serviceService.getById(transactionResponse.getService().getId());

            Status status = statusRepository.findById(transactionRequest.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found"));

            Transaction transaction = Transaction.builder()
                    .id(transactionResponse.getId())
                    .user(user)
                    .service(service)
                    .status(status)
                    .startDate(transactionResponse.getStartDate())
                    .finishDate(transactionResponse.getFinishDate())
                    .weight(transactionResponse.getWeight())
                    .totalPrice(transactionResponse.getTotalPrice())
                    .paid(transactionResponse.isPaid())
                    .isActive(true)
                    .build();

            if (transaction.getStatus().getName().equals("washing")) {
                transaction.setFinishDate(calculateFinishDate(service));
            }

            if (transactionRequest.getWeight() != 0.0 && transactionRequest.getWeight() != null &&
                    !transaction.getStatus().getName().equals("accept") &&
                    !transaction.getStatus().getName().equals("pickup") &&
                    !transaction.getStatus().getName().equals("waiting") &&
                    !transaction.getStatus().getName().equals("completed") &&
                    !transaction.getStatus().getName().equals("cancel")) {
                transaction.setWeight(transactionRequest.getWeight());
                transaction.setTotalPrice(calculateTotalPrice(service, transactionRequest));
            }

            transactionRepository.save(transaction);

            return transactionResponse(transaction);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Status Transaction Failed,  Please Try Again");
        }
    }

    @Override
    public TransactionResponse cancelTransaction(TransactionRequest transactionRequest) {
        try {
            TransactionResponse transactionResponse = getTransactionById(transactionRequest.getId());
            Status status = statusService.getByName("cancel");
            if (transactionResponse.getStatus().getName().equals("waiting")) {
                Transaction transaction = Transaction.builder()
                        .id(transactionRequest.getId())
                        .user(transactionResponse.getUser())
                        .status(status)
                        .service(transactionResponse.getService())
                        .startDate(transactionResponse.getStartDate())
                        .finishDate(transactionResponse.getFinishDate())
                        .weight(transactionResponse.getWeight())
                        .totalPrice(transactionResponse.getTotalPrice())
                        .paid(transactionResponse.isPaid())
                        .isActive(false)
                        .build();

                transactionRepository.save(transaction);

                return transactionResponse(transaction);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Cancel, Because The Order Has Already Been Accepted");
            }
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cancel Transaction Failed,  Please Try Again");
        }
    }

    @Override
    public TransactionResponse customerConfirmAcceptLaundry(TransactionRequest transactionRequest) {
        try {
            TransactionResponse transactionResponse = getTransactionById(transactionRequest.getId());
            Status status = statusService.getByName("completed");

            if (transactionResponse.getStatus().getName().equals("delivery")) {
                Transaction transaction = Transaction.builder()
                        .id(transactionRequest.getId())
                        .user(transactionResponse.getUser())
                        .status(status)
                        .service(transactionResponse.getService())
                        .startDate(transactionResponse.getStartDate())
                        .finishDate(transactionResponse.getFinishDate())
                        .weight(transactionResponse.getWeight())
                        .totalPrice(transactionResponse.getTotalPrice())
                        .paid(transactionResponse.isPaid())
                        .isActive(false)
                        .build();

                transactionRepository.save(transaction);

                return transactionResponse(transaction);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Confirm, Because The Order Has Not Been Completed");
            }

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Confirm Accept Transaction Failed,  Please Try Again");
        }
    }

    @Override
    public TransactionResponse updatePaidTransaction(TransactionRequest transactionRequest) {
        try {
            User user = userService.getUserById(transactionRequest.getUserId());
            Service service = serviceService.getById(transactionRequest.getServiceId());
            Status status = statusService.getById(transactionRequest.getServiceId());

            Double weight = (transactionRequest.getWeight() != null) ? transactionRequest.getWeight() : 0.0;
            Long totalPrice = (transactionRequest.getTotalPrice() != null ) ? transactionRequest.getTotalPrice() : 0L;

            Transaction transaction = Transaction.builder()
                    .id(transactionRequest.getId())
                    .user(user)
                    .status(status)
                    .service(service)
                    .startDate(transactionRequest.getStartDate())
                    .finishDate(transactionRequest.getFinishDate())
                    .weight(weight)
                    .totalPrice(totalPrice)
                    .paid(transactionRequest.isPaid())
                    .isActive(true)
                    .build();

            transaction.setPaid(true);
            transactionRepository.save(transaction);

            return transactionResponse(transaction);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Update Paid Status Transaction Failed,  Please Try Again");
        }
    }

    public static Long calculateTotalPrice(Service service, TransactionRequest transactionRequest) {
        Long unitPrice = service.getPrice();
        Double weight = transactionRequest.getWeight();

        Long totalPrice = (long) (unitPrice * weight);

        return totalPrice;
    }

    public static LocalDateTime calculateFinishDate(com.kelompok1.kloun.entity.Service service) {
        LocalDateTime currentDate = LocalDateTime.now();
        Integer duration = service.getDuration();
        Integer days = duration / 24;
        Integer hours = duration % 24;

        LocalDateTime finishDateTime = currentDate.plusDays(days);

        if (hours > 0) {
            finishDateTime = finishDateTime.plusHours(hours);
        }
        return finishDateTime;
    }

    private TransactionResponse transactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .user(transaction.getUser())
                .service(transaction.getService())
                .status(transaction.getStatus())
                .startDate(transaction.getStartDate())
                .finishDate(transaction.getFinishDate())
                .weight(transaction.getWeight())
                .totalPrice(transaction.getTotalPrice())
                .paid(transaction.isPaid())
                .build();
    }
}
