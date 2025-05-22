package com.example.app.util;

import com.example.app.model.PredictionResult;

import java.io.FileWriter;
import java.io.PrintWriter;

public class ResultSaver {

    public static void savePredictionResult(PredictionResult result, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Category,Amount");
            writer.println("living," + result.living);
            writer.println("entertainment," + result.entertainment);
            writer.println("catering," + result.catering);
            writer.println("shopping," + result.shopping);
            writer.println("predicted_total_expense," + result.predicted_total_expense);
            writer.println("predicted_income," + result.predicted_income);
            writer.println("suggestion,\"" + result.suggestion.replace("\"", "\"\"") + "\""); // CSV escape
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
