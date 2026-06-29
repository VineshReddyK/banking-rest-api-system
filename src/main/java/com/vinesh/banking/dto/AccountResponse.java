package com.vinesh.banking.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private String accountNumber;
    private String accountType;
    private BigDecimal balance;

    public AccountResponse() {}

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
