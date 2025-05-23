package com.example.app.saving_goal.components;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RedBook3 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建根布局
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // 创建最外层容器
        StackPane outerPane = new StackPane();
        outerPane.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15px; -fx-border-color: #dcdcdc; -fx-border-width: 1px;");

        // 左侧图片区域
        Image image = new Image(getClass().getResource("/images/image3.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setStyle("-fx-background-color: white;");
        root.setLeft(imageContainer);

        // 右侧内容区域
        VBox rightSection = new VBox(10);
        rightSection.setAlignment(Pos.TOP_LEFT);
        rightSection.setStyle("-fx-background-color: white;");

        // 顶部头像+关注区域
        HBox topHeader = new HBox();
        topHeader.setStyle("-fx-background-color: white;");
        topHeader.setAlignment(Pos.CENTER_LEFT);
        topHeader.setPadding(new Insets(10, 20, 10, 20));

        // 头像
        Image avatarImage = new Image(getClass().getResourceAsStream("/images/emoji7.png"));
        ImageView avatarView = new ImageView(avatarImage);
        avatarView.setFitWidth(40);
        avatarView.setFitHeight(40);
        Circle clip = new Circle(20, 20, 20);
        avatarView.setClip(clip);

        // 姓名标签
        Label nameLabel = new Label("Boo");
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // 关注按钮
        Button followButton = new Button("Subscribe");
        followButton.setStyle("-fx-background-color: #FF4444; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 50em; " +
                "-fx-padding: 8 20;");

        // 布局组装
        HBox leftContent = new HBox(10, avatarView, nameLabel);
        leftContent.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topHeader.getChildren().addAll(leftContent, spacer, followButton);

        // 中部内容区域
        VBox middlePart = new VBox(10);
        middlePart.setStyle("-fx-background-color: white;");

        // 添加标题
        Label titleLabel = new Label("How I'm saving money without noticing");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");
        titleLabel.setPadding(new Insets(0, 0, 10, 5));

        // 正文文本框（修改部分开始）
        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-control-inner-background: white; " +
                "-fx-font-family: 'Microsoft YaHei'; " +
                "-fx-font-size: 16px; " +
                "-fx-background-color: transparent; " +  // 文本框背景透明
                "-fx-border-color: transparent;");       // 隐藏文本框自身边框
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setText("1. No daily make-up Easy and natural, save money on cosmetics.\n" +
                "2. Filling water bottles at work or the gym is environmentally and economically friendly and reduces the frequency of purchasing drinks.。\n" +
                "3. Share meals at dinner Save money and bond with friends at the same time.\n\n" +
                "4. Use cash to avoid surcharges Pay directly to avoid extra charges on your credit card.”\n\n" +
                "5. Always keep a jumper in your car Have something to wear when it's cold and avoid buying clothes on the fly.\n\n" +
                "6. Read more instead of watching TV Elevate yourself and cut down on unnecessary entertainment consumption.");

        // 滚动面板
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setStyle("-fx-background: white; " +
                "-fx-background-radius: 8px; " +         // 背景圆角
                "-fx-border-color: #d0d0d0; " +          // 边框颜色
                "-fx-border-radius: 8px; " +             // 边框圆角
                "-fx-border-width: 1px; " +
                "-fx-padding: 5px;");                   // 内边距
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(480, 500); // 设置固定大小保持布局稳定

        // 组装中部内容
        middlePart.getChildren().addAll(titleLabel, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // 底部按钮
        Button bookListButton = new Button("Get the full list of books");
        bookListButton.setStyle("-fx-background-color: #FF4444; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px;"+
                "-fx-font-weight: bold; ");
        VBox bottomPart = new VBox(bookListButton);
        bottomPart.setAlignment(Pos.BOTTOM_RIGHT);
        bottomPart.setStyle("-fx-background-color: white;");

        // 组装右侧布局
        rightSection.getChildren().addAll(topHeader, middlePart, bottomPart);
        VBox.setVgrow(middlePart, Priority.ALWAYS);

        root.setRight(rightSection);
        outerPane.getChildren().add(root);

        // 设置窗口
        Scene scene = new Scene(outerPane, 1030, 640);
        primaryStage.setTitle("RedBook3");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
