package com.example.app.logic;

import com.example.app.model.PredictionResult;
import com.example.app.util.BudgetPromptBuilder;
import com.example.app.util.StatsAggregator;
import com.example.app.util.ResultSaver;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

public class BudgetAdvisor {

    private final OpenAiChatModel model;

    public BudgetAdvisor(OpenAiChatModel model) {
        this.model = model;
    }

    public void runPredictionAndAdvice(File folder, int months, BigDecimal targetSavings, String outputCsvPath) {
        try {
            Map<String, BigDecimal> stats = StatsAggregator.getStatsForPeriod(folder, months);

            // 构建 prompt
            String prompt = BudgetPromptBuilder.buildPredictionPrompt(stats, targetSavings);
            String aiResponse = model.chat(prompt);

            // 拆分 JSON 与建议
            int jsonStart = aiResponse.indexOf('{');
            int jsonEnd = aiResponse.lastIndexOf('}') + 1;

            String json = aiResponse.substring(jsonStart, jsonEnd);
            String suggestion = aiResponse.substring(jsonEnd).trim();

            // 解析
            ObjectMapper mapper = new ObjectMapper();
            PredictionResult result = mapper.readValue(json, PredictionResult.class);
            result.suggestion = suggestion;

            // 打印 & 保存
            System.out.println("==== AI JSON 预测 ====\n" + json);
            System.out.println("\n==== AI 建议 ====\n" + suggestion);

            ResultSaver.savePredictionResult(result, outputCsvPath);
            System.out.println("\n 结果已保存到 " + outputCsvPath);

        } catch (Exception e) {
            System.err.println("处理失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
