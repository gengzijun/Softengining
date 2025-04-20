package com.example.app.util;

import com.example.app.model.TransactionRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CsvParser {

    public static List<TransactionRecord> parseCSV(String filePath) {
        List<TransactionRecord> records = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;

                try {
                    LocalDateTime time = LocalDateTime.parse(parts[0].trim(), formatter);
                    String object = parts[1].trim();
                    String amountStr = parts[2].trim();
                    BigDecimal amount = new BigDecimal(amountStr);

                    TransactionRecord record = new TransactionRecord(time, object, amount);

                    // 新增逻辑：如果带 + 号，直接归类为 income
                    if (amountStr.startsWith("+")) {
                        record.setAiCategory("income");
                    }

                    records.add(record);
                } catch (Exception e) {
                    System.err.println("skip: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}
