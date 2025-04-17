package com.example;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPage extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("TallyAPP Login");

        // 应用标题
        Label title = new Label("Welcome to TallyAPP");
        title.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        title.setPadding(new Insets(0, 0, 20, 0));

        // 邮箱输入
        HBox emailBox = createIconInput("/images/email.png", "Email", false);
        TextField emailField = (TextField) emailBox.getChildren().get(1);

        // 密码输入
        HBox passBox = createIconInput("/images/lock.png", "Password", true);
        PasswordField passField = (PasswordField) passBox.getChildren().get(1);

        // 登录按钮
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(
                "-fx-font-family: 'Segoe UI', sans-serif; -fx-background-color: #3498db; " +
                        "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 25; -fx-padding: 12 25; -fx-cursor: hand;"
        );
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(loginBtn, new Insets(20, 0, 0, 0));
        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setStyle(
                    "-fx-font-family: 'Segoe UI', sans-serif; -fx-background-color: #2980b9; " +
                            "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                            "-fx-background-radius: 25; -fx-padding: 12 25; -fx-cursor: hand;"
            );
        });
        loginBtn.setOnMouseExited(e -> {
            loginBtn.setStyle(
                    "-fx-font-family: 'Segoe UI', sans-serif; -fx-background-color: #3498db; " +
                            "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                            "-fx-background-radius: 25; -fx-padding: 12 25; -fx-cursor: hand;"
            );
        });

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
        forgot.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-text-fill: #7f8c8d; -fx-underline: true; -fx-cursor: hand;");
        forgot.setOnMouseClicked(e -> showAlert("Feature Not Implemented", "Coming soon..."));
        forgot.setOnMouseEntered(e -> forgot.setTextFill(Color.web("#3498db")));
        forgot.setOnMouseExited(e -> forgot.setTextFill(Color.web("#7f8c8d")));

        Label register = new Label("Register");
        register.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-text-fill: #7f8c8d; -fx-underline: true; -fx-cursor: hand;");
        register.setOnMouseClicked(e -> showAlert("Feature Not Implemented", "Registration coming soon..."));
        register.setOnMouseEntered(e -> register.setTextFill(Color.web("#3498db")));
        register.setOnMouseExited(e -> register.setTextFill(Color.web("#7f8c8d")));

        HBox bottomLinks = new HBox(10, forgot, register);
        bottomLinks.setAlignment(Pos.CENTER);
        bottomLinks.setPadding(new Insets(20, 0, 0, 0));

        // 卡片容器
        VBox card = new VBox(20, title, emailBox, passBox, loginBtn, bottomLinks);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(450);
        card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 20, 0, 0, 10);"
        );

        // 添加背景图片
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(600);
        backgroundImageView.setEffect(new BoxBlur(5, 5, 3)); // 应用模糊效果，调整参数以改变模糊程度

        // 创建一个 Pane 来放置背景图片和登录框
        Pane rootPane = new Pane();
        rootPane.getChildren().add(backgroundImageView);

        // 确保布局在窗口大小改变时更新
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            card.setLayoutX((newValue.doubleValue() - card.getMaxWidth()) / 2);
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            card.setLayoutY((newValue.doubleValue() - card.getHeight()) / 2);
        });

        // 初始布局设置
        card.setLayoutX((800 - card.getMaxWidth()) / 2);
        card.setLayoutY((600 - card.getHeight()) / 2);
        rootPane.getChildren().add(card);

        // 淡入动画
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.4), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        Scene scene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createIconInput(String iconPath, String prompt, boolean isPassword) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        HBox.setMargin(icon, new Insets(0, 15, 0, 0));

        Control input;
        if (isPassword) {
            PasswordField pass = new PasswordField();
            pass.setPromptText(prompt);
            pass.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-background-radius: 20; -fx-padding: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 20;");
            pass.setOnMouseEntered(e -> pass.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-background-radius: 20; -fx-padding: 12; -fx-border-color: #3498db; -fx-border-width: 1; -fx-border-radius: 20;"));
            pass.setOnMouseExited(e -> pass.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-background-radius: 20; -fx-padding: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 20;"));
            input = pass;
        } else {
            TextField text = new TextField();
            text.setPromptText(prompt);
            text.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-background-radius: 20; -fx-padding: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 20;");
            text.setOnMouseEntered(e -> text.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-background-radius: 20; -fx-padding: 12; -fx-border-color: #3498db; -fx-border-width: 1; -fx-border-radius: 20;"));
            text.setOnMouseExited(e -> text.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-background-radius: 20; -fx-padding: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 20;"));
            input = text;
        }

        HBox.setHgrow(input, Priority.ALWAYS);
        ((Region) input).setMaxWidth(Double.MAX_VALUE);

        HBox box = new HBox(icon, input);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(350);
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