package com.vinesh.banking.controller;

import com.vinesh.banking.dto.AccountRequest;
import com.vinesh.banking.dto.AccountResponse;
import com.vinesh.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/{userId}")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable Long userId,
                                                          @jakarta.validation.Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts() {
        return ResponseEntity.ok(accountService.getAccounts());
    }
}
