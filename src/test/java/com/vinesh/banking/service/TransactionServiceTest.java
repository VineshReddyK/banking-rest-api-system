package com.vinesh.banking.service;

import com.vinesh.banking.dto.TransactionRequest;
import com.vinesh.banking.entity.Account;
import com.vinesh.banking.entity.Transaction;
import com.vinesh.banking.entity.User;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.kafka.TransactionProducer;
import com.vinesh.banking.repository.AccountRepository;
import com.vinesh.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private TransactionProducer transactionProducer;
    @Mock private AuditLogService auditLogService;
    @Mock private EmailNotificationService emailNotificationService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User testUser() {
        User u = new User();
        u.setEmail("test@example.com");
        return u;
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("500.00"));
        account.setUser(testUser());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        TransactionRequest request = new TransactionRequest();
        request.setAccountId(1L);
        request.setAmount(new BigDecimal("200.00"));

        String result = transactionService.deposit(request);

        assertTrue(result.contains("Deposit successful"));
        assertEquals(new BigDecimal("700.00"), account.getBalance());
    }

    @Test
    void withdraw_insufficientFunds_shouldThrow() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("100.00"));
        account.setUser(testUser());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        TransactionRequest request = new TransactionRequest();
        request.setAccountId(1L);
        request.setAmount(new BigDecimal("500.00"));

        assertThrows(IllegalArgumentException.class, () -> transactionService.withdraw(request));
    }

    @Test
    void transfer_shouldMoveFundsBetweenAccounts() {
        Account source = new Account();
        source.setId(1L);
        source.setBalance(new BigDecimal("1000.00"));
        source.setUser(testUser());

        Account target = new Account();
        target.setId(2L);
        target.setBalance(new BigDecimal("200.00"));
        target.setUser(testUser());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(target));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        TransactionRequest request = new TransactionRequest();
        request.setAccountId(1L);
        request.setTargetAccountId(2L);
        request.setAmount(new BigDecimal("300.00"));

        String result = transactionService.transfer(request);

        assertTrue(result.contains("Transfer successful"));
        assertEquals(new BigDecimal("700.00"), source.getBalance());
        assertEquals(new BigDecimal("500.00"), target.getBalance());
    }
}
