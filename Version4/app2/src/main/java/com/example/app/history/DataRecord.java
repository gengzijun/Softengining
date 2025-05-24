package com.example.app.history;

import javafx.beans.property.SimpleStringProperty;

public class DataRecord {
    private final SimpleStringProperty task;
    private final SimpleStringProperty detail;
    private final SimpleStringProperty type;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty date;

    public DataRecord(String task, String date, String detail, String amount, String type) {
        this.task = new SimpleStringProperty(task);
        this.detail = new SimpleStringProperty(detail);
        this.type = new SimpleStringProperty(type);
        this.amount = new SimpleStringProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    public String getTask() {
        return task.get();
    }

    public String getDetail() {
        return detail.get();
    }

    public String getType() {
        return type.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty taskProperty() {
        return task;
    }

    public SimpleStringProperty detailProperty() {
        return detail;
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    @Override
    public String toString() {
        return "DataRecord{" +
                "task='" + task.get() + '\'' +
                ", detail='" + detail.get() + '\'' +
                ", type='" + type.get() + '\'' +
                ", amount='" + amount.get() + '\'' +
                ", date='" + date.get() + '\'' +
                '}';
    }

    public String getTaskValue() {
        return task.get();
    }

    public String getDetailValue() {
        return detail.get();
    }

    public String getTypeValue() {
        return type.get();
    }

    public String getAmountValue() {
        return amount.get();
    }

    public String getDateValue() {
        return date.get();
    }
}