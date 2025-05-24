//7c33c13c-2d6f-4fdd-be8b-d7c8b3df92d9
package com.example.app.util;
import com.fasterxml.jackson.core.JsonParseException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.opencsv.exceptions.CsvValidationException;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;




public class BudgetPrediction {

    public static void runBudgetPrediction(String spendingCsv, String savingsCsv, String outputCsv) throws Exception {
        // 读取上个时期的实际花费数据
        Map<String, Double> previousSpendingData = readPreviousSpendingCSV(spendingCsv);

        // 读取存款目标数据
        Map<String, Double> savingsData = readSavingsCSV(savingsCsv);

        // 生成AI模型的Prompt
        String monthlyPrompt = generateMonthlyBudgetPrompt(previousSpendingData, savingsData);

        // 获取AI模型的预测结果
        String prediction = getPrediction(monthlyPrompt);
        System.out.println(prediction);

        // 解析AI返回的JSON预测结果
        Map<String, Object> predictionData = parsePredictionResponse(prediction);

        // 将预测结果保存为CSV文件
        writePredictionToCSV(predictionData, outputCsv);

        System.out.println("预算预测已保存为CSV文件。");
    }

    // 读取上个时期的花费CSV
    public static Map<String, Double> readPreviousSpendingCSV(String filePath) throws IOException {
        Map<String, Double> previousSpendingData = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] nextRecord;
            csvReader.readNext(); // 跳过标题行
            while ((nextRecord = csvReader.readNext()) != null) {
                String category = nextRecord[0];
                double amount = Double.parseDouble(nextRecord[1]);
                previousSpendingData.put(category, amount);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return previousSpendingData;
    }

    // 读取存款目标CSV
    public static Map<String, Double> readSavingsCSV(String filePath) throws IOException {
        Map<String, Double> savingsData = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] nextRecord;
            csvReader.readNext(); // 跳过标题行
            while ((nextRecord = csvReader.readNext()) != null) {
                double target = Double.parseDouble(nextRecord[0]);
                double haveSaved = Double.parseDouble(nextRecord[1]);
                double stillNeed = Double.parseDouble(nextRecord[2]);
                savingsData.put("target", target);
                savingsData.put("haveSaved", haveSaved);
                savingsData.put("stillNeed", stillNeed);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return savingsData;
    }

    // 生成月度预算的Prompt
    public static String generateMonthlyBudgetPrompt(Map<String, Double> previousSpendingData, Map<String, Double> savingsData) {
        double totalIncome = previousSpendingData.getOrDefault("Total Income", 0.0);
        double totalExpenses = previousSpendingData.getOrDefault("Total Expenses", 0.0);
        double totalSavingsNeed = savingsData.getOrDefault("stillNeed", 0.0);

        double previousLiving = previousSpendingData.getOrDefault("living", 0.0);
        double previousDining = previousSpendingData.getOrDefault("dining", 0.0);
        double previousShopping = previousSpendingData.getOrDefault("shopping", 0.0);
        double previousEntertainment = previousSpendingData.getOrDefault("entertainment", 0.0);

        return String.format(
                "You are a financial advisor assistant.\n\n" +
                        "Based on the following user's financial data, create a monthly budget recommendation:\n\n" +
                        "Total Income: %.2f\n" +
                        "Total Expenses: %.2f\n" +
                        "Savings Goal Remaining: %.2f\n" +
                        "Previous Spending:\n" +
                        "  - Living: %.2f\n" +
                        "  - Dining: %.2f\n" +
                        "  - Shopping: %.2f\n" +
                        "  - Entertainment: %.2f\n\n" +
                        "Respond strictly in this exact JSON format:\n" +
                        "{\n" +
                        "  \"summary\": \"string\",\n" +
                        "  \"current_financial_status\": {\n" +
                        "    \"monthly_income\": number,\n" +
                        "    \"monthly_expenses\": number,\n" +
                        "    \"monthly_savings_potential\": number,\n" +
                        "    \"savings_goal\": number,\n" +
                        "    \"months_to_goal\": number\n" +
                        "  },\n" +
                        "  \"budget_allocation_suggestions\": [\n" +
                        "    {\n" +
                        "      \"category\": \"string\",\n" +
                        "      \"previous_spending\": number,\n" +
                        "      \"suggested_budget\": number,\n" +
                        "      \"rationale\": \"string\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"total_suggested_expenses\": number,\n" +
                        "  \"remaining_for_savings\": number,\n" +
                        "  \"additional_recommendations\": [\"string\", \"string\"],\n" +
                        "  \"response_format\": \"json\"\n" +
                        "}\n\n" +
                        "Do not include markdown or extra explanation. Only output valid JSON that matches the above format.",
                totalIncome, totalExpenses, totalSavingsNeed,
                previousLiving, previousDining, previousShopping, previousEntertainment
        );
    }

    public static String generateQuarterlyBudgetPrompt(Map<String, Double> previousSpendingData, Map<String, Double> savingsData) {
        double totalIncome = previousSpendingData.getOrDefault("Total Income", 0.0);
        double totalExpenses = previousSpendingData.getOrDefault("Total Expenses", 0.0);
        double totalSavingsNeed = savingsData.getOrDefault("stillNeed", 0.0);

        double previousLiving = previousSpendingData.getOrDefault("living", 0.0);
        double previousDining = previousSpendingData.getOrDefault("entertainment", 0.0);
        double previousShopping = previousSpendingData.getOrDefault("catering", 0.0);
        double previousEntertainment = previousSpendingData.getOrDefault("shopping", 0.0);

        return String.format(
                "{\n" +
                        "  \"total_income\": %.2f,\n" +
                        "  \"total_expenses\": %.2f,\n" +
                        "  \"savings_goal\": %.2f,\n" +
                        "  \"previous_spending\": {\n" +
                        "    \"living\": %.2f,\n" +
                        "    \"dining\": %.2f,\n" +
                        "    \"shopping\": %.2f,\n" +
                        "    \"entertainment\": %.2f\n" +
                        "  },\n" +
                        "  \"categories\": [\n" +
                        "    {\"name\": \"Living\", \"budget\": 0},\n" +
                        "    {\"name\": \"Dining\", \"budget\": 0},\n" +
                        "    {\"name\": \"Shopping\", \"budget\": 0},\n" +
                        "    {\"name\": \"Entertainment\", \"budget\": 0},\n" +
                        "    {\"name\": \"Savings\", \"budget\": 0}\n" +
                        "  ],\n" +
                        "  \"total_suggestion\": \"Provide a summary of how to allocate funds effectively to reach savings goals while balancing lifestyle needs.\",\n" +
                        "  \"response_format\": \"json\"\n" +
                        "}\n",
                totalIncome, totalExpenses, totalSavingsNeed,
                previousLiving, previousDining, previousShopping, previousEntertainment
        );
    }
    public static String generateYearlyBudgetPrompt(Map<String, Double> previousSpendingData, Map<String, Double> savingsData) {
        double totalIncome = previousSpendingData.getOrDefault("Total Income", 0.0);
        double totalExpenses = previousSpendingData.getOrDefault("Total Expenses", 0.0);
        double totalSavingsNeed = savingsData.getOrDefault("stillNeed", 0.0);

        double previousLiving = previousSpendingData.getOrDefault("living", 0.0);
        double previousDining = previousSpendingData.getOrDefault("entertainment", 0.0);
        double previousShopping = previousSpendingData.getOrDefault("catering", 0.0);
        double previousEntertainment = previousSpendingData.getOrDefault("shopping", 0.0);

        return String.format(
                "{\n" +
                        "  \"total_income\": %.2f,\n" +
                        "  \"total_expenses\": %.2f,\n" +
                        "  \"savings_goal\": %.2f,\n" +
                        "  \"previous_spending\": {\n" +
                        "    \"living\": %.2f,\n" +
                        "    \"dining\": %.2f,\n" +
                        "    \"shopping\": %.2f,\n" +
                        "    \"entertainment\": %.2f\n" +
                        "  },\n" +
                        "  \"categories\": [\n" +
                        "    {\"name\": \"Living\", \"budget\": 0},\n" +
                        "    {\"name\": \"Dining\", \"budget\": 0},\n" +
                        "    {\"name\": \"Shopping\", \"budget\": 0},\n" +
                        "    {\"name\": \"Entertainment\", \"budget\": 0},\n" +
                        "    {\"name\": \"Savings\", \"budget\": 0}\n" +
                        "  ],\n" +
                        "  \"total_suggestion\": \"Provide a summary of how to allocate funds effectively to reach savings goals while balancing lifestyle needs.\",\n" +
                        "  \"response_format\": \"json\"\n" +
                        "}\n",
                totalIncome, totalExpenses, totalSavingsNeed,
                previousLiving, previousDining, previousShopping, previousEntertainment
        );
    }

    // 调用AI模型获取预测结果
    public static String getPrediction(String prompt) {
        OpenAiChatModel model = OpenAiChatModel
                .builder()
                .baseUrl("https://ark.cn-beijing.volces.com/api/v3/")
                .apiKey("7c33c13c-2d6f-4fdd-be8b-d7c8b3df92d9")  // 使用环境变量存储API密钥
                .modelName("deepseek-v3-250324")
                .temperature(0.01)
                .timeout(Duration.ofSeconds(60))
                .build();
        return model.chat(prompt);  // 调用LangChain API获取响应
    }

    // 解析AI返回的JSON数据
    public static Map<String, Object> parsePredictionResponse(String jsonResponse) throws IOException {
        // 清理 JSON 响应
        jsonResponse = jsonResponse.replaceAll("`", "")
                .replaceAll("^\\s*```json", "")
                .replaceAll("```\\s*$", "")
                .replaceAll("(?i)^json", "")
                .trim();

        Map<String, Object> predictions = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // 解析 summary
            predictions.put("summary", rootNode.path("summary").asText());

            // 解析 current_financial_status 字段
            JsonNode statusNode = rootNode.path("current_financial_status");
            predictions.put("monthly_income", statusNode.path("monthly_income").asDouble());
            predictions.put("monthly_expenses", statusNode.path("monthly_expenses").asDouble());
            predictions.put("savings_goal", statusNode.path("savings_goal").asDouble());
            predictions.put("time_to_goal_months", statusNode.path("months_to_goal").asDouble());

            // 解析预算分配建议
            JsonNode allocationArray = rootNode.path("budget_allocation_suggestions");
            if (allocationArray.isArray()) {
                StringBuilder allSuggestions = new StringBuilder();
                for (JsonNode item : allocationArray) {
                    String category = item.path("category").asText();
                    double suggestedBudget = item.path("suggested_budget").asDouble();
                    String rationale = item.path("rationale").asText();

                    predictions.put(category + "_suggested_budget", suggestedBudget);
                    predictions.put(category + "_rationale", rationale);
                    allSuggestions.append(category).append(": ").append(rationale).append("\n");
                }
                predictions.put("combined_suggestions", allSuggestions.toString().trim());
            }

            // 解析额外字段（如果存在）
            predictions.put("total_suggested_expenses", rootNode.path("total_suggested_expenses").asDouble(0.0));
            predictions.put("total_suggested_savings", rootNode.path("remaining_for_savings").asDouble(0.0));

            // 解析 notes 或 tips
            JsonNode notesNode = rootNode.path("additional_recommendations");
            StringBuilder allNotes = new StringBuilder();
            if (notesNode.isArray()) {
                for (JsonNode note : notesNode) {
                    allNotes.append(note.asText()).append("\n");
                }
            }
            predictions.put("notes", allNotes.toString().trim());

        } catch (JsonParseException e) {
            System.out.println("Error parsing JSON response: " + e.getMessage());
            throw e;
        }

        return predictions;
    }



    // 保存AI预测结果到CSV文件
    public static void writePredictionToCSV(Map<String, Object> predictionData, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header row
            writer.writeNext(new String[] {
                    "Category", "Amount", "Suggestion"
            });

            // Categories to iterate over for the CSV output
            String[] categories = {"Living", "Dining", "Shopping", "Entertainment", "Savings"};
            for (String category : categories) {
                String budgetKey = category + "_suggested_budget";
                String percentageKey = category + "_percentage_change";
                String rationaleKey = category + "_rationale";
                String percentageIncomeKey = category + "_percentage_of_income";

                // Write the category's budget, percentage change, and rationale to CSV
                String amount = String.valueOf(predictionData.getOrDefault(budgetKey, "null"));
                String suggestion = (String) predictionData.getOrDefault(rationaleKey, "No suggestion available.");
                writer.writeNext(new String[] {
                        category, amount, suggestion
                });
            }

            // Write the combined suggestion (all categories) as a single entry
            writer.writeNext(new String[] {
                    "Total Suggestion", "", (String) predictionData.get("combined_suggestions")
            });

            // Write summary information (Total Income, Total Expenses, etc.)
            writer.writeNext(new String[] {
                    "Total Income", String.valueOf(predictionData.get("monthly_income")), ""
            });

            writer.writeNext(new String[] {
                    "Total Expenses", String.valueOf(predictionData.get("monthly_expenses")), ""
            });

            writer.writeNext(new String[] {
                    "Savings Goal", String.valueOf(predictionData.get("savings_goal")), ""
            });

            writer.writeNext(new String[] {
                    "Time to Goal (Months)", String.valueOf(predictionData.get("time_to_goal_months")), ""
            });

            writer.writeNext(new String[] {
                    "Total Suggested Expenses", String.valueOf(predictionData.get("total_suggested_expenses")), ""
            });

            writer.writeNext(new String[] {
                    "Total Suggested Savings", String.valueOf(predictionData.get("total_suggested_savings")), ""
            });

            // Write notes
            writer.writeNext(new String[] {
                    "Notes", (String) predictionData.get("notes"), ""
            });
        }
    }


}
