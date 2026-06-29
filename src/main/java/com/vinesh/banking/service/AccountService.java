package com.vinesh.banking.service;

import com.vinesh.banking.dto.AccountRequest;
import com.vinesh.banking.dto.AccountResponse;
import com.vinesh.banking.entity.Account;
import com.vinesh.banking.entity.User;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.repository.AccountRepository;
import com.vinesh.banking.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "accounts", allEntries = true)
    public AccountResponse createAccount(Long userId, AccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // generate a 12-char alphanumeric account number from UUID
        String accountNumber = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();

        // TODO: validate accountType against an allowed set (SAVINGS, CHECKING, etc.)
        String accountType = request.getAccountType() != null ? request.getAccountType() : "SAVINGS";
        double initialBalance = request.getInitialDeposit() != null ? request.getInitialDeposit() : 0.0;

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setBalance(initialBalance);
        account.setUser(user);

        var saved = accountRepository.save(account);

        AccountResponse response = new AccountResponse();
        response.setAccountNumber(saved.getAccountNumber());
        response.setAccountType(saved.getAccountType());
        response.setBalance(saved.getBalance());
        return response;
    }

    @Cacheable(value = "accounts")
    public List<AccountResponse> getAccounts() {
        return accountRepository.findAll().stream()
                .map(acc -> {
                    AccountResponse resp = new AccountResponse();
                    resp.setAccountNumber(acc.getAccountNumber());
                    resp.setAccountType(acc.getAccountType());
                    resp.setBalance(acc.getBalance());
                    return resp;
                })
                .collect(Collectors.toList());
    }
}
