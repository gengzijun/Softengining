package com.example.version1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.version1.components.*;

public class SavingGoalPage extends Application {

    @Override
    public void start(Stage primaryStage) {
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
        ProgressBox progressBox = new ProgressBox();
        DetailsBox detailsBox = new DetailsBox();
        EmergencyBox emergencyBox = new EmergencyBox();
        SavingTipsBox savingTipsBox = new SavingTipsBox(getHostServices());

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
