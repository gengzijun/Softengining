package com.example.app.saving_goal.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EmergencyBox extends VBox {

    private TextField inputField; // TextField for user input

    public EmergencyBox() {
        initialize(); // Initialize the layout of the EmergencyBox
    }

    private void initialize() {
        // Basic container setup
        setSpacing(10);
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px;");

        // Create the main container and add it to the EmergencyBox
        HBox mainContainer = createMainContainer();
        getChildren().add(mainContainer);
    }

    private HBox createMainContainer() {
        // Create the left section with icon and title
        HBox iconTitleSection = createIconTitleSection();

        // Create the right section with description and input field
        VBox inputSection = createInputSection();

        // Combine the left and right sections into a horizontal box
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().addAll(iconTitleSection, inputSection);

        return container;
    }

    private HBox createIconTitleSection() {
        // Load the icon and title for the section
        try {
            Image icon = new Image(new FileInputStream("src/main/resources/images/emoji1.png"));
            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(20);  // 替代原来的setFitSize(20,20)
            iconView.setFitHeight(20);

            Label title = new Label("Any Emergency");
            title.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 17));

            HBox section = new HBox(10);
            section.setAlignment(Pos.CENTER_LEFT);
            section.getChildren().addAll(iconView, title);
            return section;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Emergency icon not found", e);
        }
    }

    private VBox createInputSection() {
        // Create description text for the input section
        Label description = new Label(
                "If you have necessary additional or emergency expenses next month, " +
                        "you can input the approximate reserved amount and the AI will automatically " +
                        "adjust your consumption budget for you."
        );
        description.setFont(javafx.scene.text.Font.font(12));
        description.setWrapText(true);

        // Create the input field for emergency value
        inputField = new TextField();
        inputField.setPromptText("Value");
        inputField.setPrefWidth(400);

        VBox section = new VBox(10);
        section.getChildren().addAll(description, inputField);
        return section;
    }
}