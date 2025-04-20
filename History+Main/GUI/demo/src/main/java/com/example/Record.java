// src/main/java/com/example/Record.java
package com.example;

import javafx.beans.property.SimpleStringProperty;

public class Record {
    private final SimpleStringProperty task;
    private final SimpleStringProperty detail;
    private final SimpleStringProperty type;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty date;

    public Record(String task, String detail, String type, String amount, String date) {
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
}
