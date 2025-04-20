package com.example;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("TallyAPP Login");

        // 应用标题
        Label title = new Label("Welcome to TallyAPP");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // 邮箱输入
        HBox emailBox = createIconInput("/images/email.png", "Email", false);
        TextField emailField = (TextField) emailBox.getChildren().get(1);

        // 密码输入
        HBox passBox = createIconInput("/images/lock.png", "Password", true);
        PasswordField passField = (PasswordField) passBox.getChildren().get(1);

        // 登录按钮
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 20;"
        );
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(loginBtn, new Insets(10, 0, 0, 0));
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(
            "-fx-background-color: #45a049;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 20;"
        ));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 20;"
        ));

        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String pass = passField.getText();
            if (!isValidEmail(email)) {
                showAlert("Invalid Email", "Please enter a valid email address.");
                return;
            }

            if (authenticate(email, pass)) {
                showMainPage();
                primaryStage.close();
            } else {
                showAlert("Login Failed", "Incorrect email or password.");
            }
        });

        // 辅助链接
        Label forgot = new Label("Forgot password?");
        forgot.setStyle("-fx-text-fill: #555; -fx-underline: true;");
        forgot.setOnMouseClicked(e -> showAlert("Feature Not Implemented", "Coming soon..."));

        Label register = new Label("Register");
        register.setStyle("-fx-text-fill: #555; -fx-underline: true;");
        register.setOnMouseClicked(e -> showAlert("Feature Not Implemented", "Registration coming soon..."));

        HBox bottomLinks = new HBox(10, forgot, register);
        bottomLinks.setAlignment(Pos.CENTER);
        bottomLinks.setPadding(new Insets(10, 0, 0, 0));

        // 卡片容器
        VBox card = new VBox(18, title, emailBox, passBox, loginBtn, bottomLinks);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );

        // 根布局
        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e0eafc, #cfdef3);");
        root.setPrefSize(600, 500);  // 更舒适的窗口比例

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createIconInput(String iconPath, String prompt, boolean isPassword) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        HBox.setMargin(icon, new Insets(0, 10, 0, 0));

        Control input;
        if (isPassword) {
            PasswordField pass = new PasswordField();
            pass.setPromptText(prompt);
            pass.setStyle("-fx-background-radius: 8; -fx-padding: 8;");
            input = pass;
        } else {
            TextField text = new TextField();
            text.setPromptText(prompt);
            text.setStyle("-fx-background-radius: 8; -fx-padding: 8;");
            input = text;
        }

        HBox.setHgrow(input, Priority.ALWAYS);
        ((Region) input).setMaxWidth(Double.MAX_VALUE);

        HBox box = new HBox(icon, input);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(300);
        return box;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^\\S+@\\S+\\.\\S+$");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private boolean authenticate(String email, String pass) {
        return "admin@163.com".equalsIgnoreCase(email.trim()) && "admin".equals(pass);
    }

    private void showMainPage() {
        Stage mainStage = new Stage();
        MainPage mainApp = new MainPage();
        mainApp.start(mainStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
