package com.vinesh.banking.dto;

// returned from account creation and listing — keeps the user entity
// out of the API response so we never accidentally leak the password hash.
public class AccountResponse {

    private String accountNumber;
    private String accountType;
    private Double balance;

    public AccountResponse() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
