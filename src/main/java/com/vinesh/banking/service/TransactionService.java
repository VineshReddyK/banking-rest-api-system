package com.vinesh.banking.service;

import com.vinesh.banking.dto.TransactionRequest;
import com.vinesh.banking.entity.Account;
import com.vinesh.banking.entity.Transaction;
import com.vinesh.banking.exception.ResourceNotFoundException;
import com.vinesh.banking.kafka.TransactionEvent;
import com.vinesh.banking.kafka.TransactionProducer;
import com.vinesh.banking.repository.AccountRepository;
import com.vinesh.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionProducer transactionProducer;

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String deposit(TransactionRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        transactionProducer.publish(new TransactionEvent("DEPOSIT", account.getId(), request.getAmount(), account.getBalance()));

        return "Deposit Successful. New balance: " + account.getBalance();
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String withdraw(TransactionRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));

        if (account.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds. Current balance: " + account.getBalance());
        }

        account.setBalance(account.getBalance() - request.getAmount());
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        transactionProducer.publish(new TransactionEvent("WITHDRAWAL", account.getId(), request.getAmount(), account.getBalance()));

        return "Withdrawal Successful. New balance: " + account.getBalance();
    }

    @Transactional
    @CacheEvict(value = "accounts", allEntries = true)
    public String transfer(TransactionRequest request) {
        Account source = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + request.getAccountId()));
        Account target = accountRepository.findById(request.getTargetAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Target account not found with id: " + request.getTargetAccountId()));

        if (source.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds. Current balance: " + source.getBalance());
        }

        source.setBalance(source.getBalance() - request.getAmount());
        target.setBalance(target.getBalance() + request.getAmount());
        accountRepository.save(source);
        accountRepository.save(target);

        Transaction debit = new Transaction();
        debit.setAccount(source);
        debit.setAmount(request.getAmount());
        debit.setTransactionType("TRANSFER_DEBIT");
        debit.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(debit);

        Transaction credit = new Transaction();
        credit.setAccount(target);
        credit.setAmount(request.getAmount());
        credit.setTransactionType("TRANSFER_CREDIT");
        credit.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(credit);
        transactionProducer.publish(new TransactionEvent("TRANSFER", source.getId(), request.getAmount(), source.getBalance()));

        return "Transfer Successful. New balance: " + source.getBalance();
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
