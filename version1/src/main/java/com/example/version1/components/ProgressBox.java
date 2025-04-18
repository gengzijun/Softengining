package com.example.version1.components;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import java.text.NumberFormat;
import java.util.Locale;

public class ProgressBox extends VBox {
    // 格式化相关常量
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    private static final double DEFAULT_AMOUNT = 120_000;
    private static final String DEFAULT_FORMATTED = "120,000";

    public ProgressBox() {
        initialize(); // 初始化 ProgressBox 布局
        // 配置全局格式化参数
        CURRENCY_FORMAT.setMaximumFractionDigits(2);
        CURRENCY_FORMAT.setMinimumFractionDigits(0);
        CURRENCY_FORMAT.setGroupingUsed(true);
    }

    private void initialize() {
        // 基本样式和布局设置
        setAlignment(Pos.CENTER);
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px;");
        setPadding(new Insets(10));
        setSpacing(5);

        // 创建并添加顶部信息区域（目标，开始日期，结束日期）
        HBox topInfo = createTopInfo();

        // 创建并添加进度条区域
        HBox progressArea = createProgressBar();

        // 创建并添加底部信息区域（已存金额，剩余金额）
        HBox bottomInfo = createBottomInfo();
        // 将所有区域添加到 ProgressBox
        getChildren().addAll(topInfo, progressArea, bottomInfo);
    }

    private HBox createTopInfo() {
        // 目标部分（修改后的可编辑金额）
        HBox targetBox = new HBox(5);
        Label targetTitle = new Label("Target: ");
        targetTitle.setFont(Font.font("Arial", FontWeight.BOLD, 21));

        // 人民币符号（不可编辑部分）
        Label currencySymbol = new Label("¥");
        currencySymbol.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField amountInput = new TextField(DEFAULT_FORMATTED);
        configureAmountInput(amountInput);

        // 组合货币符号和输入框
        HBox amountContainer = new HBox(2);
        amountContainer.setAlignment(Pos.CENTER_LEFT);
        amountContainer.getChildren().addAll(currencySymbol, amountInput);

        targetBox.getChildren().addAll(targetTitle, amountContainer);

        // 保持原有开始日期部分
        HBox startDateBox = new HBox(5);
        startDateBox.setAlignment(Pos.CENTER);
        Label startDateTitle = new Label("Start from: ");
        startDateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ComboBox<String> startDateComboBox = createYearComboBox();
        startDateBox.getChildren().addAll(startDateTitle, startDateComboBox);

        // 保持原有结束日期部分
        HBox endDateBox = new HBox(5);
        endDateBox.setAlignment(Pos.CENTER);
        Label endDateTitle = new Label("End by: ");
        endDateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ComboBox<String> endDateComboBox = createYearComboBox();
        endDateBox.getChildren().addAll(endDateTitle, endDateComboBox);

        // 布局容器（保持原有间距）
        HBox topInfo = new HBox(40);
        topInfo.setAlignment(Pos.CENTER);
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        topInfo.getChildren().addAll(
                targetBox, spacer1,
                startDateBox, spacer2,
                endDateBox
        );

        return topInfo;
    }

    // 创建一个 ComboBox，预填充从 2022 到 2030 的年份，并调整垂直对齐
    private ComboBox<String> createYearComboBox() {
        ComboBox<String> yearComboBox = new ComboBox<>();

        // 填充年份选项
        for (int i = 2022; i <= 2030; i++) {
            yearComboBox.getItems().add(String.valueOf(i));
        }

        // 默认选择 2022
        yearComboBox.setValue("2022");

        // 调整后的样式（核心改动）
        yearComboBox.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-family: Arial;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: -2 0 -2 0;" +    // 减少内部填充压力
                        "-fx-alignment: center;" +     // 强制文本垂直居中
                        "-fx-cell-size: 25px;"         // 统一行高
        );

        // 固定高度以避免布局计算偏差
        yearComboBox.setPrefHeight(25);

        return yearComboBox;
    }

    private HBox createProgressBar() {
        // 创建并配置进度条
        ProgressBar progressBar = new ProgressBar(0.41473);
        progressBar.setPrefWidth(1100);
        progressBar.setPrefHeight(50);
        progressBar.setStyle(
                "-fx-background-radius: 25px 0 0 25px;"
                        + "-fx-border-radius: 25px;"
                        + "-fx-accent: #B6B0ED;"
                        + "-fx-background-color: #E0E0E0;"
                        + "-fx-border-color: transparent;"
                        + "-fx-padding: 0;"
                        + "-fx-background-insets: 0;"
        );

        // 动态调整进度条的圆角
        progressBar.progressProperty().addListener((obs, oldVal, newVal) -> {
            String style = progressBar.getStyle();
            if (newVal.doubleValue() >= 1.0) {
                progressBar.setStyle(style.replace("25px 0 0 25px", "25px"));
            } else {
                progressBar.setStyle(style.replace("25px", "25px 0 0 25px"));
            }
        });

        // 创建并配置百分比标签
        Label progressPercentage = new Label("41.473%");
        progressPercentage.setTextFill(Color.web("#CCA4EB"));
        progressPercentage.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // 将进度条和百分比标签叠加在一起
        StackPane progressStack = new StackPane();
        progressStack.getChildren().addAll(progressBar, progressPercentage);
        progressStack.setClip(null);

        // 创建进度条和标签的容器
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(progressStack);
        return container;
    }

    private HBox createBottomInfo() {
        // 已存金额部分
        HBox savedBox = new HBox(10);
        Label savedLabel = new Label("Have saved: ");
        savedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label savedAmount = new Label("¥49,200");
        savedAmount.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        savedAmount.setTextFill(Color.web("#82DD6E"));
        savedBox.getChildren().addAll(savedLabel, savedAmount);

        // 剩余金额部分
        HBox remainingBox = new HBox(10);
        Label remainingLabel = new Label("Still need: ");
        remainingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label remainingAmount = new Label("¥70,800");
        remainingAmount.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        remainingAmount.setTextFill(Color.web("#C24943"));
        remainingBox.getChildren().addAll(remainingLabel, remainingAmount);

        // 底部信息的布局容器
        HBox bottomInfo = new HBox(1000);
        bottomInfo.setAlignment(Pos.CENTER);
        bottomInfo.getChildren().addAll(savedBox, remainingBox);
        return bottomInfo;
    }

    private void configureAmountInput(TextField input) {
        // 样式设置
        input.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        input.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0 0 0 2px;");

        // 实时输入验证
        input.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("^(\\d{1,3}(,\\d{3})*|\\d+)(\\.\\d{0,2})?$")) {
                String sanitized = newVal
                        .replaceAll("[^\\d.]", "")  // 移除非数字和小数点
                        .replaceAll("(\\..*)\\.", "$1"); // 只保留第一个小数点
                input.setText(sanitized);
            }
        });

        // 焦点处理
        input.focusedProperty().addListener((obs, oldVal, hasFocus) -> {
            if (hasFocus) {
                input.setText(input.getText().replaceAll("[^\\d.]", ""));
            } else {
                formatCurrencyInput(input);
            }
        });
    }

    private void formatCurrencyInput(TextField input) {
        String raw = input.getText().replaceAll("[^\\d.]", "");

        try {
            if (raw.isEmpty()) {
                input.setText(DEFAULT_FORMATTED);
                return;
            }

            // 处理前导零（保留单个零的情况）
            String normalized = raw.replaceFirst("^0+(?=\\d)", "");
            if (normalized.startsWith(".")) normalized = "0" + normalized;

            double value = Double.parseDouble(normalized);
            String formatted = CURRENCY_FORMAT.format(value);

            // 处理没有小数位的情况（避免显示.00）
            if (formatted.endsWith(".00")) {
                formatted = formatted.substring(0, formatted.length() - 3);
            }

            input.setText(formatted);
        } catch (NumberFormatException e) {
            input.setText(DEFAULT_FORMATTED);
        }
    }
    private Label createAmountLabel(String text, String color) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label.setTextFill(Color.web(color));
        return label;
    }

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}