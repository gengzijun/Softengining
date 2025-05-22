package com.example.app.main;

import com.example.app.model.TransactionRecord;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.nio.file.*;

import java.util.*;


import com.example.app.history.HistoryPage;
import com.example.app.chart.ChartPage;
import com.example.app.Main;

import java.math.BigDecimal;
public class MainPage {

    private final Stage stage;
    private final Scene scene;
    private TableView<Expense> table;
    private ObservableList<Expense> data;
    private HistoryPage historypage; // 供跳转
    private ChartPage chartPage; // 供跳转
    private final double MONTHLY_BUDGET = 2000.00;
    private Button warningLabel;
    private static final String DATA_FILE = "data.csv";

    public MainPage(Stage stage) {
        this.stage = stage;
        this.scene = buildMainScene();
    }

    public Scene getScene() {
        return scene;
    }

    public void setPageTwo(HistoryPage historypage) {
        this.historypage = historypage;
    }

    public void setChartPage(ChartPage chartPage) {
        this.chartPage = chartPage;
    }

    private Scene buildMainScene() {

        /* ——— 左侧导航栏 ——— */
        Label appTitle = new Label("TallyAPP");
        appTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox.setMargin(appTitle, new Insets(10, 0, 20, 10));

        Button chartBtn = createIconButton("/images/chart.png", "Chart", 20, 160, 20);
        Button detailsBtn = createIconButton("/images/details.png", "Details", 20, 160, 20);
        Button historyBtn = createIconButton("/images/history.png", "history", 32, 10, 10);
        historyBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        chartBtn.setOnAction(e -> {
            if (chartPage != null) {
                stage.setScene(chartPage.getScene());
                stage.setTitle("chart page");
            }
        });

        historyBtn.setOnAction(e -> {
            if (historypage != null) {
                stage.setScene(historypage.getScene());
            }
        });

        VBox sideMenu = new VBox(10, appTitle, chartBtn, detailsBtn, historyBtn);
        sideMenu.setPadding(new Insets(10));
        sideMenu.setAlignment(Pos.TOP_CENTER);
        sideMenu.setStyle("-fx-background-color: rgb(238,223,207);");
        sideMenu.setPrefWidth(170);

        // 顶部搜索栏
        TextField searchField = new TextField();
        searchField.setPromptText("Search transactions...");
        searchField.setPrefWidth(600);

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All", "Shopping", "Catering", "Living", "Entertainment", "Contingency", "Income");
        typeFilter.setValue("All");

        HBox searchBar = new HBox(10, searchField, typeFilter);
        searchBar.setPadding(new Insets(10, 0, 10, 0));
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // 按钮栏
        Label defaultBook = new Label("Default Book");
        defaultBook.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button exportBtn = createIconButton("/images/export.png", "export", 32, 10, 10);
        exportBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Button importBtn = createIconButton("/images/import.png", "import", 32, 10, 10);
        importBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        warningLabel = createIconButton("/images/warning.png", "warning", 32, 10, 10);
        warningLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        HBox topInfoBar = new HBox(
                new HBox(defaultBook),
                new HBox(historyBtn),
                new Region(),
                new HBox(5, exportBtn, warningLabel, importBtn));
        HBox.setHgrow(topInfoBar.getChildren().get(1), Priority.ALWAYS);
        topInfoBar.setPadding(new Insets(5, 0, 5, 0));
        topInfoBar.setAlignment(Pos.CENTER_LEFT);

        // 表格
        table = new TableView<>();
        data = FXCollections.observableArrayList();
        loadDataFromFile(); // 登录后加载数据
        showRecentRecords(); // 只展示最近记录

        TableColumn<Expense, String> taskCol = new TableColumn<>("Task");
        taskCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getIndex() < 0) {
                    setText(null);
                } else {
                    setText("Ex-" + (getTableRow().getIndex() + 1));
                }
            }
        });
        TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> c.getValue().dateProperty());
        dateCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");
        TableColumn<Expense, String> locationCol = new TableColumn<>("Detail");
        locationCol.setCellValueFactory(c -> c.getValue().contentProperty());
        locationCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");
        TableColumn<Expense, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.isEmpty()) {
                    setText(null);
                    setStyle("");
                } else {
                    Expense expense = getTableView().getItems().get(getIndex());
                    String type = expense.getType();

                    try {
                        double value = Double.parseDouble(item);

                        if ("Income".equalsIgnoreCase(type)) {
                            setText("￥" + item);
                            setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: green;");
                        } else {
                            if (value > 1000) {
                                setText("-￥" + item + "              ⚠");
                            } else {
                                setText("-￥" + item);
                            }
                            setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");
                        }

                    } catch (NumberFormatException e) {
                        setText(item);
                        setStyle("-fx-text-fill: gray;");
                    }
                }
            }
        });

        amountCol.setCellValueFactory(c -> c.getValue().amountProperty());

        table.setEditable(true); // 开启表格编辑
        TableColumn<Expense, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> c.getValue().typeProperty());
        typeCol.setCellFactory(ComboBoxTableCell.forTableColumn(
                "Shopping", "Catering", "Living", "Entertainment", "Contingency", "Income"));
        typeCol.setOnEditCommit(event -> {
            Expense expense = event.getRowValue();
            expense.setType(event.getNewValue());
            saveDataToFile(); // 你可加上 setType 方法
        });

        typeCol.setCellValueFactory(c -> c.getValue().typeProperty());
        typeCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");
        table.getColumns().addAll(taskCol, locationCol, typeCol, amountCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(data);

        VBox centerContent = new VBox(10, searchBar, topInfoBar, table);
        centerContent.setStyle("-fx-background-color: white");
        centerContent.setPadding(new Insets(10));
        VBox.setVgrow(table, Priority.ALWAYS); // 让表格垂直扩展

        // 搜索过滤
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTable(newVal, typeFilter.getValue()));
        typeFilter.setOnAction(e -> filterTable(searchField.getText(), typeFilter.getValue()));

        // 导出导入
        exportBtn.setOnAction(e -> exportDataToCSV());
        importBtn.setOnAction(e -> importDataFromCSV());

        BorderPane root = new BorderPane();
        root.setLeft(sideMenu);
        root.setCenter(centerContent);
        return new Scene(root, 1200, 900);
    }

    private void filterTable(String keyword, String type) {
        ObservableList<Expense> filtered = FXCollections.observableArrayList();
        for (Expense e : data) {
            boolean matchKeyword = keyword.isEmpty() ||
                    e.getContent().toLowerCase().contains(keyword.toLowerCase()) ||
                    e.getType().toLowerCase().contains(keyword.toLowerCase());
            boolean matchType = type.equals("All") || e.getType().equalsIgnoreCase(type);
            if (matchKeyword && matchType)
                filtered.add(e);
        }
        table.setItems(filtered);
    }

    private void exportDataToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Date,Detail,Amount,Type\n");
                for (Expense e : data) {
                    String formattedAmount = "￥" + e.getAmount();
                    writer.write(String.format("%s,%s,%s,%s\n",
                            e.getDate(), e.getContent(), formattedAmount, e.getType()));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void importDataFromCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import CSV Files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null && !files.isEmpty()) {
            ObservableList<Expense> importedData = FXCollections.observableArrayList();

            // 显示加载提示框
            Alert loadingAlert = new Alert(AlertType.INFORMATION);
            loadingAlert.setTitle("AI识别中");
            loadingAlert.setHeaderText("AI正在识别交易记录，请稍候...");
            loadingAlert.setContentText("识别过程中请勿进行其他操作");
            loadingAlert.getButtonTypes().setAll(ButtonType.CLOSE);
            loadingAlert.show();

            // 在后台线程中处理文件以避免阻塞UI线程
            new Thread(() -> {
                Platform.runLater(() -> {
                    data.clear(); // 清空主数据列表
                    table.setItems(data); // 更新表格显示
                });

                for (File file : files) {
                    String filepath = file.getAbsolutePath();
                    List<TransactionRecord> records = Main.processFile(filepath);

                    // 遍历 records 并构建 Expense 对象
                    for (TransactionRecord record : records) {
                        // 从 TransactionRecord 中提取相关信息
                        LocalDateTime transactionTime = record.getTransactionTime();
                        String detail = record.getTransactionObject();
                        BigDecimal amount = record.getAmount();
                        String type = record.getAiCategory(); // 使用 AI 分类结果作为类型
                        // 将 LocalDateTime 格式化为字符串，并将 'T' 替换为空格
                        String date = transactionTime.toString().replace('T', ' ');
                        String amountStr = amount.toString(); // 如果需要特定格式，可以格式化金额
                        // 假设 Expense 构造函数接受 date、detail、amount、type 参数
                        Expense e = new Expense(date, detail, amountStr, type);
                        Platform.runLater(() -> importedData.add(e));
                    }
                }

                // 完成所有文件处理后，通知用户
                Platform.runLater(() -> {
                    loadingAlert.close(); // 关闭加载提示框
                    data.addAll(importedData); // 将导入的数据添加到主数据列表
                    savePredictedDataToCSV(importedData);
                    saveDataToFile();
                    table.setItems(data);
                    updateTotalAndWarning();

                    // 显示完成提示
                    Alert completeAlert = new Alert(AlertType.INFORMATION);
                    completeAlert.setTitle("完成");
                    completeAlert.setHeaderText("AI识别完成！");
                    completeAlert.setContentText("交易记录已成功导入并分类");
                    completeAlert.showAndWait();
                });
            }).start();
        }
    }

    private void updateTotalAndWarning() {
        double total = 0.0;
        for (Expense e : data) {
            try {
                total += Double.parseDouble(e.getAmount());
            } catch (NumberFormatException ignored) {
            }
        }

        ImageView imageView;
        if (total > MONTHLY_BUDGET) {
            Image image = new Image(getClass().getResourceAsStream("/images/warning_red.png"));
            imageView = new ImageView(image);
        } else {
            Image image = new Image(getClass().getResourceAsStream("/images/warning.png"));
            imageView = new ImageView(image);
        }

        // 强制设置图标显示尺寸为32x32（与原图一致）
        imageView.setFitWidth(32); // 宽度固定为32像素
        imageView.setFitHeight(32); // 高度固定为32像素
        imageView.setPreserveRatio(true); // 保持宽高比，防止变形

        warningLabel.setGraphic(imageView);
    }

    private Button createIconButton(String imagePath, String text, int imageSize, int btnWidth, int btnHeight) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        imageView.setPreserveRatio(true); // 保持宽高比，防止变形
        Button button = new Button(text, imageView);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setTooltip(new Tooltip(text));
        button.setPrefSize(btnWidth, btnHeight);
        button.setStyle("-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #FFFFFF;" +
                "-fx-border-radius: 5;" +
                "-fx-font-size: 13px;" +
                "-fx-alignment: CENTER_LEFT;" +
                "-fx-padding: 5 10 5 10;");
        return button;
    }

    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            writer.write("Date,Detail,Amount,Type\n");
            for (Expense e : data) {
                writer.write(String.format("%s,%s,%s,%s\n", e.getDate(), e.getContent(), e.getAmount(), e.getType()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists())
            return;

        ObservableList<Expense> loaded = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                String[] fields = line.split(",", -1);
                if (fields.length >= 4) {
                    String date = fields[0].trim();
                    String detail = fields[1].trim();
                    String amount = fields[2].trim().replace("￥", "");
                    String type = fields[3].trim();
                    loaded.add(new Expense(date, detail, amount, type));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        data.addAll(loaded);
        showRecentRecords(); // 登录后展示最近几条
    }

    private void showRecentRecords() {
        int showCount = 5;
        int size = data.size();
        if (size <= showCount)
            return;

        ObservableList<Expense> recent = FXCollections.observableArrayList();
        for (int i = size - showCount; i < size; i++) {
            recent.add(data.get(i));
        }

        table.setItems(recent); // 仅显示最近几条
    }


    private void savePredictedDataToCSV(List<Expense> expenses) {
        // 创建一个映射，键为月份（格式为 YYYYMM），值为该月份的 Expense 列表
        Map<String, List<Expense>> expensesByMonth = new HashMap<>();

        for (Expense expense : expenses) {
            // 解析日期字符串为 LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(expense.getDate(), formatter);

            // 格式化为 YYYYMM
            String month = dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));

            // 将 Expense 添加到对应月份的列表中
            expensesByMonth.computeIfAbsent(month, k -> new ArrayList<>()).add(expense);
        }

        // 获取历史数据目录
        String directoryPath = "src/main/resources/history";
        File directory = new File(directoryPath);

        // 如果目录不存在，则创建目录
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 遍历每个月份的数据并保存到对应的 CSV 文件
        for (Map.Entry<String, List<Expense>> entry : expensesByMonth.entrySet()) {
            String month = entry.getKey();
            List<Expense> monthExpenses = entry.getValue();
            String fileName = month + ".csv";
            Path filePath = Paths.get(directoryPath, fileName);

            // 写入表头和数据
            try {
                boolean fileExists = Files.exists(filePath);
                try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                    if (!fileExists) {
                        writer.write("Task,Date,Detail,Amount,Type\n");
                    }

                    int i = 1;
                    for (Expense e : monthExpenses) {
                        writer.write(String.format("%s,%s,%s,%s,%s\n",
                                "Ex-" + i, e.getDate(), e.getContent(), e.getAmount(), e.getType()));
                        i++;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();

                // 提示用户保存失败
                Alert saveErrorAlert = new Alert(Alert.AlertType.ERROR);
                saveErrorAlert.setTitle("保存失败");
                saveErrorAlert.setHeaderText("无法保存预测数据");
                saveErrorAlert.setContentText("错误信息: " + ex.getMessage());
                saveErrorAlert.showAndWait();
            }

            // 计算统计信息
            MonthSummary summary = calculateSummary(monthExpenses);

            // 保存统计信息到一个新的 CSV 文件
            saveSummaryToCSV(summary, month);
        }
    }

    private MonthSummary calculateSummary(List<Expense> expenses) {
        MonthSummary summary = new MonthSummary();

        for (Expense expense : expenses) {
            double amount = Double.parseDouble(expense.getAmount());
            String type = expense.getType();

            if (type.equalsIgnoreCase("Income")) {
                summary.totalIncome += amount;
            } else {
                summary.totalExpenses += amount;
                summary.expensesByType.put(type, summary.expensesByType.getOrDefault(type, 0.0) + amount);
            }
        }

        return summary;
    }

    private void saveSummaryToCSV(MonthSummary summary, String month) {
        String directoryPath = "src/main/resources/summaries";
        File directory = new File(directoryPath);

        // 如果目录不存在，则创建目录
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = month + "_summary.csv";
        Path filePath = Paths.get(directoryPath, fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE)) {
            // 写入表头
            writer.write("Category,Amount\n");

            // 写入总收入和总支出
            writer.write(String.format("Total Income,%s\n", summary.totalIncome));
            writer.write(String.format("Total Expenses,%s\n", summary.totalExpenses));

            // 写入各个类型的支出
            for (Map.Entry<String, Double> entry : summary.expensesByType.entrySet()) {
                writer.write(String.format("%s,%s\n", entry.getKey(), entry.getValue()));
            }

            // 提示用户统计信息已保存
            Alert saveAlert = new Alert(Alert.AlertType.INFORMATION);
            saveAlert.setTitle("保存成功");
            saveAlert.setHeaderText("统计信息已成功保存");
            saveAlert.setContentText("文件路径: " + filePath.toString());
            saveAlert.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();

            // 提示用户保存失败
            Alert saveErrorAlert = new Alert(Alert.AlertType.ERROR);
            saveErrorAlert.setTitle("保存失败");
            saveErrorAlert.setHeaderText("无法保存统计信息");
            saveErrorAlert.setContentText("错误信息: " + ex.getMessage());
            saveErrorAlert.showAndWait();
        }
    }

    public class MonthSummary {
        public double totalIncome = 0.0; // 总收入
        public double totalExpenses = 0.0; // 总支出
        public Map<String, Double> expensesByType = new HashMap<>(); // 各类支出
    }
}
