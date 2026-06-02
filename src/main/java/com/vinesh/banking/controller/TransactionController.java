package com.vinesh.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
  @PostMapping("/deposit")
    public String deposit(@RequestBody TransactionRequest request) {
        return "Deposit successful";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody TransactionRequest request) {
        return "Withdrawal successful";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransactionRequest request) {
        return "Transfer successful";
    }
}
