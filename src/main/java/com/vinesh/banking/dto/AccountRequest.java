package com.vinesh.banking.dto;

import jakarta.validation.constraints.DecimalMin;

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
