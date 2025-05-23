package com.example.app.history;

import java.util.ArrayList;
import java.util.List;

public class Month {
    // 可动态添加的月份列表
    public static final List<String> month_list = new ArrayList<>();

    // 添加一个新的月份字符串
    public static void addMonth(String newMonth) {
        month_list.add(newMonth);
    }

    // 获取全部月份
    public static String[] getMonthArray() {
        return month_list.toArray(new String[0]);
    }

    // 初始化部分数据(仅设计UI时方便展示)
    static {
        month_list.add("2024-12");
        month_list.add("2024-11");
        month_list.add("2024-10");
        month_list.add("2024-8");
    }
}
