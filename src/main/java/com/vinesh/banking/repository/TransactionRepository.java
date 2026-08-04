package com.vinesh.banking.repository;

import com.vinesh.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// two query methods: one for raw history, one sorted newest-first for the API
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);
}
