package com.example.app.model;

import java.math.BigDecimal;

public class AiClassificationResult {
    private String object;
    private BigDecimal amount;
    private String category;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
