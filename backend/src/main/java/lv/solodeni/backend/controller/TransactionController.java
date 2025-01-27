package lv.solodeni.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import lv.solodeni.backend.model.Transaction;
import lv.solodeni.backend.model.dto.response.TransactionDto;
import lv.solodeni.backend.service.transaction.ITransactionService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @GetMapping("/{accountId}")
    public ResponseEntity<List<TransactionDto>> getUserTransactions(@PathVariable Long accountId) {
        return new ResponseEntity<>(transactionService.getLoggedInUserTransactions(accountId), HttpStatus.OK);
    }

}
