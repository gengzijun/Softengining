package com.example.app.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BudgetPromptBuilder {

    public static String buildPredictionPrompt(Map<String, BigDecimal> stats, BigDecimal targetSavings) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a personal financial assistant.\n");
        prompt.append("Based on the following past monthly spending data, please:\n");
        prompt.append("1. Predict the budget for the following categories: living, entertainment, catering, shopping.\n");
        prompt.append("2. Predict the total expense and income for next month.\n");
        prompt.append("3. Return your response in JSON format like this:\n\n");

        prompt.append("{\n");
        prompt.append("  \"living\": 100.00,\n");
        prompt.append("  \"entertainment\": 150.00,\n");
        prompt.append("  \"catering\": 200.00,\n");
        prompt.append("  \"shopping\": 250.00,\n");
        prompt.append("  \"predicted_total_expense\": 700.00,\n");
        prompt.append("  \"predicted_income\": 2000.00\n");
        prompt.append("}\n\n");

        prompt.append("Then, provide a brief financial advice in English text format after the JSON output.\n");
        prompt.append("The goal is to save at least ").append(targetSavings).append(" RMB next month.\n");
        prompt.append("Be specific and actionable, such as \"reduce takeaway to twice a week\" or \"limit shopping to essentials only\".\n");
        prompt.append("Do not include any extra commentary. JSON first, then advice.");

        prompt.append("\n\nHere is my latest monthly spending summary:\n{\n");
        for (Map.Entry<String, BigDecimal> entry : stats.entrySet()) {
            prompt.append("  \"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(",\n");
        }
        if (!stats.isEmpty()) prompt.deleteCharAt(prompt.length() - 2); // remove last comma
        prompt.append("}");

        return prompt.toString();
    }



//    public static String buildAdvicePrompt(Map<String, BigDecimal> actualSpending,
//                                           List<Map<String, Object>> aiBudgets,
//                                           BigDecimal targetSavings) {
//
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("你是一个个人理财助手。以下是我的交易数据分析及新的预算计划。\n\n");
//        prompt.append("我设定了一个目标：在下一个阶段存下 ").append(targetSavings).append(" 元。\n\n");
//
//        prompt.append("这是我最近的支出情况（单位 RMB）：\n");
//        for (Map.Entry<String, BigDecimal> entry : actualSpending.entrySet()) {
//            String key = entry.getKey();
//            if (!key.equalsIgnoreCase("Total Income") && !key.equalsIgnoreCase("Total Expenses")) {
//                prompt.append("- ").append(key).append(": ").append(entry.getValue()).append("\n");
//            }
//        }
//
//        prompt.append("\n以下是你给出的预算建议（单位 RMB）：\n");
//        for (Map<String, Object> item : aiBudgets) {
//            prompt.append("- ").append(item.get("category")).append(": ").append(item.get("budget")).append("\n");
//        }
//
//        prompt.append("\n请根据预算的变化，分析我当前的消费习惯，指出哪些支出过高、哪些可以减少，并告诉我如何在不牺牲基本生活质量的前提下更合理地控制支出。\n");
//        prompt.append("请用自然语言输出建议，并尽量具体，比如：“你可以每周只点两次外卖”、“购物可以限制在必要生活用品”等。\n");
//
//        return prompt.toString();
//    }
}
