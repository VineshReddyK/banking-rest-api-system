package com.vinesh.banking.kafka;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionEvent {

    private String type;
    private Long accountId;
    private BigDecimal amount;
    private BigDecimal newBalance;
    private LocalDateTime timestamp;

    public TransactionEvent() {}

    public TransactionEvent(String type, Long accountId, BigDecimal amount, BigDecimal newBalance) {
        this.type = type;
        this.accountId = accountId;
        this.amount = amount;
        this.newBalance = newBalance;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getNewBalance() { return newBalance; }
    public void setNewBalance(BigDecimal newBalance) { this.newBalance = newBalance; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return String.format("{type=%s, accountId=%d, amount=%s, newBalance=%s, timestamp=%s}",
                type, accountId, amount, newBalance, timestamp);
    }
}
