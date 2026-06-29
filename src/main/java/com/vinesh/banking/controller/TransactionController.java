package com.vinesh.banking.controller;

import com.vinesh.banking.dto.TransactionRequest;
import com.vinesh.banking.entity.Transaction;
import com.vinesh.banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody TransactionRequest request) {
        String result = transactionService.deposit(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@Valid @RequestBody TransactionRequest request) {
        String result = transactionService.withdraw(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransactionRequest request) {
        String result = transactionService.transfer(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId));
    }
}
