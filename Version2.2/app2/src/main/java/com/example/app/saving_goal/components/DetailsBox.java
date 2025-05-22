package com.example.app.saving_goal.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.NumberFormat;
import java.util.*;

public class DetailsBox extends VBox {
    private List<String[]> mergedData;  // 用来存储合并的CSV数据
    // 添加数据更新监听器
    private List<Runnable> updateListeners = new ArrayList<>();
    // Define style for ComboBox elements
    // 已修改
    private static final String COMBO_BOX_STYLE =
            "-fx-background-color: white;" +
                    "-fx-border-color: #D3D3D3;" +
                    "-fx-border-width: 1px;" +
                    "-fx-background-radius: 5px;" +
                    "-fx-border-radius: 5px;" +
                    "-fx-padding: 1 5 1 5;" +      // 上下padding减少
                    "-fx-pref-width: 1400;" +
                    "-fx-pref-height: 22;" +       // 高度降低
                    "-fx-font-size: 12px;" +
                    "-fx-cell-size: 20px;";

    private ScrollPane scrollPane;
    private double cumulativeSaving_end = 0.0;  // 存储累计存款

    public DetailsBox(List<String[]> mergedData) {
        this.mergedData = mergedData;  // 初始化 mergedData
        initialize();
    }

    private void initialize() {
        // Set the spacing, alignment, and padding for the container
        setSpacing(3);
        setAlignment(javafx.geometry.Pos.TOP_LEFT);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-background-color: white;");  // Set background to white

        // Create the title label
        Label titleLabel = createTitleLabel();

        // Create the ScrollPane to contain the month sections
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Create sections for each month (October, September, August)
        VBox monthSections = new VBox(3);  // Use VBox to contain month sections

        // Create sections for the first two months
        VBox month202410 = createMonthSection("2024-10", createTextArea(), true);
        VBox month20249 = createMonthSection("2024-9", createTextArea(), false);

        // Add first two months to the VBox
        monthSections.getChildren().addAll(month202410, month20249);
        scrollPane.setPadding(new Insets(0)); // ✅ 清除内边距
        // Add ScrollPane to hold the monthSections VBox
        scrollPane.setContent(monthSections);
        // Now, we can set the background color of the content inside ScrollPane
        scrollPane.setStyle("-fx-background-color: white;");  // Set the ScrollPane background to white
        scrollPane.getContent().setStyle("-fx-background-color: white;");  // Set the background of the content area

        // Add the title and ScrollPane to the layout
        getChildren().addAll(titleLabel, scrollPane);
    }

    public double getCumulativeSavings() {
        return cumulativeSaving_end;  // 返回累计存款值
    }

    // Create the title label for the details section
    private Label createTitleLabel() {
        Label label = new Label("Details");
        label.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        return label;
    }

    // Create a section for each month with content (either a TextArea or ComboBox)
    // 已修改
    private VBox createMonthSection(String month, TextArea content, boolean isExpanded) {
        VBox container = new VBox(0); // 垂直间距设为0
        Label monthLabel = new Label(month);
        monthLabel.setFont(Font.font("Arial", 14));
        VBox.setMargin(monthLabel, new Insets(0, 0, 2, 0)); // 减少标签底部间距

        if (isExpanded) {
            container.getChildren().addAll(monthLabel, content);
        } else {
            VBox contentContainer = new VBox(content);
            contentContainer.setVisible(false);
            contentContainer.managedProperty().bind(contentContainer.visibleProperty()); // 关键：可见性绑定布局管理

            ComboBox<String> comboBox = createComboBox(contentContainer);
            HBox header = new HBox(0, comboBox); // HBox间距设为0
            header.setAlignment(Pos.CENTER_LEFT);

            container.getChildren().addAll(monthLabel, header, contentContainer);
        }

        applySectionMargin(container);
        return container;
    }

    // Create a non-editable TextArea for displaying saved amounts and other information
    private TextArea createTextArea() {
        TextArea textArea = new TextArea("Saved: ¥14,032\nAdd up: ¥49,200");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(1200);
        textArea.setPrefHeight(60);
        textArea.setStyle(
                "-fx-background-radius: 10 10 10 10;" +
                        "-fx-border-radius: 10 10 10 10;" +
                        "-fx-border-color: #D3D3D3;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: white;"
        );

        // Apply margin to the TextArea
        applyElementMargin(textArea);
        return textArea;
    }

    // Helper method to apply margin to sections (VBox)
    private void applySectionMargin(VBox section) {
        VBox.setMargin(section, new Insets(0, 0, 0, 10));
    }

    // Helper method to apply margin to individual elements (TextArea, ComboBox)
    private void applyElementMargin(javafx.scene.Node node) {
        VBox.setMargin(node, new Insets(0, 0, 0, 10));
    }

    private ComboBox<String> createComboBox(VBox contentContainer) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("More Details");
        comboBox.setValue("More Details"); // 默认显示文本
        comboBox.setStyle(COMBO_BOX_STYLE);

        // 添加样式覆盖下拉箭头右侧的空白
        comboBox.setStyle(COMBO_BOX_STYLE +
                "-fx-padding: 0 25 0 5;" + // 调整内边距，确保文本不被箭头遮挡
                "-fx-font-weight: normal;");

        // 点击箭头时展开内容
        comboBox.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                comboBox.hide(); // 阻止下拉列表弹出
                contentContainer.setVisible(!contentContainer.isVisible());
            }
        });
        return comboBox;
    }

    // 已修改
    public void updateData(int startYear, int endYear) {
        // 在方法开头初始化cumulativeSaving_end
        cumulativeSaving_end = 0.0;
        // 清空之前的数据
        VBox contentVBox = new VBox(3); // Creating new VBox to update with filtered data
        getChildren().clear();
        getChildren().add(createTitleLabel());
        getChildren().add(scrollPane); // Adding ScrollPane back

        // 根据年份范围筛选数据
        Map<String, Double> monthlySavings = filterAndCalculateMonthlySavings(startYear, endYear);

        if (monthlySavings.isEmpty()) {
            // 创建提示文本框
            TextArea emptyInfo = new TextArea("No transaction data available for the selected year range.");
            emptyInfo.setEditable(false);
            emptyInfo.setWrapText(true);
            emptyInfo.setPrefWidth(1200);
            emptyInfo.setPrefHeight(60);
            emptyInfo.setStyle(
                    "-fx-background-radius: 10px;" +
                            "-fx-border-radius: 10px;" +
                            "-fx-border-color: #D3D3D3;" +
                            "-fx-border-width: 1px;" +
                            "-fx-background-color: white;" +
                            "-fx-font-family: Arial;" +
                            "-fx-font-size: 12px;" +
                            "-fx-text-fill: #666666;"  // 灰色文字更友好
            );
            contentVBox.getChildren().add(emptyInfo);
        } else {

            // 步骤1：按时间升序排列月份（1月到12月）
            List<String> ascendingMonths = new ArrayList<>(monthlySavings.keySet());
            ascendingMonths.sort((s1, s2) -> {
                String[] p1 = s1.split("/");
                String[] p2 = s2.split("/");

                int yearCompare = Integer.compare(
                        Integer.parseInt(p1[0]),
                        Integer.parseInt(p2[0])
                );
                if (yearCompare != 0) return yearCompare;

                return Integer.compare(
                        Integer.parseInt(p1[1]),
                        Integer.parseInt(p2[1])
                );
            });

            // 步骤2：计算累计存款
            Map<String, Double> cumulativeSavings = new LinkedHashMap<>();
            double runningTotal = 0.0;
            for (String month : ascendingMonths) {
                runningTotal += monthlySavings.get(month);
                cumulativeSavings.put(month, runningTotal);
            }

            List<String> displayMonths = new ArrayList<>(ascendingMonths);
            Collections.reverse(displayMonths);

            for (int i = 0; i < displayMonths.size(); i++) {
                String month = displayMonths.get(i);
                double monthlySave = monthlySavings.get(month);
                double totalSave = cumulativeSavings.get(month);

                // 生成带最新数据的 TextArea
                TextArea textArea = createTextAreaForMonth(monthlySave, totalSave);

                // 创建月份区块（第一个展开）
                VBox monthSection = createMonthSection(month, textArea, i == 0);
                contentVBox.getChildren().add(monthSection);
            }

            // 获取最后一个月的累计存款，并将其赋值给 cumulativeSaving_end
            if (!displayMonths.isEmpty()) {
                String lastMonth = ascendingMonths.get(ascendingMonths.size() - 1); // 正确：取升序列表的最后一个元素（如12月）
                cumulativeSaving_end = cumulativeSavings.get(lastMonth); // 获取并赋值累计存款
            }
        }
        contentVBox.setStyle("-fx-background-color: white;");
        // 确保 ScrollPane 背景颜色在更新后仍为白色
        scrollPane.setStyle("-fx-background-color: white;");
        // 配置滚动窗格
        scrollPane.setContent(contentVBox);
        scrollPane.setPrefViewportHeight(180); // 显示约2个月的高度
        scrollPane.setMinHeight(180);
        scrollPane.setMaxHeight(Double.MAX_VALUE); // 允许扩展

        // 触发所有监听器
        updateListeners.forEach(Runnable::run);

    }

    // 筛选并计算每个月的存款
    private Map<String, Double> filterAndCalculateMonthlySavings(int startYear, int endYear) {
        Map<String, Double> monthlySavings = new HashMap<>();
        // 跳过 CSV 文件的表头（假设从第二行开始读取）
        for (int i = 1; i < mergedData.size(); i++) {  // 从索引 1 开始
            String[] transaction = mergedData.get(i);

            // 获取交易时间，确保在选定的年份范围内
            String transactionTime = transaction[0]; // 日期
            if (transactionTime == null || transactionTime.length() < 4) {
                continue; // 跳过无效的日期字段
            }

            try {
                int transactionYear = Integer.parseInt(transactionTime.substring(0, 4));
                if (transactionYear >= startYear && transactionYear < endYear) {
                    String month = transactionTime.substring(0, 7); // 提取年月（例如2024-02）
                    String type = transaction[1]; // 类型
                    String amountStr = transaction[3]; // 金额

                    double amount = 0;
                    try {
                        // 尝试解析金额
                        amount = Double.parseDouble(amountStr);
                    } catch (NumberFormatException e) {
                        // 如果金额字段不是有效的数字，跳过该交易
                        System.out.println("Invalid amount: " + amountStr);  // 输出错误信息
                        continue;  // 跳过当前交易记录
                    }

                    // 根据交易类型计算存款（收入减去支出）
                    if (type.equals("Income")) {
                        monthlySavings.put(month, monthlySavings.getOrDefault(month, 0.0) + amount);
                    } else {
                        monthlySavings.put(month, monthlySavings.getOrDefault(month, 0.0) - amount);
                    }
                }
            } catch (NumberFormatException e) {
                // 发生异常时跳过该行
                System.out.println("Invalid year in transaction time: " + transactionTime);
                continue;
            }
        }
        return monthlySavings;
    }

    // Create a TextArea for each month displaying both "Saved" and "Add up"
    private TextArea createTextAreaForMonth(double savings, double cumulativeSavings) {
        TextArea textArea = new TextArea(
                "Saved: ¥" + NumberFormat.getInstance().format(savings) + "\n" +
                        "Add up: ¥" + NumberFormat.getInstance().format(cumulativeSavings)
        );
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(1200);
        textArea.setPrefHeight(60);
        textArea.setStyle(
                "-fx-background-radius: 10px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-color: #D3D3D3;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: white;" +
                        "-fx-font-family: Arial;" +
                        "-fx-font-size: 12px;"
        );
        return textArea;
    }
    public void addUpdateListener(Runnable listener) {
        updateListeners.add(listener);
    }

}



