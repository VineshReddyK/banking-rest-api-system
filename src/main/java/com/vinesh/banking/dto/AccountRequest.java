package com.vinesh.banking.dto;

import java.math.BigDecimal;

public class AccountRequest {

    private String accountType;
    private BigDecimal initialDeposit;

    public AccountRequest() {}

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getInitialDeposit() { return initialDeposit; }
    public void setInitialDeposit(BigDecimal initialDeposit) { this.initialDeposit = initialDeposit; }
}
