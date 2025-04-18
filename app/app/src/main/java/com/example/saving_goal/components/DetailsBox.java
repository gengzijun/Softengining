package com.example.saving_goal.components;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class DetailsBox extends VBox {
    // Define style for ComboBox elements
    private static final String COMBO_BOX_STYLE =
            "-fx-background-color: white;" +
                    "-fx-border-color: #D3D3D3;" +
                    "-fx-border-width: 1px;" +
                    "-fx-background-radius: 5px;" +
                    "-fx-border-radius: 5px;" +
                    "-fx-padding: 2 5 2 5;" +
                    "-fx-pref-width: 1400;" +
                    "-fx-pref-height: 25;" +
                    "-fx-font-size: 12px;" +
                    "-fx-cell-size: 20px;";

    public DetailsBox() {
        initialize(); // Initialize the details box layout
    }

    private void initialize() {
        // Set the spacing, alignment, and padding for the container
        setSpacing(5);
        setAlignment(javafx.geometry.Pos.TOP_LEFT);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px;");

        // Create the title label and month sections
        Label titleLabel = createTitleLabel();

        // Create sections for each month (October, September, August)
        VBox month202410 = createMonthSection("2024-10", createTextArea());
        VBox month20249 = createMonthSection("2024-9", createComboBox());
        VBox month20248 = createMonthSection("2024-8", createComboBox());

        // Add all components to the layout
        getChildren().addAll(titleLabel, month202410, month20249, month20248);
    }

    // Create the title label for the details section
    private Label createTitleLabel() {
        Label label = new Label("Details");
        label.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        return label;
    }

    // Create a section for each month with content (either a TextArea or ComboBox)
    private VBox createMonthSection(String month, javafx.scene.Node content) {
        VBox container = new VBox(5);

        // Create the month label
        Label monthLabel = new Label(month);
        monthLabel.setFont(javafx.scene.text.Font.font("Arial", 14));
        setMargin(monthLabel, new Insets(0, 0, 0, 3));
        container.getChildren().addAll(monthLabel, content);

        // Apply margin to the whole section
        applySectionMargin(container);
        return container;
    }

    // Create a non-editable TextArea for displaying saved amounts and other information
    private TextArea createTextArea() {
        TextArea textArea = new TextArea("Saved: ¥14,032\nAdd up: ¥49,200");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(1200);
        textArea.setPrefHeight(60);
        textArea.setStyle(
                "-fx-background-radius: 10 10 10 10;" +
                        "-fx-border-radius: 10 10 10 10;" +
                        "-fx-border-color: #D3D3D3;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: white;"
        );

        // Apply margin to the TextArea
        applyElementMargin(textArea);
        return textArea;
    }

    // Create a ComboBox for more details options
    private ComboBox<String> createComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("More Details");
        comboBox.setValue("More Details");
        comboBox.setStyle(COMBO_BOX_STYLE);
        // Apply margin to the ComboBox
        applyElementMargin(comboBox);
        return comboBox;
    }

    // Helper method to apply margin to sections (VBox)
    private void applySectionMargin(VBox section) {
        VBox.setMargin(section, new Insets(0, 0, 0, 10));
    }

    // Helper method to apply margin to individual elements (TextArea, ComboBox)
    private void applyElementMargin(javafx.scene.Node node) {
        VBox.setMargin(node, new Insets(0, 0, 0, 10));
    }
}

