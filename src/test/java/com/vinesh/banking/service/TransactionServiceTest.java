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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionProducer transactionProducer;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User sampleUser() {
        User u = new User();
        u.setEmail("test@example.com");
        return u;
    }

    @Test
    void deposit_shouldIncreaseAccountBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(500.0);
        account.setUser(sampleUser());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        TransactionRequest request = new TransactionRequest();
        request.setAccountId(1L);
        request.setAmount(200.0);

        String result = transactionService.deposit(request);

        assertTrue(result.contains("Deposit Successful"));
        assertEquals(700.0, account.getBalance());
    }

    @Test
    void withdraw_insufficientFunds_shouldThrowIllegalArgumentException() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(100.0);
        account.setUser(sampleUser());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        TransactionRequest request = new TransactionRequest();
        request.setAccountId(1L);
        request.setAmount(500.0);

        assertThrows(IllegalArgumentException.class, () -> transactionService.withdraw(request));
    }

    @Test
    void transfer_shouldMoveFundsFromSourceToTarget() {
        Account source = new Account();
        source.setId(1L);
        source.setBalance(1000.0);
        source.setUser(sampleUser());

        Account target = new Account();
        target.setId(2L);
        target.setBalance(200.0);
        target.setUser(sampleUser());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(target));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any())).thenReturn(new Transaction());

        TransactionRequest request = new TransactionRequest();
        request.setAccountId(1L);
        request.setTargetAccountId(2L);
        request.setAmount(300.0);

        String result = transactionService.transfer(request);

        assertTrue(result.contains("Transfer Successful"));
        assertEquals(700.0, source.getBalance());
        assertEquals(500.0, target.getBalance());
    }

    @Test
    void deposit_accountNotFound_shouldThrowResourceNotFoundException() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        TransactionRequest request = new TransactionRequest();
        request.setAccountId(999L);
        request.setAmount(100.0);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.deposit(request));
    }
}
