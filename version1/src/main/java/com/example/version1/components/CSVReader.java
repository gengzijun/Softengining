package com.example.version1.components;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    // 读取 CSV 文件并计算收入总额（收入 - 支出）
    public double readCSVAndCalculateSavedAmount(String filePath) throws IOException {
        double totalIncome = 0.0;
        double totalExpense = 0.0;

        // 读取 CSV 文件
        FileReader reader = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);

        // 遍历所有记录，筛选出收入和支出
        for (CSVRecord record : records) {
            String type = record.get("Type");
            double amount = Double.parseDouble(record.get("Amount (RMB)"));

            // 如果是收入，累加到 totalIncome
            if ("income".equals(type)) {
                totalIncome += amount;
            } else {
                // 如果是支出，累加到 totalExpense
                totalExpense += amount;
            }
        }

        // 计算已存金额（收入 - 支出）
        return totalIncome - totalExpense;
    }
}

