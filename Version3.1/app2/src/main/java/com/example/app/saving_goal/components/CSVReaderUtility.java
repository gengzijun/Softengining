package com.example.app.saving_goal.components;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CSVReaderUtility {
    // CSV格式类型枚举
    private enum CsvFormat {
        OLD_FORMAT(new String[]{"Transaction time", "Type", "Transaction object", "Amount (RMB)"}),
        NEW_FORMAT(new String[]{"Task", "Date", "Detail", "Amount", "Type"}),
        HISTORY_FORMAT(new String[]{"task", "date", "detail", "amount", "type"}),
        NO_HEADER_HISTORY_FORMAT(5); // 新增支持无表头格式

        private final Set<String> headers;
        private final int columnCount;

        CsvFormat(String[] headers) {
            this.headers = new HashSet<>(Arrays.asList(headers));
            this.columnCount = headers.length;
        }

        CsvFormat(int columnCount) {
            this.headers = Collections.emptySet();
            this.columnCount = columnCount;
        }

        public static CsvFormat detectFormat(String[] line) {
            if (line == null || line.length == 0) {
                throw new IllegalArgumentException("Empty CSV line");
            }

            Set<String> headerSet = new HashSet<>();
            for (String h : line) {
                headerSet.add(h.trim().replace("\uFEFF", ""));
            }

            for (CsvFormat format : values()) {
                if (!format.headers.isEmpty() && headerSet.containsAll(format.headers)) {
                    return format;
                }
            }

            // 如果匹配不上表头，尝试按列数判断
            if (line.length == 5) {
                System.out.println("⚠️ No header detected. Assuming NO_HEADER_HISTORY_FORMAT.");
                return NO_HEADER_HISTORY_FORMAT;
            }

            throw new IllegalArgumentException("Unknown CSV format: " + Arrays.toString(line));
        }
    }

    // 清理金额字段
    public static String cleanAmount(String amount) {
        return amount.replaceAll("[^0-9.]", "");
    }

    // 新增：日期格式转换方法
    private static String formatDateTime(String originalDateTime) {
        if (originalDateTime == null || originalDateTime.isEmpty()) {
            return "";
        }
        try {
            // 分割日期和时间部分
            String[] parts = originalDateTime.split(" ");
            String datePart = parts[0];
            String timePart = parts.length > 1 ? " " + parts[1] : "";

            // 分割年月日
            String[] dateComponents = datePart.split("-");
            if (dateComponents.length != 3) {
                return originalDateTime;
            }

            // 转换并去除前导零
            int year = Integer.parseInt(dateComponents[0]);
            int month = Integer.parseInt(dateComponents[1]);
            int day = Integer.parseInt(dateComponents[2]);

            // 重新组合为YYYY/M/D格式
            return String.format("%d/%d/%d%s", year, month, day, timePart);
        } catch (Exception e) {
            System.err.println("日期格式转换失败: " + originalDateTime);
            return originalDateTime;
        }
    }

    // 读取并合并多个CSV文件
    public static List<String[]> readAndMergeCSVFiles(List<String> filePaths) throws IOException, CsvValidationException {
        List<String[]> mergedData = new ArrayList<>();
        boolean headerAdded = false;

        for (String filePath : filePaths) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.ISO_8859_1);
                 CSVReader csvReader = new CSVReader(reader)) {

                // 读取并解析头部
                String[] rawHeader = csvReader.readNext();
                CsvFormat format = CsvFormat.detectFormat(rawHeader);

                // 添加统一格式的头部（只在第一次添加）
                if (!headerAdded) {
                    mergedData.add(new String[]{"Transaction time", "Type", "Transaction object", "Amount (RMB)"});
                    headerAdded = true;
                }

                // 处理数据行
                String[] rawLine;
                while ((rawLine = csvReader.readNext()) != null) {
                    String[] convertedLine = convertLine(format, rawLine);
                    if (convertedLine != null) {
                        mergedData.add(convertedLine);
                    }
                }
            }
        }
        return mergedData;
    }

    // 根据CSV格式转换数据行
    private static String[] convertLine(CsvFormat format, String[] rawLine) {
        try {
            switch (format) {
                case OLD_FORMAT:
                    return convertOldFormat(rawLine);
                case NEW_FORMAT:
                    return convertNewFormat(rawLine);
                case NO_HEADER_HISTORY_FORMAT:
                    return convertNoHeaderHistoryFormat(rawLine);
                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Error converting line: " + Arrays.toString(rawLine));
            return null;
        }
    }

    // 转换旧格式数据行（增加日期处理）
    private static String[] convertOldFormat(String[] line) {
        return new String[]{
                formatDateTime(line[0]),  // 处理后的日期时间
                line[1],
                line[2],
                cleanAmount(line[3])
        };
    }

    // 转换新格式数据行（增加日期处理）
    private static String[] convertNewFormat(String[] line) {
        return new String[]{
                formatDateTime(line[1]),  // 处理后的日期时间
                line[4],
                line[2],
                cleanAmount(line[3])
        };
    }

    private static String[] convertNoHeaderHistoryFormat(String[] line) {
        return new String[]{
                formatDateTime(line[1]), // date
                line[4],                 // type
                line[2],                 // detail
                cleanAmount(line[3])     // amount
        };
    }

}






