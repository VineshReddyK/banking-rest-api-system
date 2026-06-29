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
    private final EmailNotificationService emailNotificationService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              TransactionProducer transactionProducer,
                              AuditLogService auditLogService,
                              EmailNotificationService emailNotificationService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionProducer = transactionProducer;
        this.auditLogService = auditLogService;
        this.emailNotificationService = emailNotificationService;
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String deposit(TransactionRequest request) {
        var account = findAccountOrThrow(request.getAccountId());

        double newBalance = account.getBalance() + request.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);

        recordTransaction(account, request.getAmount(), "DEPOSIT");
        transactionProducer.publish(new TransactionEvent("DEPOSIT", account.getId(), request.getAmount(), newBalance));

        auditLogService.log("DEPOSIT", "account:" + account.getId(),
                "Deposited $" + request.getAmount() + ", new balance: $" + newBalance);
        emailNotificationService.sendDepositAlert(
                account.getUser().getEmail(), account.getId(), request.getAmount(), newBalance);

        return "Deposit Successful. New balance: " + newBalance;
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String withdraw(TransactionRequest request) {
        var account = findAccountOrThrow(request.getAccountId());

        // TODO: might want to add an overdraft limit check here later
        if (account.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds. Available balance: " + account.getBalance());
        }

        double newBalance = account.getBalance() - request.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);

        recordTransaction(account, request.getAmount(), "WITHDRAWAL");
        transactionProducer.publish(new TransactionEvent("WITHDRAWAL", account.getId(), request.getAmount(), newBalance));
        auditLogService.log("WITHDRAWAL", "account:" + account.getId(),
                "Withdrew $" + request.getAmount() + ", new balance: $" + newBalance);
        emailNotificationService.sendWithdrawalAlert(
                account.getUser().getEmail(), account.getId(), request.getAmount(), newBalance);

        return "Withdrawal Successful. New balance: " + newBalance;
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String transfer(TransactionRequest request) {
        var source = findAccountOrThrow(request.getAccountId());
        var target = findAccountOrThrow(request.getTargetAccountId());

        if (source.getId().equals(target.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        if (source.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds. Available balance: " + source.getBalance());
        }

        double sourceNewBalance = source.getBalance() - request.getAmount();
        double targetNewBalance = target.getBalance() + request.getAmount();

        source.setBalance(sourceNewBalance);
        target.setBalance(targetNewBalance);

        // save both before recording transactions
        accountRepository.save(source);
        accountRepository.save(target);

        recordTransaction(source, request.getAmount(), "TRANSFER_DEBIT");
        recordTransaction(target, request.getAmount(), "TRANSFER_CREDIT");

        transactionProducer.publish(
                new TransactionEvent("TRANSFER", source.getId(), request.getAmount(), sourceNewBalance));
        auditLogService.log("TRANSFER", "account:" + source.getId(),
                "Transferred $" + request.getAmount() + " to account:" + target.getId()
                        + ", new balance: $" + sourceNewBalance);
        emailNotificationService.sendTransferAlert(
                source.getUser().getEmail(), source.getId(), target.getId(),
                request.getAmount(), sourceNewBalance);

        return "Transfer Successful. New balance: " + sourceNewBalance;
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }


    private Account findAccountOrThrow(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
    }

    private void recordTransaction(Account account, Double amount, String type) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setTransactionType(type);
        tx.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(tx);
    }
}
