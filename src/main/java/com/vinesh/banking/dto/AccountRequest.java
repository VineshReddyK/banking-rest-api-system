package com.vinesh.banking.dto;

import jakarta.validation.constraints.DecimalMin;

// inbound payload for creating a new bank account.
// accountType defaults to SAVINGS if omitted.
public class AccountRequest {

    private String accountType;

    @DecimalMin(value = "0.0", message = "Initial deposit cannot be negative")
    private Double initialDeposit;

    public AccountRequest() {
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(Double initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
}
