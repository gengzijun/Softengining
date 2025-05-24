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

public class RedBook1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建根布局
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // 创建最外层容器
        StackPane outerPane = new StackPane();
        outerPane.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15px; -fx-border-color: #dcdcdc; -fx-border-width: 1px;");

        // 左侧图片区域
        Image image = new Image(getClass().getResource("/images/image1.png").toExternalForm());
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
        Image avatarImage = new Image(getClass().getResourceAsStream("/images/emoji5.png"));
        ImageView avatarView = new ImageView(avatarImage);
        avatarView.setFitWidth(40);
        avatarView.setFitHeight(40);
        Circle clip = new Circle(20, 20, 20);
        avatarView.setClip(clip);

        // 姓名标签
        Label nameLabel = new Label("Whatever");
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
        Label titleLabel = new Label("7 Books You Must Read to Get Started in Finance");
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
        textArea.setText("1) \"The Psychology of Money\"（《金钱心理学》）- Morgan Housel\n" +
                "   通过众多故事揭示金钱与心理的紧密联系，阐述人们在金钱决策背后的心理因素，帮助读者理解个人经历等如何影响理财行为。\n\n" +
                "2) \"The Intelligent Investor\" （《聪明的投资者》）- Benjamin Graham\n" +
                "   作为投资领域的经典之作，系统阐述了价值投资理念，提供理性投资策略和方法，是价值投资的奠基之作。\n\n" +
                "3) \"Thinking, Fast and Slow\" （《思考，快与慢》）- Daniel Kahneman\n" +
                "   深入剖析人类思维的两种模式，揭示思维机制在决策判断中的影响，对行为经济学具有重大意义。\n\n" +
                "4) \"Margin of Safety\" （《安全边际》）- Seth Klarman\n" +
                "   强调投资中的安全边际原则，讲解如何通过深入分析和风险控制实现稳健投资。\n\n" +
                "5) \"One Up on Wall Street\" （《彼得・林奇的成功投资》）- Peter Lynch\n" +
                "   结合实战经验，介绍从日常生活发现投资机会的方法，提供实用股票投资策略。\n\n" +
                "6) \"The Most Important Thing\" （《投资最重要的事》）- Howard Marks\n" +
                "   分享多年投资心得，探讨关键投资理念，强调风险控制和逆向思维的重要性。\n\n" +
                "7) \"Fooled by Randomness\" （《随机漫步的傻瓜》）- Nassim Taleb\n" +
                "   揭示金融市场中的随机性影响，提醒警惕随机现象误导，重新审视风险认知。");

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
        primaryStage.setTitle("RedBook1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}