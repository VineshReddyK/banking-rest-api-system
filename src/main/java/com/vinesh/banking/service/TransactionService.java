package com.vinesh.banking.service;

import com.vinesh.banking.dto.TransactionRequest;
import com.vinesh.banking.entity.Account;
import com.vinesh.banking.entity.Transaction;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.kafka.TransactionEvent;
import com.vinesh.banking.kafka.TransactionProducer;
import com.vinesh.banking.repository.AccountRepository;
import com.vinesh.banking.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionProducer transactionProducer;
    private final AuditLogService auditLogService;
    private final EmailNotificationService emailService;

    public TransactionService(TransactionRepository transactionRepository,
                               AccountRepository accountRepository,
                               TransactionProducer transactionProducer,
                               AuditLogService auditLogService,
                               EmailNotificationService emailService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionProducer = transactionProducer;
        this.auditLogService = auditLogService;
        this.emailService = emailService;
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String deposit(TransactionRequest req) {
        Account account = findAccount(req.getAccountId());

        account.setBalance(account.getBalance().add(req.getAmount()));
        accountRepository.save(account);
        saveTransaction(account, req.getAmount(), "DEPOSIT");

        transactionProducer.publish(new TransactionEvent("DEPOSIT", account.getId(), req.getAmount(), account.getBalance()));
        auditLogService.log("DEPOSIT", "account:" + account.getId(),
                "Deposited $" + req.getAmount() + ", new balance: $" + account.getBalance());
        emailService.sendDepositAlert(account.getUser().getEmail(), account.getId(), req.getAmount(), account.getBalance());

        return "Deposit successful. New balance: " + account.getBalance();
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String withdraw(TransactionRequest req) {
        Account account = findAccount(req.getAccountId());

        if (account.getBalance().compareTo(req.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds. Current balance: " + account.getBalance());
        }

        account.setBalance(account.getBalance().subtract(req.getAmount()));
        accountRepository.save(account);
        saveTransaction(account, req.getAmount(), "WITHDRAWAL");

        transactionProducer.publish(new TransactionEvent("WITHDRAWAL", account.getId(), req.getAmount(), account.getBalance()));
        auditLogService.log("WITHDRAWAL", "account:" + account.getId(),
                "Withdrew $" + req.getAmount() + ", new balance: $" + account.getBalance());
        emailService.sendWithdrawalAlert(account.getUser().getEmail(), account.getId(), req.getAmount(), account.getBalance());

        return "Withdrawal successful. New balance: " + account.getBalance();
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String transfer(TransactionRequest req) {
        Account source = findAccount(req.getAccountId());
        Account target = findAccount(req.getTargetAccountId());

        if (source.getBalance().compareTo(req.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds. Current balance: " + source.getBalance());
        }

        source.setBalance(source.getBalance().subtract(req.getAmount()));
        target.setBalance(target.getBalance().add(req.getAmount()));
        accountRepository.save(source);
        accountRepository.save(target);

        saveTransaction(source, req.getAmount(), "TRANSFER_DEBIT");
        saveTransaction(target, req.getAmount(), "TRANSFER_CREDIT");

        transactionProducer.publish(new TransactionEvent("TRANSFER", source.getId(), req.getAmount(), source.getBalance()));
        auditLogService.log("TRANSFER", "account:" + source.getId(),
                "Transferred $" + req.getAmount() + " to account:" + target.getId() + ", new balance: $" + source.getBalance());
        emailService.sendTransferAlert(source.getUser().getEmail(), source.getId(), target.getId(), req.getAmount(), source.getBalance());

        return "Transfer successful. New balance: " + source.getBalance();
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    private void saveTransaction(Account account, java.math.BigDecimal amount, String type) {
        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(amount);
        t.setTransactionType(type);
        t.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(t);
    }
}
