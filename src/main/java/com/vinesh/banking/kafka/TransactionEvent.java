package com.vinesh.banking.kafka;

import java.time.LocalDateTime;

// Kafka message payload — serialized to JSON by the producer and
// deserialized by the consumer. the "type" field determines how
// downstream processors handle each event.
public class TransactionEvent {

    private String type;
    private Long accountId;
    private Double amount;
    private Double newBalance;
    private LocalDateTime timestamp;

    public TransactionEvent() {
    }

    public TransactionEvent(String type, Long accountId, Double amount, Double newBalance) {
        this.type = type;
        this.accountId = accountId;
        this.amount = amount;
        this.newBalance = newBalance;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("{type=%s, accountId=%d, amount=%.2f, newBalance=%.2f, timestamp=%s}",
                type, accountId, amount, newBalance, timestamp);
    }
}
