package com.example.history;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Month {
    public static final List<String> month_list = new ArrayList<>();

    // 添加一个新的月份字符串
    public static void addMonth(String newMonth) {
        month_list.add(newMonth);
    }

    // 获取全部月份
    public static String[] getMonthArray() {
        return month_list.toArray(new String[0]);
    }

    // 初始化：从 save_data 文件夹读取所有 csv 文件名作为月份
    static {
        File folder = new File("app/src/main/resources/data/save_data");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".csv")) {
                        String month = fileName.substring(0, fileName.length() - 4); // 去除.csv后缀
                        month_list.add(month);
                    }
                }
            }
        }
    }
}
