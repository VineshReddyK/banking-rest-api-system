package com.vinesh.banking.controller;

import com.vinesh.banking.dto.TransactionRequest;
import com.vinesh.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }
}
