package com.vinesh.banking.controller;

import com.vinesh.banking.dto.AccountRequest;
import com.vinesh.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public String createAccount(@RequestBody AccountRequest request) {
        return accountService.createAccount();
    }

    @GetMapping
    public String getAccounts() {
        return accountService.getAccounts();
    }
}
