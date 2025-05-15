package com.example.app.saving_goal.components;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NavBox {
    private Button prevButton;

    // Method to create and return the navigation box
    public HBox getNavBox() {
        HBox navBox = new HBox(1280);
        navBox.setAlignment(Pos.CENTER);
        navBox.setStyle("-fx-background-color: #EEDFCC; -fx-border-color: #D3D3D3; -fx-border-width: 2px;");

        prevButton = new Button("<-previous");
        prevButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        HBox.setHgrow(prevButton, Priority.ALWAYS);

        Label appTitle = new Label("Tally app");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        HBox.setHgrow(appTitle, Priority.ALWAYS);

        navBox.getChildren().addAll(prevButton, appTitle);
        return navBox;
    }

    // Method to create and return the title with icon box
    public HBox getTitleWithIconBox() {
        HBox titleWithIconBox = new HBox(10);
        titleWithIconBox.setAlignment(Pos.CENTER_LEFT);

        Label piggyBankLabel = new Label("Piggy bank for 2024");
        piggyBankLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        piggyBankLabel.setTextFill(javafx.scene.paint.Color.GRAY);
        HBox.setMargin(piggyBankLabel, new Insets(0, 0, 4, 10));

        javafx.scene.image.Image iconImage = null;
        try {
            iconImage = new javafx.scene.image.Image(new java.io.FileInputStream("src/main/resources/images/emoji3.png"));
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
        javafx.scene.image.ImageView iconImageView = new javafx.scene.image.ImageView(iconImage);
        iconImageView.setFitWidth(20);
        iconImageView.setFitHeight(20);

        titleWithIconBox.getChildren().addAll(piggyBankLabel, iconImageView);
        return titleWithIconBox;
    }

    /** 提供 prevButton 以便外部绑定返回事件 */
    public Button getPrevButton() {
        return prevButton;
    }
}
