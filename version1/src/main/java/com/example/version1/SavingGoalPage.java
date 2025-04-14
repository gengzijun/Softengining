package com.example.version1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;  // Import FontWeight
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SavingGoalPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建根布局
        BorderPane root = new BorderPane();

        // 设置根布局背景颜色为白色
        root.setStyle("-fx-background-color: white;");

        // 第一个框：导航区域
        HBox navBox = new HBox(1280);
        navBox.setAlignment(Pos.CENTER);
        navBox.setStyle("-fx-background-color: #EEDFCC; -fx-border-color: #D3D3D3; -fx-border-width: 2px;");

        // 设置"previous"按钮为透明
        Button prevButton = new Button("<-previous");
        prevButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"); // 设置透明背景和边框
        HBox.setHgrow(prevButton, Priority.ALWAYS); // 让按钮尽可能靠左

        // 设置"Tally App"为加粗
        Label appTitle = new Label("Tally app");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));  // 设置字体加粗
        HBox.setHgrow(appTitle, Priority.ALWAYS); // 让标题尽可能靠右

        navBox.getChildren().addAll(prevButton, appTitle);

        // 新的框：显示文字和图标
        HBox titleWithIconBox = new HBox(10);
        titleWithIconBox.setAlignment(Pos.CENTER_LEFT);

        // 文字标签
        Label piggyBankLabel = new Label("Piggy bank for 2024");
        piggyBankLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));  // 设置字体为Arial，加粗，大小为25
        piggyBankLabel.setTextFill(Color.GRAY);  // 设置文字颜色为灰色

        // 加载图标
        Image iconImage = null;
        try {
            iconImage = new Image(new FileInputStream("src/main/resources/emoji3.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView iconImageView = new ImageView(iconImage);
        iconImageView.setFitWidth(20); // 设置图标的宽度
        iconImageView.setFitHeight(20); // 设置图标的高度

        // 将文字和图标放入 HBox
        titleWithIconBox.getChildren().addAll(piggyBankLabel, iconImageView);

        // 将 navBox 和 titleWithIconBox 放入 VBox
        VBox topBox = new VBox(10);
        topBox.getChildren().addAll(navBox, titleWithIconBox);

        root.setTop(topBox);  // 设置为顶部框

        // 第二个框：存款目标进度（优化版）
        VBox progressBox = new VBox(40);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 2px;"); // 设置淡灰色边框

        HBox topInfo = new HBox(400);
        topInfo.setAlignment(Pos.CENTER);
        Label targetLabel = new Label("Target: ¥120,000");
        Label startDateLabel = new Label("Start from: 2024-7-3");
        Label endDateLabel = new Label("End by: 2025-12-31");

        targetLabel.setFont(Font.font(16));
        startDateLabel.setFont(Font.font(16));
        endDateLabel.setFont(Font.font(16));

        topInfo.getChildren().addAll(targetLabel, startDateLabel, endDateLabel);

        ProgressBar progressBar = new ProgressBar(0.41473); // 41.473% 完成进度
        progressBar.setPrefWidth(1100);  // 设置宽度为800像素
        progressBar.setPrefHeight(30);  // 设置高度为30像素
        progressBar.setStyle("-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-accent: #B6B0ED; -fx-border-color: transparent;");  // 设置进度条颜色为淡紫色

// 创建一个 Label 来显示百分比
        Label progressPercentage = new Label("41.473%");
        progressPercentage.setTextFill(Color.web("#CCA4EB"));  // 设置完成进度数字为淡紫色
        progressPercentage.setFont(Font.font("Arial", FontWeight.BOLD, 18));  // 设置字体加粗，大小为18
        progressPercentage.setStyle("-fx-font-weight: bold;");  // 通过CSS加粗字体

// 使用 StackPane 来将进度条和百分比标签放在一起
        StackPane progressStack = new StackPane();
        progressStack.getChildren().addAll(progressBar, progressPercentage);
        progressStack.setAlignment(Pos.CENTER); // 让百分比标签在进度条中居中显示

        HBox progressBoxInner = new HBox(10);
        progressBoxInner.setAlignment(Pos.CENTER);
        progressBoxInner.getChildren().add(progressStack); // 将 StackPane 添加到进度框

        // 设置底部的已存款和剩余金额信息
        HBox bottomInfo = new HBox(700);  // 设置间距为40（可以根据需要调整）
        bottomInfo.setAlignment(Pos.CENTER);  // 设置对齐方式为居中

        // 创建 "Have saved:" 和金额的 HBox
        HBox savedBox = new HBox(10);  // 设置 "Have saved:" 和金额之间的间距
        Label savedLabel = new Label("Have saved: ");
        savedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));  // 设置字体为黑色且加粗
        savedLabel.setTextFill(Color.BLACK);  // 设置为黑色

        Label savedAmount = new Label("¥49,200");
        savedAmount.setFont(Font.font("Arial", FontWeight.BOLD, 20));  // 设置为加粗且放大字体
        savedAmount.setTextFill(Color.web("#82DD6E"));  // 使用十六进制颜色代码设置金额颜色

        savedBox.getChildren().addAll(savedLabel, savedAmount);  // 将 "Have saved:" 和金额放入同一个 HBox

        // 创建 "Still need:" 和金额的 HBox
        HBox remainingBox = new HBox(10);  // 设置 "Still need:" 和金额之间的间距
        Label remainingLabel = new Label("Still need: ");
        remainingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));  // 设置字体为黑色且加粗
        remainingLabel.setTextFill(Color.BLACK);  // 设置为黑色

        Label remainingAmount = new Label("¥70,800");
        remainingAmount.setFont(Font.font("Arial", FontWeight.BOLD, 20));  // 设置为加粗且放大字体
        remainingAmount.setTextFill(Color.web("#C24943"));  // 使用十六进制颜色代码设置金额颜色

        remainingBox.getChildren().addAll(remainingLabel, remainingAmount);  // 将 "Still need:" 和金额放入同一个 HBox

        bottomInfo.getChildren().addAll(savedBox, remainingBox);  // 将两个 HBox 放入 main HBox 中，增加它们之间的间距

        progressBox.getChildren().addAll(topInfo, progressBoxInner, bottomInfo);  // 添加到进度框中

        // 第三个框：详细存款信息
        VBox detailsBox = new VBox(10);
        detailsBox.setAlignment(Pos.TOP_LEFT);
        detailsBox.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 2px;"); // 设置淡灰色边框

        Label detailsLabel = new Label("Details");
        detailsLabel.setFont(Font.font(16));

        Label month2024_10 = new Label("2024-10");
        TextArea month2024_10Details = new TextArea("Saved: ¥14,032\nAdd up: ¥49,200");
        month2024_10Details.setEditable(false);
        month2024_10Details.setWrapText(true);
        // 设置TextArea的大小
        month2024_10Details.setPrefWidth(1200);  // 设置宽度为250像素
        month2024_10Details.setPrefHeight(60);  // 设置高度为60像素

        Label month2024_9 = new Label("2024-9");
        ComboBox<String> moreDetailsComboBox2024_9 = new ComboBox<>();
        moreDetailsComboBox2024_9.getItems().add("More Details");
        moreDetailsComboBox2024_9.setValue("More Details");

        Label month2024_8 = new Label("2024-8");
        ComboBox<String> moreDetailsComboBox2024_8 = new ComboBox<>();
        moreDetailsComboBox2024_8.getItems().add("More Details");
        moreDetailsComboBox2024_8.setValue("More Details");

        Label month2024_7 = new Label("2024-7");
        ComboBox<String> moreDetailsComboBox2024_7 = new ComboBox<>();
        moreDetailsComboBox2024_7.getItems().add("More Details");
        moreDetailsComboBox2024_7.setValue("More Details");

        detailsBox.getChildren().addAll(detailsLabel, month2024_10, month2024_10Details, month2024_9, moreDetailsComboBox2024_9,
                month2024_8, moreDetailsComboBox2024_8, month2024_7, moreDetailsComboBox2024_7);

        // 第四个框：紧急费用输入框（修改后的框架）
        VBox emergencyBox = new VBox(10);
        emergencyBox.setAlignment(Pos.TOP_LEFT);
        emergencyBox.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 2px;");

// 左侧：图标和标题
        HBox iconAndTitle_4 = new HBox(10);
        iconAndTitle_4.setAlignment(Pos.CENTER_LEFT);

// 加载图标
        Image iconImage_4 = null;
        try {
            iconImage_4 = new Image(new FileInputStream("src/main/resources/emoji1.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView iconImageView_4 = new ImageView(iconImage_4);
        iconImageView_4.setFitWidth(20);
        iconImageView_4.setFitHeight(20);

// 标题"Any Emergency"
        Label emergencyLabel = new Label("Any Emergency");
        emergencyLabel.setFont(Font.font(16));

        iconAndTitle_4.getChildren().addAll(iconImageView_4, emergencyLabel);

// 右侧：文字说明和输入框
        VBox rightBox = new VBox(10);
        rightBox.setAlignment(Pos.TOP_LEFT);

// 文字说明
        Label descriptionLabel = new Label("If you have necessary additional or emergency expenses next month, you can input the approximate reserved amount and the AI will automatically adjust your consumption budget for you.");
        descriptionLabel.setFont(Font.font(12));
        descriptionLabel.setWrapText(true);  // 自动换行

// 输入框
        TextField emergencyField = new TextField();
        emergencyField.setPromptText("Value");
        emergencyField.setPrefWidth(400); // 设置文本框宽度

        rightBox.getChildren().addAll(descriptionLabel, emergencyField);

// 将左侧和右侧内容放入主容器
        HBox mainBox_4 = new HBox(20);
        mainBox_4.setAlignment(Pos.CENTER_LEFT);
        mainBox_4.getChildren().addAll(iconAndTitle_4, rightBox);

// 将整个紧急费用输入框部分添加到 emergencyBox
        emergencyBox.getChildren().add(mainBox_4);

// 第五个框：存钱技巧区域
        VBox savingTipsBox = new VBox(10);
        savingTipsBox.setAlignment(Pos.TOP_LEFT);
        savingTipsBox.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 2px;");

// 使用 HBox 来排列 Saving Tips 标题和图片区域
        HBox savingTipsHBox = new HBox(10);  // 创建 HBox 使图片和标题水平排列
        savingTipsHBox.setAlignment(Pos.TOP_LEFT);  // 设置为左对齐

// 左侧图标和标题
        HBox iconAndTitle_5 = new HBox(10);
        iconAndTitle_5.setAlignment(Pos.CENTER_LEFT);

// 加载图标
        Image iconImage_5 = null;
        try {
            iconImage_5 = new Image(new FileInputStream("src/main/resources/emoji2.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView iconImageView_5 = new ImageView(iconImage_5);
        iconImageView_5.setFitWidth(20);
        iconImageView_5.setFitHeight(20);

// 标题
        Label savingTipsLabel = new Label("Saving Tips");
        savingTipsLabel.setFont(Font.font(16));

        iconAndTitle_5.getChildren().addAll(iconImageView_5, savingTipsLabel);

// 将标题和图标放入 savingTipsHBox
        savingTipsHBox.getChildren().add(iconAndTitle_5);

// 右侧图片区域（水平排列）
        HBox imageBox = new HBox(10);  // 使用 HBox 来水平排列图片
        imageBox.setAlignment(Pos.CENTER_LEFT);  // 设置为左对齐

// 假设五个图片路径，加载并设置
        for (int i = 0; i < 5; i++) {
            Image image = null;
            try {
                image = new Image(new FileInputStream("src/main/resources/image" + (i + 1) + ".png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 创建图片视图并设置大小
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(100);

            // 为每个图片添加点击事件
            final int index = i; // 捕获图片索引
            imageView.setOnMouseClicked(event -> {
                System.out.println("Image " + (index + 1) + " clicked");
            });

            imageBox.getChildren().add(imageView);  // 添加图片到 HBox 中
        }

// 将图片区域添加到 savingTipsHBox 中
        savingTipsHBox.getChildren().add(imageBox);

// 将 savingTipsHBox 放入 savingTipsBox
        savingTipsBox.getChildren().add(savingTipsHBox);


        // 设置主内容区域
        VBox mainContent = new VBox(30);
        mainContent.getChildren().addAll(progressBox, detailsBox, emergencyBox, savingTipsBox);

        root.setCenter(mainContent);

        // 设置场景
        Scene scene = new Scene(root, 1440, 810, Color.WHITE);
        primaryStage.setTitle("Saving Goal Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}





