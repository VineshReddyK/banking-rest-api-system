package com.vinesh.banking.controller;

import com.vinesh.banking.dto.TransactionRequest;
import com.vinesh.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public String deposit(@RequestBody TransactionRequest request) {
        return transactionService.deposit();
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody TransactionRequest request) {
        return transactionService.withdraw();
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransactionRequest request) {
        return transactionService.transfer();
    }
}
