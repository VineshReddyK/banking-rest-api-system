package com.vinesh.banking.service;

import com.vinesh.banking.dto.AccountRequest;
import com.vinesh.banking.dto.AccountResponse;
import com.vinesh.banking.entity.Account;
import com.vinesh.banking.entity.User;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.repository.AccountRepository;
import com.vinesh.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public AccountResponse createAccount(Long userId, AccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        account.setAccountType(request.getAccountType() != null ? request.getAccountType() : "SAVINGS");
        account.setBalance(request.getInitialDeposit() != null ? request.getInitialDeposit() : 0.0);
        account.setUser(user);

        Account saved = accountRepository.save(account);

        AccountResponse response = new AccountResponse();
        response.setAccountNumber(saved.getAccountNumber());
        response.setAccountType(saved.getAccountType());
        response.setBalance(saved.getBalance());
        return response;
    }

    public List<AccountResponse> getAccounts() {
        return accountRepository.findAll().stream().map(account -> {
            AccountResponse response = new AccountResponse();
            response.setAccountNumber(account.getAccountNumber());
            response.setBalance(account.getBalance());
            return response;
            response.setAccountType(account.getAccountType());
            return response;
        }).collect(Collectors.toList());
    }
}
