package com.vinesh.banking.controller;

import com.vinesh.banking.dto.AccountRequest;
import com.vinesh.banking.dto.AccountResponse;
import com.vinesh.banking.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<AccountResponse> createAccount(
            @PathVariable Long userId,
            @Valid @RequestBody AccountRequest request) {

        log.debug("Creating account for userId={}", userId);
        var created = accountService.createAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts() {
        var accounts = accountService.getAccounts();
        return ResponseEntity.ok(accounts);
    }
}
