package com.example.app.saving_goal.components;

import com.opencsv.CSVReader;  // 导入 opencsv 的 CSVReader 类
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CSVReaderUtility {

    // 清理 'Amount' 列的符号
    public static String cleanAmount(String amount) {
        return amount.replaceAll("[^0-9.]", "");  // 去除除数字和小数点以外的所有字符
    }

    // 读取并合并多个 CSV 文件
    public static List<String[]> readAndMergeCSVFiles(List<String> filePaths) throws IOException, CsvValidationException {
        List<String[]> mergedData = new ArrayList<>();

        // 处理每个文件
        for (String filePath : filePaths) {
            BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.ISO_8859_1);
            CSVReader csvReader = new CSVReader(reader);

            // 读取并添加头部
            String[] header = csvReader.readNext();
            mergedData.add(header);  // 将头部添加到合并数据中

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                // 清理 'Amount' 列（假设它是第四列，即索引为 3）
                line[3] = cleanAmount(line[3]);
                mergedData.add(line);
            }
        }

        return mergedData;
    }
}






