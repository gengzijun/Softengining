package com.example.app.saving_goal.components;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProgressBox extends VBox {
    // 格式化相关常量
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    public static final double DEFAULT_AMOUNT = 120_000;
    private static final String DEFAULT_FORMATTED = "120,000";
    // 新增成员变量保存路径（可自定义）
    private static final String CSV_DIR = "src/main/resources/savinggoal/";
    private static final String CSV_PATH = CSV_DIR + "saving_data.csv";


    private DetailsBox detailsBox;

    private Label savedAmountLabel;  // 用来显示 Have Saved
    private Label remainingAmountLabel;  // 用来显示 Still need

    private TextField amountInput;
    private ComboBox<String> startDateComboBox;
    private ComboBox<String> endDateComboBox;

    private int cachedStartYear = 2024; // 默认值
    private int cachedEndYear = 2025;   // 默认值



    public ProgressBox(DetailsBox detailsBox) {
        this.detailsBox = detailsBox;
        initialize(); // 初始化 ProgressBox 布局
        // 后配置监听器
        bindListeners();
        // 配置全局格式化参数
        CURRENCY_FORMAT.setMaximumFractionDigits(2);
        CURRENCY_FORMAT.setMinimumFractionDigits(0);
        CURRENCY_FORMAT.setGroupingUsed(true);

        cachedStartYear = Integer.parseInt(startDateComboBox.getValue());
        cachedEndYear = Integer.parseInt(endDateComboBox.getValue());

        initializeCSV();

    }

    private void ensureDirExists() {
        try {
            // 自动创建多层目录（如果不存在）
            Files.createDirectories(Paths.get(CSV_DIR));
        } catch (IOException e) {
            System.err.println("创建目录失败: " + e.getMessage());
        }
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

        // 启动时先更新 Have Saved 和 Still need
        updateProgress();
    }

    private void initializeCSV() {
        ensureDirExists(); // ✅ 新增目录检查
        if (!Files.exists(Paths.get(CSV_PATH))) {
            saveToCSV(0.0, DEFAULT_AMOUNT, DEFAULT_AMOUNT);
        }
    }
    private void saveToCSV(double haveSaved, double target, double stillNeed) {
        try {
            int startYear = Integer.parseInt(startDateComboBox.getValue());
            int endYear = Integer.parseInt(endDateComboBox.getValue());

            String header = "target,haveSaved,stillNeed,startYear,endYear,emergency_amount";
            String data = String.format(Locale.US, "%.2f,%.2f,%.2f,%d,%d,0.00",
                    target, haveSaved, stillNeed, startYear, endYear);

            Files.write(Paths.get(CSV_PATH), Arrays.asList(header, data),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("CSV保存失败: " + e.getMessage());
        }
    }

    // 新增保存CSV的方法
    private void saveToCSVWithYears(int startYear, int endYear) {
        ensureDirExists();
        try {
            Path csvPath = Paths.get(CSV_PATH);
            List<String> lines = Files.exists(csvPath) ?
                    Files.readAllLines(csvPath, StandardCharsets.UTF_8) :
                    new ArrayList<>();

            // 处理列头
            String header = "target,haveSaved,stillNeed,startYear,endYear,emergency_amount";
            if (!lines.isEmpty() && !lines.get(0).equals(header)) {
                lines.set(0, header);
            }

            // 处理数据行
            String dataLine;
            if (lines.size() > 1) {
                // 兼容旧数据格式
                String[] oldValues = lines.get(1).split(",");
                dataLine = String.format(Locale.US, "%s,%s,%s,%d,%d,0.00",
                        oldValues[0], oldValues[1], oldValues[2],
                        startYear, endYear);
            } else {
                dataLine = String.format(Locale.US, "%.2f,0.00,%.2f,%d,%d,0.00",
                        DEFAULT_AMOUNT, DEFAULT_AMOUNT,
                        startYear, endYear);
            }

            List<String> newLines = new ArrayList<>();
            newLines.add(header);
            newLines.add(dataLine);
            Files.write(csvPath, newLines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("保存年份到CSV失败: " + e.getMessage());
        }
    }

    private void handleConfirm() {
        if (!validateInputs()) return;

        // 强制更新所有数据
        int startYear = Integer.parseInt(startDateComboBox.getValue());
        int endYear = Integer.parseInt(endDateComboBox.getValue());
        double target = parseTarget(amountInput.getText());

        // 更新 DetailsBox 数据
        detailsBox.updateData(startYear, endYear);

        // 更新 UI 进度
        updateProgress();

        // 统一保存 CSV 数据（合并两个保存方法）
        saveFullCSVData(target, detailsBox.getCumulativeSavings(), startYear, endYear);
    }

    // 新增统一保存方法
    private void saveFullCSVData(double target, double haveSaved, int startYear, int endYear) {
        try {
            String header = "target,haveSaved,stillNeed,startYear,endYear,emergency_amount";
            String data = String.format(Locale.US, "%.2f,%.2f,%.2f,%d,%d,0.00",
                    target, haveSaved, target - haveSaved, startYear, endYear);

            Files.write(Paths.get(CSV_PATH), Arrays.asList(header, data),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.err.println("完整保存CSV失败: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        try {
            // 获取年份数值
            int startYear = Integer.parseInt(startDateComboBox.getValue());
            int endYear = Integer.parseInt(endDateComboBox.getValue());

            // 新增年份顺序检查
            if (startYear >= endYear) {
                showErrorAlert("The start year must be earlier than the end year!");
                return false;
            }

            // 原金额校验
            parseTarget(amountInput.getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorAlert("请输入有效的年份（2022-2030）和金额");
            return false;
        } catch (Exception e) {
            showErrorAlert("输入无效");
            return false;
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error: Invalid input");
        alert.setHeaderText(null);
        alert.setContentText(message + "\nPlease re-enter!");
        alert.showAndWait();
    }

    //已修改
    private void bindListeners() {
        // 绑定日期选择监听
        startDateComboBox.valueProperty().addListener((obs, old, newVal) -> {
//            detailsBox.updateData(Integer.parseInt(newVal), Integer.parseInt(endDateComboBox.getValue()));
//            updateProgress();
        });

        endDateComboBox.valueProperty().addListener((obs, old, newVal) -> {
//            detailsBox.updateData(Integer.parseInt(startDateComboBox.getValue()), Integer.parseInt(newVal));
//            updateProgress();
        });

    }

    private HBox createTopInfo() {
        this.startDateComboBox = createYearComboBox();
        this.endDateComboBox = createYearComboBox();
        // 目标部分（修改后的可编辑金额）
        HBox targetBox = new HBox(5);
        Label targetTitle = new Label("Target: ");
        targetTitle.setFont(Font.font("Arial", FontWeight.BOLD, 21));

        // 人民币符号（不可编辑部分）
        Label currencySymbol = new Label("¥");
        currencySymbol.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        // 正确初始化成员变量（仅一次）
        this.amountInput = new TextField(DEFAULT_FORMATTED);
        configureAmountInput(this.amountInput); // 直接配置成员变量

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
        startDateBox.getChildren().addAll(startDateTitle, this.startDateComboBox);

        // 保持原有结束日期部分
        HBox endDateBox = new HBox(5);
        endDateBox.setAlignment(Pos.CENTER);
        Label endDateTitle = new Label("End by: ");
        endDateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ComboBox<String> endDateComboBox = createYearComboBox();
        endDateBox.getChildren().addAll(endDateTitle, this.endDateComboBox);


        startDateComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            int newStartYear = Integer.parseInt(newValue);
            int newEndYear = Integer.parseInt(this.endDateComboBox.getValue());
            // 打印结果
            System.out.println("Start Year: " + newStartYear + ", End Year: " + newEndYear);
            // 更新数据模型
            detailsBox.updateData(newStartYear, newEndYear);

            // 直接保存最新值到 CSV
            saveToCSVWithYears(newStartYear, newEndYear); // ✅ 新增专用保存方法
            updateProgress(); // 可选：如需同时更新进度条
        });

        endDateComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            int newStartYear = Integer.parseInt(this.startDateComboBox.getValue());
            int newEndYear = Integer.parseInt(newValue);
            // 打印结果
            System.out.println("Start Year: " + newStartYear + ", End Year: " + newEndYear);
            // 更新数据模型
            detailsBox.updateData(newStartYear, newEndYear);

            // 直接保存最新值到 CSV
            saveToCSVWithYears(newStartYear, newEndYear); // ✅ 新增专用保存方法
            updateProgress(); // 可选：如需同时更新进度条
        });

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

        // 动态调整圆角（修复后的逻辑）
        progressBar.progressProperty().addListener((obs, oldVal, newVal) -> {
            String baseStyle = "-fx-border-radius: 25px; -fx-accent: #B6B0ED; -fx-background-color: #E0E0E0;";
            if (newVal.doubleValue() >= 1.0) {
                progressBar.setStyle(baseStyle + " -fx-background-radius: 25px;");
            } else {
                progressBar.setStyle(baseStyle + " -fx-background-radius: 25px 0 0 25px;");
            }
        });

        // 创建并配置百分比标签
        Label progressPercentage = new Label("41.473%");
        progressPercentage.setTextFill(Color.web("#90EE90"));
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

    // 更新进度信息
    public void updateProgress() {

        // 动态解析目标金额
        double target = parseTarget(amountInput.getText());
        double haveSaved = detailsBox.getCumulativeSavings();
        double stillNeed = target - haveSaved;
        int startYear = Integer.parseInt(startDateComboBox.getValue());
        int endYear = Integer.parseInt(endDateComboBox.getValue());
        // 更新 Have Saved 和 Still need 标签
        savedAmountLabel.setText("¥" + NumberFormat.getInstance().format(haveSaved));
        remainingAmountLabel.setText("¥" + NumberFormat.getInstance().format(stillNeed));

        // 更新进度条
        double progress = haveSaved / target;
        System.out.println(haveSaved);
        System.out.println(target);
        System.out.println(stillNeed);

        // 获取进度条和百分比标签
        HBox progressBarContainer = (HBox) getChildren().get(1); // 假设进度条是第二个组件
        StackPane progressStack = (StackPane) progressBarContainer.getChildren().get(0);  // 获取 StackPane
        ProgressBar progressBar = (ProgressBar) progressStack.getChildren().get(0);  // 获取进度条
        Label progressPercentage = (Label) progressStack.getChildren().get(1);  // 获取百分比标签

        // 更新进度条的进度
        progressBar.setProgress(progress);

        // 更新百分比标签
        progressPercentage.setText(String.format("%.2f%%", progress * 100));
        // 保存到CSV（仅更新前三列，使用缓存的年份）
        saveToCSV(haveSaved, target, stillNeed);
    }

    private double parseTarget(String input) {
        try {
            return NumberFormat.getInstance().parse(input.replaceAll("[^\\d.]", "")).doubleValue();
        } catch (Exception e) {
            return DEFAULT_AMOUNT;
        }
    }
    private HBox createBottomInfo() {
        // 已存金额部分
        HBox savedBox = new HBox(20);
        Label savedLabel = new Label("Have saved: ");
        savedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        savedAmountLabel = new Label("¥49,200");
        savedAmountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        savedAmountLabel.setTextFill(Color.web("#82DD6E"));
        savedBox.getChildren().addAll(savedLabel, savedAmountLabel);

        // 剩余金额部分
        HBox remainingBox = new HBox(20);
        Label remainingLabel = new Label("Still need: ");
        remainingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        remainingAmountLabel = new Label("¥70,800");
        remainingAmountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        remainingAmountLabel.setTextFill(Color.web("#C24943"));

        // 新增确认按钮
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-color: #82DD6E; -fx-text-fill: white; -fx-background-radius: 5px;");
        confirmButton.setOnAction(e -> handleConfirm());
        HBox.setMargin(confirmButton, new Insets(0,0,0,40)); // 左间距40px

        remainingBox.getChildren().addAll(remainingLabel, remainingAmountLabel, confirmButton);

        // 底部信息的布局容器
        HBox bottomInfo = new HBox(800);
        bottomInfo.setAlignment(Pos.CENTER);
        bottomInfo.getChildren().addAll(savedBox, remainingBox);
        return bottomInfo;
    }

    //已修改
    private void configureAmountInput(TextField input) {
        // 样式设置
        input.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        input.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0 0 0 2px;");

        // 移除旧的文本变化监听器
        input.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("^(\\d{1,3}(,\\d{3})*|\\d+)(\\.\\d{0,2})?$")) {
                String sanitized = newVal
                        .replaceAll("[^\\d.]", "")
                        .replaceAll("(\\..*)\\.", "$1");
                input.setText(sanitized);
            }
        });

        // 焦点处理（关键修改点）
        input.focusedProperty().addListener((obs, oldVal, hasFocus) -> {
            if (hasFocus) {
                input.setText(input.getText().replaceAll("[^\\d.]", ""));
            } else {
                formatCurrencyInput(input);
//                updateProgress();  // 仅在失去焦点时更新进度
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
}