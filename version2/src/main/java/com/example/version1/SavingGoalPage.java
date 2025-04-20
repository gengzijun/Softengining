package com.example.version1;

import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

public class SavingGoalPage extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, CsvValidationException {

        // File paths for CSV files
        List<String> files = Arrays.asList(
                "src/main/resources/data/(20240101-20240229).csv",
                "src/main/resources/data/(20240601-20240901).csv",
                "src/main/resources/data/(20240301-20240601).csv",
                "src/main/resources/data/(20240901-20241130).csv",
                "src/main/resources/data/(20241201-20250228).csv"
        );

        // Reading and merging data from CSV files
        List<String[]> mergedData = CSVReaderUtility.readAndMergeCSVFiles(files);

        // Print merged data for debugging
        for (String[] line : mergedData) {
            System.out.println(String.join(",", line));
        }

        // Create the root layout (BorderPane)
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Create and configure navigation box
        NavBox navBox = new NavBox();
        HBox navBoxLayout = navBox.getNavBox();
        HBox titleWithIconBoxLayout = navBox.getTitleWithIconBox();
        VBox topBox = new VBox(10);
        topBox.getChildren().addAll(navBoxLayout, titleWithIconBoxLayout);
        root.setTop(topBox);

        // Create main content sections
        DetailsBox detailsBox = new DetailsBox(mergedData);
        ProgressBox progressBox = new ProgressBox(detailsBox);
        EmergencyBox emergencyBox = new EmergencyBox();
        SavingTipsBox savingTipsBox = new SavingTipsBox(getHostServices());

        // Add update listener to bind progress updates
        detailsBox.addUpdateListener(() -> {
            progressBox.updateProgress();
        });

        // Create the main content VBox with padding and margin
        VBox mainContent = new VBox(30);
        Insets marginInsets = new Insets(0, 10, 0, 10);
        mainContent.getChildren().addAll(progressBox, detailsBox, emergencyBox, savingTipsBox);

        // Apply margin to each component
        VBox.setMargin(progressBox, marginInsets);
        VBox.setMargin(detailsBox, marginInsets);
        VBox.setMargin(emergencyBox, marginInsets);
        VBox.setMargin(savingTipsBox, marginInsets);

        // Set the main content in the center of the BorderPane
        root.setCenter(mainContent);

        // Bind the width of the main content to the width of the scene to enable scaling
        DoubleProperty sceneWidth = new SimpleDoubleProperty();
        sceneWidth.bind(primaryStage.widthProperty());

        // Dynamically resize the components based on the scene width
        mainContent.prefWidthProperty().bind(sceneWidth.multiply(0.9));  // 90% of scene width
        progressBox.prefWidthProperty().bind(mainContent.widthProperty());
        detailsBox.prefWidthProperty().bind(mainContent.widthProperty());
        emergencyBox.prefWidthProperty().bind(mainContent.widthProperty());
        savingTipsBox.prefWidthProperty().bind(mainContent.widthProperty());

        // Set the scene with the root layout and show the stage
        Scene scene = new Scene(root, 1440, 810, Color.WHITE);
        primaryStage.setTitle("Saving Goal Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Launch the application
    }
}



