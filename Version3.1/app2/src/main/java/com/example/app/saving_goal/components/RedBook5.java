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

public class RedBook5 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建根布局
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // 创建最外层容器
        StackPane outerPane = new StackPane();
        outerPane.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15px; -fx-border-color: #dcdcdc; -fx-border-width: 1px;");

        // 左侧图片区域
        Image image = new Image(getClass().getResource("/images/image5.png").toExternalForm());
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
        Image avatarImage = new Image(getClass().getResourceAsStream("/images/emoji9.png"));
        ImageView avatarView = new ImageView(avatarImage);
        avatarView.setFitWidth(40);
        avatarView.setFitHeight(40);
        Circle clip = new Circle(20, 20, 20);
        avatarView.setClip(clip);

        // 姓名标签
        Label nameLabel = new Label("Belle");
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
        Label titleLabel = new Label("Top 7 rules for saving money");
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
        textArea.setText("1. 50/30/20 Budget Rule: Divide income into three parts: 50 per cent for needs, 30 per cent for wants and 20 per cent for savings. Examples of needs include food, housing, insurance, etc.; examples of wants include travelling, fashionable gear, etc.; and savings can be used to pay for emergency funds, debt repayment, investments and retirement.\n\n" +
                "2. The 1 per cent rule of impulse buying: If an item exceeds 1 per cent of annual income, one should wait three days before deciding whether to buy it. The reason for this is that people usually realise after three days that they may not really need that item.\n\n" +
                "3. Rule of 72: This is a quick formula for calculating the time it takes to double. The way to do this is to divide 72 by the interest rate. For example, an 8 per cent return means that the funds will double every nine years.\n\n" +
                "4. 401(k) MATCH RULE: Many employers will match a portion of their employees' 401(k) contributions. Therefore, maximise your contributions until you reach the highest level of employer match.\n\n" +
                "5. 3X EMERGENCY FUND RULE: Keep 3 to 6 times your monthly income as an emergency fund. That way, when the unexpected happens, you'll have enough money to cover it.\n\n" +
                "6. Automation rules: default settings are powerful because people are lazy. So force savings through automation. The author highly recommends Ramit Seth's book ‘I Will Teach You to Be Rich’. This book emphasises on saving money before you see it and creating automated money systems.\n\n"+
                "7. The in-and-out principle: if you buy one item, then donate, throw away or sell another. Minimalism is a double discipline: manage incoming and outgoing possessions to enjoy balance.");

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
        primaryStage.setTitle("RedBook5");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

