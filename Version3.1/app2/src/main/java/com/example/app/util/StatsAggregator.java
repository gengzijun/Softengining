package com.example.app.util;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.YearMonth;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsAggregator {

    private static final Pattern FILENAME_PATTERN = Pattern.compile("(\\d{4})-(\\d{2})\\.csv");

    public static Map<String, BigDecimal> getStatsForPeriod(File folder, int monthsBack) {
        Map<String, BigDecimal> aggregated = new HashMap<>();
        YearMonth now = YearMonth.now();

        try {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files == null) return aggregated;

            for (File file : files) {
                Matcher matcher = FILENAME_PATTERN.matcher(file.getName());
                if (!matcher.matches()) continue;

                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                YearMonth fileMonth = YearMonth.of(year, month);

                // 筛选在指定时间窗口内的文件
                if (!fileMonth.isBefore(now.minusMonths(monthsBack))) {
                    Map<String, BigDecimal> stats = MonthlyStatsLoader.loadMonthlyStats(file.getAbsolutePath());
                    mergeStats(aggregated, stats);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return aggregated;
    }

    private static void mergeStats(Map<String, BigDecimal> base, Map<String, BigDecimal> addition) {
        for (Map.Entry<String, BigDecimal> entry : addition.entrySet()) {
            base.merge(entry.getKey(), entry.getValue(), BigDecimal::add);
        }
    }
}
