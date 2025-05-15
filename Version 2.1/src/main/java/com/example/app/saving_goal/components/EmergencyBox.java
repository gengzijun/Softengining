package com.example.app.saving_goal.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class EmergencyBox extends VBox {

    private TextField inputField; // TextField for user input
    private double reservedAmount;
    private static final String CSV_DIR = "target/classes/Savinggoal/";
    private static final String CSV_PATH = CSV_DIR + "saving_data.csv";

    public EmergencyBox() {
        initialize(); // Initialize the layout of the EmergencyBox
    }

    private void initialize() {
        // Basic container setup
        setSpacing(10);
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px;");

        // Create the main container and add it to the EmergencyBox
        HBox mainContainer = createMainContainer();
        getChildren().add(mainContainer);
    }

    private HBox createMainContainer() {
        // Create the left section with icon and title
        HBox iconTitleSection = createIconTitleSection();

        // Create the right section with description and input field
        VBox inputSection = createInputSection();

        // Combine the left and right sections into a horizontal box
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().addAll(iconTitleSection, inputSection);

        return container;
    }

    private HBox createIconTitleSection() {
        // Load the icon and title for the section
        try {
            Image icon = new Image(new FileInputStream("src/main/resources/images/emoji1.png"));
            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(20);  // 替代原来的setFitSize(20,20)
            iconView.setFitHeight(20);

            Label title = new Label("Any Emergency");
            title.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 17));

            HBox section = new HBox(10);
            section.setAlignment(Pos.CENTER_LEFT);
            section.getChildren().addAll(iconView, title);
            return section;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Emergency icon not found", e);
        }
    }

    private VBox createInputSection() {
        // Create description text for the input section
        Label description = new Label(
                "If you have necessary additional or emergency expenses next month, " +
                        "you can input the approximate reserved amount and the AI will automatically " +
                        "adjust your consumption budget for you."
        );
        description.setFont(javafx.scene.text.Font.font(12));
        description.setWrapText(true);

        // 创建输入框和按钮的容器
        HBox inputContainer = new HBox(10);
        inputContainer.setAlignment(Pos.CENTER_LEFT);

        // Create the input field for emergency value
        inputField = new TextField();
        inputField.setPromptText("Value");
        inputField.setPrefWidth(1000);

        // 创建确定按钮
        Button confirmButton = new Button("Confirm");
        // 设置按钮样式
        confirmButton.setStyle("-fx-background-color: #82DD6E; -fx-text-fill: white; -fx-background-radius: 5px;");

        // 按钮点击事件
        confirmButton.setOnAction(e -> saveInputValue());

        // 将输入框和按钮添加到容器
        inputContainer.getChildren().addAll(inputField, confirmButton);

        VBox section = new VBox(10);
        section.getChildren().addAll(description, inputContainer); // 替换原来的inputField
        return section;
    }

    // 新增保存方法
    private void saveInputValue() {
        try {
            // 解析输入金额
            String input = inputField.getText().replaceAll("[^\\d.]", "");
            reservedAmount = Double.parseDouble(input);
            inputField.clear();

            // 读取现有CSV内容
            Path csvPath = Paths.get(CSV_PATH);
            List<String> lines = new ArrayList<>();

            // 读取现有文件（如果存在）
            if (Files.exists(csvPath)) {
                lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            }

            // 确保列头格式
            String header = "target,haveSaved,stillNeed,startYear,endYear,emergency_amount";
            if (lines.isEmpty() || !lines.get(0).equals(header)) {
                lines.clear();
                lines.add(header);
            }

            // 处理数据行（兼容旧数据）
            String[] columns;
            if (lines.size() > 1) {
                columns = lines.get(1).split(",");
            } else {
                columns = new String[]{
                        String.valueOf(ProgressBox.DEFAULT_AMOUNT),
                        "0.00",
                        String.valueOf(ProgressBox.DEFAULT_AMOUNT),
                        "2022",
                        "2022",
                        "0.00" // 初始紧急金额
                };
            }

            // 确保列数正确（覆盖第6列）
            if (columns.length < 6) {
                columns = Arrays.copyOf(columns, 6);
                columns[5] = "0.00"; // 填充默认值
            }
            columns[5] = String.format(Locale.US, "%.2f", reservedAmount);

            // 重建数据行
            String newDataLine = String.join(",", columns);

            // 更新数据行
            if (lines.size() > 1) {
                lines.set(1, newDataLine);
            } else {
                lines.add(newDataLine);
            }

            // 写回文件
            Files.write(csvPath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (NumberFormatException ex) {
            inputField.setStyle("-fx-border-color: red;");
            new Timeline(new KeyFrame(Duration.seconds(1),
                    e -> inputField.setStyle("-fx-border-color: null;"))).play();
        } catch (IOException e) {
            System.err.println("CSV保存失败: " + e.getMessage());
        }
    }
}