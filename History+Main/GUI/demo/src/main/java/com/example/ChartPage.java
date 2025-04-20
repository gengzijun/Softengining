package com.example;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChartPage {

    public static Scene getScene(Stage stage) {
        Label label = new Label("📊 Chart Page");
        Button backButton = new Button("⬅ Back to Main");
        backButton.setOnAction(e -> stage.setScene(MainPage.getMainScene(stage)));

        VBox root = new VBox(20, label, backButton);
        root.setStyle("-fx-alignment: center; -fx-padding: 50px; -fx-font-size: 18px;");
        return new Scene(root, 900, 600);
    }
}
