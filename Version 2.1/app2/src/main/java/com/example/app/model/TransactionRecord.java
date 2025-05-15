package com.example.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionRecord {
    private LocalDateTime transactionTime;
    private String transactionObject;
    private BigDecimal amount;
    private String aiCategory;

    public TransactionRecord(LocalDateTime transactionTime, String transactionObject, BigDecimal amount) {
        this.transactionTime = transactionTime;
        this.transactionObject = transactionObject;
        this.amount = amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public String getTransactionObject() {
        return transactionObject;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAiCategory() {
        return aiCategory;
    }

    public void setAiCategory(String aiCategory) {
        this.aiCategory = aiCategory;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "transactionTime=" + transactionTime +
                ", transactionObject='" + transactionObject + '\'' +
                ", amount=" + amount +
                ", aiCategory='" + aiCategory + '\'' +
                '}';
    }
}
