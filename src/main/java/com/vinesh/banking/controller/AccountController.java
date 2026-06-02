package com.vinesh.banking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  
    @PostMapping
    public String createAccount(@RequestBody AccountRequest request) {
        return "Account created successfully";
    }

    @GetMapping
    public String getAccounts() {
        return "Accounts fetched successfully";
    }
}
