package com.vinesh.banking.service;

import com.vinesh.banking.dto.AccountRequest;
import com.vinesh.banking.dto.AccountResponse;
import com.vinesh.banking.entity.Account;
import com.vinesh.banking.entity.User;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.repository.AccountRepository;
import com.vinesh.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_shouldReturnAccountResponse() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        Account saved = new Account();
        saved.setAccountNumber("ABC123");
        saved.setAccountType("SAVINGS");
        saved.setBalance(500.0);
        saved.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        AccountRequest request = new AccountRequest();
        request.setInitialDeposit(500.0);

        AccountResponse response = accountService.createAccount(1L, request);

        assertEquals("ABC123", response.getAccountNumber());
        assertEquals(500.0, response.getBalance());
    }

    @Test
    void createAccount_userNotFound_shouldThrow() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        AccountRequest request = new AccountRequest();
        request.setInitialDeposit(100.0);

        assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(99L, request));
    }

    @Test
    void getAccounts_shouldReturnList() {
        Account account = new Account();
        account.setAccountNumber("XYZ789");
        account.setBalance(1000.0);

        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountResponse> result = accountService.getAccounts();

        assertEquals(1, result.size());
        assertEquals("XYZ789", result.get(0).getAccountNumber());
    }
}
