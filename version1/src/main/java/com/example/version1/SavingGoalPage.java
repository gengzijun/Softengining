package com.example.version1;

import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.version1.components.*;

import java.io.IOException;
import java.util.*;
import java.util.List;


public class SavingGoalPage extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, CsvValidationException {

        List<String> files = Arrays.asList(
                "src/main/resources/data/(20240101-20240229).csv",
                "src/main/resources/data/(20240601-20240901).csv",
                "src/main/resources/data/(20240301-20240601).csv",
                "src/main/resources/data/(20240901-20241130).csv",
                "src/main/resources/data/(20241201-20250228).csv"
        );

        // 调用 CSVReaderUtility 类的读取和合并文件方法
        List<String[]> mergedData = CSVReaderUtility.readAndMergeCSVFiles(files);

        // 打印合并后的内容
        for (String[] line : mergedData) {
            System.out.println(String.join(",", line));
        }




        // Create root layout
        BorderPane root = new BorderPane();

        root.setStyle("-fx-background-color: white;");
        // Create and configure navigation box
        NavBox navBox = new NavBox();

        // Get navigation box and title with icon layout from NavBox component
        HBox navBoxLayout = navBox.getNavBox();
        HBox titleWithIconBoxLayout = navBox.getTitleWithIconBox();
        VBox topBox = new VBox(10);  // Set the top layout for the root
        topBox.getChildren().addAll(navBoxLayout, titleWithIconBoxLayout);
        root.setTop(topBox);

        // Create main content sections
        DetailsBox detailsBox = new DetailsBox(mergedData);
        ProgressBox progressBox = new ProgressBox(detailsBox);
        EmergencyBox emergencyBox = new EmergencyBox();
        SavingTipsBox savingTipsBox = new SavingTipsBox(getHostServices());

        // 添加双向绑定
        detailsBox.addUpdateListener(() -> {
            progressBox.updateProgress();
        });

        // Combine all sections into the main content area
        VBox mainContent = new VBox(30);
        // Set consistent margin for all sections
        Insets marginInsets = new Insets(0, 10, 0, 10);
        mainContent.getChildren().addAll(progressBox, detailsBox, emergencyBox, savingTipsBox);
        // Apply margin to each component
        VBox.setMargin(progressBox, marginInsets);
        VBox.setMargin(detailsBox, marginInsets);
        VBox.setMargin(emergencyBox, marginInsets);
        VBox.setMargin(savingTipsBox, marginInsets);
        // Set the main content as the center of the root layout
        root.setCenter(mainContent);

        // Set the scene and show the stage
        Scene scene = new Scene(root, 1440, 810, Color.WHITE);
        primaryStage.setTitle("Saving Goal Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);// Launch the application
    }
}

