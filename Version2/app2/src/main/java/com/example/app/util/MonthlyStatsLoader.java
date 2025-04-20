package com.example.app.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MonthlyStatsLoader {

    public static Map<String, BigDecimal> loadMonthlyStats(String filePath) {
        Map<String, BigDecimal> stats = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 2) continue;

                String category = parts[0].trim();
                BigDecimal amount = new BigDecimal(parts[1].trim());

                stats.put(category, amount);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }
}
