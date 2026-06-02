package com.vinesh.banking.service;

import com.vinesh.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
}
public String deposit() {
    return "Deposit Successful";
}

public String withdraw() {
    return "Withdrawal Successful";
}

public String transfer() {
    return "Transfer Successful";
}
