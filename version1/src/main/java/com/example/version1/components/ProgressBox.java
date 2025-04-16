package com.example.version1.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

public class ProgressBox extends VBox {

    public ProgressBox() {
        initialize(); // Initialize the ProgressBox layout
    }

    private void initialize() {
        // Basic style and layout setup
        setAlignment(Pos.CENTER);
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px;");
        setPadding(new Insets(10));
        setSpacing(5);

        // Create and add top info section (Target, Start Date, End Date)
        HBox topInfo = createTopInfo();

        // Create and add progress bar section
        HBox progressArea = createProgressBar();

        // Create and add bottom info section (Saved Amount, Remaining Amount)
        HBox bottomInfo = createBottomInfo();
        // Add all sections to the ProgressBox
        getChildren().addAll(topInfo, progressArea, bottomInfo);
    }

    private HBox createTopInfo() {
        // Target section
        HBox targetBox = new HBox(5);
        Label targetTitle = new Label("Target: ");
        targetTitle.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        Label targetValue = new Label("¥120,000");
        targetValue.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        targetBox.getChildren().addAll(targetTitle, targetValue);

        // Start Date section
        HBox startDateBox = new HBox(5);
        Label startDateTitle = new Label("Start from: ");
        startDateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label startDateValue = new Label("2024-7-3");
        startDateValue.setFont(Font.font("Arial Black", FontWeight.BOLD, 16));
        startDateBox.getChildren().addAll(startDateTitle, startDateValue);

        // End Date section
        HBox endDateBox = new HBox(5);
        Label endDateTitle = new Label("End by: ");
        endDateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label endDateValue = new Label("2025-12-31");
        endDateValue.setFont(Font.font("Arial Black", FontWeight.BOLD, 16));
        endDateBox.getChildren().addAll(endDateTitle, endDateValue);

        // Layout container with spacing between the sections
        HBox topInfo = new HBox(40);
        topInfo.setAlignment(Pos.CENTER);
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        // Add the sections and spacers into the top info layout
        topInfo.getChildren().addAll(
                targetBox, spacer1,
                startDateBox, spacer2,
                endDateBox
        );

        return topInfo;
    }

    private HBox createProgressBar() {
        // Create and configure the progress bar
        ProgressBar progressBar = new ProgressBar(0.41473);
        progressBar.setPrefWidth(1100);
        progressBar.setPrefHeight(50);
        progressBar.setStyle(
                "-fx-background-radius: 25px 0 0 25px;"
                        + "-fx-border-radius: 25px;"
                        + "-fx-accent: #B6B0ED;"
                        + "-fx-background-color: #E0E0E0;"
                        + "-fx-border-color: transparent;"
                        + "-fx-padding: 0;"
                        + "-fx-background-insets: 0;"
        );

        // Dynamically adjust the corner radius based on progress
        progressBar.progressProperty().addListener((obs, oldVal, newVal) -> {
            String style = progressBar.getStyle();
            if (newVal.doubleValue() >= 1.0) {
                progressBar.setStyle(style.replace("25px 0 0 25px", "25px"));
            } else {
                progressBar.setStyle(style.replace("25px", "25px 0 0 25px"));
            }
        });

        // Create and configure the percentage label
        Label progressPercentage = new Label("41.473%");
        progressPercentage.setTextFill(Color.web("#CCA4EB"));
        progressPercentage.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Stack the progress bar and percentage label together
        StackPane progressStack = new StackPane();
        progressStack.getChildren().addAll(progressBar, progressPercentage);
        progressStack.setClip(null);

        // Create container for the progress bar and label
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(progressStack);
        return container;
    }

    private HBox createBottomInfo() {
        // Saved amount section
        HBox savedBox = new HBox(10);
        Label savedLabel = new Label("Have saved: ");
        savedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label savedAmount = new Label("¥49,200");
        savedAmount.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        savedAmount.setTextFill(Color.web("#82DD6E"));
        savedBox.getChildren().addAll(savedLabel, savedAmount);

        // Remaining amount section
        HBox remainingBox = new HBox(10);
        Label remainingLabel = new Label("Still need: ");
        remainingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label remainingAmount = new Label("¥70,800");
        remainingAmount.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        remainingAmount.setTextFill(Color.web("#C24943"));
        remainingBox.getChildren().addAll(remainingLabel, remainingAmount);

        // Layout container for bottom information
        HBox bottomInfo = new HBox(1000);
        bottomInfo.setAlignment(Pos.CENTER);
        bottomInfo.getChildren().addAll(savedBox, remainingBox);
        return bottomInfo;
    }
}



