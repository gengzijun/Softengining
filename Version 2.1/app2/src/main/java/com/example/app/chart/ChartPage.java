package com.example.app.chart;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.example.app.main.MainPage;
import com.example.app.saving_goal.SavingGoalPage;


public class ChartPage {
    private final Stage stage;
    private MainPage mainPage;
    private SavingGoalPage savingGoalPage;
    private final Scene scene;

    private BorderPane root;
    private SimpleDoubleProperty fontSizeFactor = new SimpleDoubleProperty(1.0);
    private String currentView = "month";


    private static class Record {
        String task;
        String date;
        String detail;
        double amount;
        String type;
    
        Record(String task, String date, String detail, double amount, String type) {
            this.task = task;
            this.date = date;
            this.detail = detail;
            this.amount = amount;
            this.type = type;
        }
    }
    public ChartPage(Stage stage) {
        this.stage = stage;
        this.scene = buildScene();
    }

    public Scene getScene() {
        return scene;
    }

    public void setMainPage(MainPage m) {
        this.mainPage = m;
    }

    public void setSavingGoalPage(SavingGoalPage p) {
        this.savingGoalPage = p;
    }

    private Scene buildScene() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        Label lblTitle = new Label("Tally App");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;");

        Button btnPrevious = new Button("< Previous");
        Button btnMonth = new Button("Month");
        Button btnQuarter = new Button("Quarter");
        Button btnYear = new Button("Year");
        Button btnSavingGoal = new Button("Saving Goal");

        btnPrevious.setOnAction(e -> {
            if (mainPage != null) {
                stage.setScene(mainPage.getScene());
                stage.setTitle("main page");
            }
        });

        btnSavingGoal.setOnAction(e -> {
            if (savingGoalPage != null) {
                stage.setScene(savingGoalPage.getScene());
                stage.setTitle("saving goal page");
            }
        });

        addScaleAnimation(btnMonth);
        addScaleAnimation(btnQuarter);
        addScaleAnimation(btnYear);
        addScaleAnimation(btnSavingGoal);

        btnMonth.setOnAction(e -> switchView("month", btnMonth, btnQuarter, btnYear));
        btnQuarter.setOnAction(e -> switchView("quarter", btnMonth, btnQuarter, btnYear));
        btnYear.setOnAction(e -> switchView("year", btnMonth, btnQuarter, btnYear));

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #F4E1D2; -fx-padding: 10px;");
        HBox leftBox = new HBox(btnPrevious);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox rightBox = new HBox(lblTitle);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        topBar.getChildren().addAll(leftBox, rightBox);

        // 给 Saving Goal 设置样式 & 动效
        btnSavingGoal.setStyle(buttonStyle("saving"));
        addScaleAnimation(btnSavingGoal);

        // 左边按钮区域
        HBox buttonRow = new HBox(30, btnMonth, btnQuarter, btnYear);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        // 右边按钮区域，留出右边距
        HBox rightButtonBox = new HBox(btnSavingGoal);
        rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
        rightButtonBox.setPadding(new Insets(0, 40, 0, 0)); // 距右边 40px

        // 用 BorderPane 实现左右分布
        BorderPane buttonBar = new BorderPane();
        buttonBar.setLeft(buttonRow);
        buttonBar.setRight(rightButtonBox);
        buttonBar.setPadding(new Insets(10, 40, 10, 40)); // 整体左右都有间距

        // 顶部组合
        VBox topScreen = new VBox(topBar, buttonBar);
        root.setTop(topScreen);

        switchView("month", btnMonth, btnQuarter, btnYear);

        return new Scene(root, 1400, 900);
    }
    private Map<String, Double> loadBudgetRecommendation() {
        Map<String, Double> prediction = new HashMap<>();
//        String latestFile = findLatestFileName("/predicate", "predicate");
//        if (latestFile == null) {
//            return prediction;
//        }

        String filePath = "/prediction/budget_recommendation.csv";
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            if (inputStream == null) {
                System.out.println("Prediction file not found: " + filePath);
                return prediction;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] fields = line.split(",", 2);
                if (fields.length == 2) {
                    String category = fields[0].trim();
                    String amountStr = fields[1].trim();
                    try {
                        double amount = Double.parseDouble(amountStr);
                        prediction.put(category, amount);
                    } catch (NumberFormatException e) {
                        // 忽略非数字字段，例如 suggestion
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prediction;
    }

    private void switchView(String view, Button btnMonth, Button btnQuarter, Button btnYear) {
        currentView = view;
        btnMonth.setStyle(buttonStyle("month"));
        btnQuarter.setStyle(buttonStyle("quarter"));
        btnYear.setStyle(buttonStyle("year"));

        VBox content = new VBox(10);
        content.getChildren().addAll(
                createCards(),
                createCharts(view),
                createBottomSection());
        content.setPadding(new Insets(0, 60, 20, 60)); // bottom 20px padding
        root.setCenter(content);
    }

    private String buttonStyle(String type) {
        String base = "-fx-text-fill: white; -fx-background-radius: 10px; -fx-pref-width: 90px; -fx-pref-height: 40px;";
        String bg = type.equals(currentView) ? "#FFA500" : "#000000";
        return "-fx-background-color: " + bg + ";" + base;
    }

    private HBox createCards() {
        double income = 0, expenses = 0;
        List<Record> summary = loadRecordsFromCsv(currentView.equals("month") ? "summary_month" : currentView);

        for (Record r : summary) {
            if (r.task.equalsIgnoreCase("Total Income")) {
                income = r.amount;
            } else if (r.task.equalsIgnoreCase("Total Expenses")) {
                expenses = r.amount;
            }
        }

        Map<String, Double> budgetRecommendation = loadBudgetRecommendation();
        double predictedIncome = budgetRecommendation.getOrDefault("predicted_income", 0.0);
        double predictedExpenses = budgetRecommendation.getOrDefault("predicted_total_expense", 0.0);
        double predictedBudget = predictedIncome - predictedExpenses;

        VBox expenditure = createCard("Expenditure", "¥ " + String.format("%.2f", expenses), "¥ "+ String.format("%.2f", predictedExpenses), Color.RED);
        VBox incomeCard = createCard("Income", "¥ " + String.format("%.2f", income), "¥ "+  String.format("%.2f", predictedIncome), Color.GREEN);
        VBox budgetCard = createCard("Budget", String.format("%.2f", income - expenses), "¥ "+  String.format("%.2f", predictedExpenses), Color.BLACK);

        HBox hbox = new HBox(30, expenditure, incomeCard, budgetCard);
        hbox.setPadding(new Insets(10, 60, 10, 60));
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(expenditure, Priority.ALWAYS);
        HBox.setHgrow(incomeCard, Priority.ALWAYS);
        HBox.setHgrow(budgetCard, Priority.ALWAYS);
        expenditure.setMaxWidth(Double.MAX_VALUE);
        incomeCard.setMaxWidth(Double.MAX_VALUE);
        budgetCard.setMaxWidth(Double.MAX_VALUE);

        return hbox;
    }

    private String findLatestFileName(String directory, String type) {
        try {
            // 使用 ClassLoader 获取资源路径
            URL url = getClass().getResource(directory);
            if (url == null) {
                System.out.println("No directory found: " + directory);
                return null;
            }
    
            File folder = new File(url.toURI()); // 把URL转成File
            File[] files = folder.listFiles();
    
            if (files == null || files.length == 0) {
                System.out.println("No files found in " + directory);
                return null;
            }
    
            int latestDate = -1;
            String latestFileName = null;
    
            for (File file : files) {
                String name = file.getName();
                if (name.endsWith(".csv") && name.length() >= 10) { // 例如 202402.csv
                    try {
                        int date = Integer.parseInt(name.substring(0, 6)); // 提取 202401
                        if (date > latestDate) {
                            latestDate = date;
                            latestFileName = name;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Filename format error: " + name);
                    }
                }
            }
    
            if (latestFileName != null) {
                System.out.println("Latest " + type + " file: " + latestFileName);
            }
            return latestFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    private List<Record> loadRecordsFromCsv(String view) {
        List<Record> records = new ArrayList<>();
        String filePath = "";
    
        if (view.equals("month")) {
            String latestHistory = findLatestFileName("/history", "history");
            if (latestHistory != null) {
                filePath = "/history/" + latestHistory;
            }
        } else if (view.equals("quarter")) {
            String latestSummary = findLatestFileName("/summaries", "summary");
            if (latestSummary != null) {
                filePath = "/summaries/" + latestSummary;
            }
        } else if (view.equals("year")) {
            filePath = "/summaries/2024_summary.csv"; // 年度汇总还是固定
        } else if (view.equals("summary_month")) {
            String latestSummary = findLatestFileName("/summaries", "summary");
            if (latestSummary != null) {
                filePath = "/summaries/" + latestSummary;
            }
        }
    
        if (filePath.isEmpty()) {
            System.out.println("No valid file path found for view = " + view);
            return records;
        }
    
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            if (inputStream == null) {
                System.out.println("File not found: " + filePath);
                return records;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length == 2) {
                    records.add(new Record(fields[0].trim(), "", "", Double.parseDouble(fields[1].trim()), ""));
                } else if (fields.length >= 5) {
                    records.add(new Record(fields[0], fields[1], fields[2], Double.parseDouble(fields[3]), fields[4]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
    
    
    

    private HBox createCharts(String view) {
    List<Record> records = loadRecordsFromCsv(view);

    NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel("Day");

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Amount (¥)");

    LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
    XYChart.Series<Number, Number> series = new XYChart.Series<>();

    // 【新增】按日期累计支出金额
    Map<String, Double> dailyExpenseMap = new TreeMap<>(); // TreeMap默认按日期升序

    for (Record r : records) {
        if (!r.type.equalsIgnoreCase("Income")) {
            dailyExpenseMap.put(r.date, dailyExpenseMap.getOrDefault(r.date, 0.0) + r.amount);
        }
    }

    int dayIndex = 1;
    for (Map.Entry<String, Double> entry : dailyExpenseMap.entrySet()) {
        series.getData().add(new XYChart.Data<>(dayIndex++, entry.getValue()));
    }

    lineChart.getData().add(series);
    lineChart.setLegendVisible(false);
    lineChart.setCreateSymbols(false);
    lineChart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
 
        Map<String, Double> typeToAmount = new HashMap<>();
        for (Record r : records) {
            if (!r.type.equalsIgnoreCase("Income")) {
                typeToAmount.put(r.type, typeToAmount.getOrDefault(r.type, 0.0) + r.amount);
            }
        }

        PieChart pieChart = new PieChart();
        for (Map.Entry<String, Double> entry : typeToAmount.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        pieChart.setTitle("Category Breakdown");
        pieChart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox lineChartBox = new VBox(lineChart);
        lineChartBox.setPadding(new Insets(10));
        lineChartBox.setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 1; -fx-background-color: white;");

        VBox pieChartBox = new VBox(pieChart);
        pieChartBox.setPadding(new Insets(10));
        pieChartBox.setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 1; -fx-background-color: white;");

        HBox charts = new HBox(20, lineChartBox, pieChartBox);
        charts.setPadding(new Insets(10, 60, 10, 60));
        charts.setAlignment(Pos.CENTER);
        HBox.setHgrow(lineChartBox, Priority.ALWAYS);
        HBox.setHgrow(pieChartBox, Priority.ALWAYS);

        return charts;
    }
    private String loadSuggestionFromCsv() {
        StringBuilder suggestionText = new StringBuilder();
//        String latestFile = findLatestFileName("/predicate", "predicate");
//        if (latestFile == null) {
//            return "";
//        }

        String filePath = "/prediction/budget_recommendation.csv";
        boolean inSuggestion = false;
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            if (inputStream == null) {
                System.out.println("Suggestion file not found: " + filePath);
                return "";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.startsWith("suggestion,")) {
                    inSuggestion = true;
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        suggestionText.append(parts[1].trim());
                        suggestionText.append("\n");
                    }
                } else if (inSuggestion) {
                    suggestionText.append(line.trim());
                    suggestionText.append("\n");
                }
            }

            String result = suggestionText.toString().trim();

            // ！！！核心补充：把"\n"字符变成真实换行！！！
            result = result.replace("\\n", "\n");

            // 去除首尾可能存在的```符号
            if (result.startsWith("```") && result.endsWith("```")) {
                result = result.substring(3, result.length() - 3).trim();
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private VBox createSuggestionSection() {
        VBox suggestionBox = new VBox(10);
        suggestionBox.setPadding(new Insets(15));
        suggestionBox.setStyle("-fx-background-color: #f3c88b; -fx-border-color: #f3c88b; -fx-border-width: 1px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        Label title = new Label("AI Recommendation");
        title.setFont(new Font("Arial", 16));
        title.setTextFill(Color.GRAY);
        suggestionBox.getChildren().add(title);

        // 加载 suggestion 文本
        String suggestion = loadSuggestionFromCsv();
        if (suggestion.isEmpty()) {
            suggestion = "No suggestion available.";
        }

        Label suggestionContent = new Label(suggestion);
        suggestionContent.setFont(new Font("Arial", 16));
        suggestionContent.setWrapText(true);  // 必须开启换行
        suggestionContent.setTextFill(Color.DIMGRAY);
        suggestionContent.setMaxWidth(Double.MAX_VALUE); // 不固定500了
        suggestionContent.setPrefWidth(Double.MAX_VALUE); // 自动充满可用空

        ScrollPane scrollPane = new ScrollPane(suggestionContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportWidth(500);  // Scroll区域大小
        scrollPane.setPrefViewportHeight(300); // 高度可以调大一点
        scrollPane.setStyle("-fx-background: #f3c88b;");

        suggestionBox.getChildren().add(scrollPane);

        return suggestionBox;
    }
    private HBox createBottomSection() {
        // 左侧排行榜 Rank List
    VBox rankList = new VBox(20);
    rankList.setPadding(new Insets(15));
    rankList.setStyle(
            "-fx-background-color: white; " +
                    "-fx-border-color: #e0e0e0; " +
                    "-fx-border-width: 1px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
    rankList.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    Label rankTitle = new Label("Rank");
    rankTitle.setFont(new Font("Arial", 20));
    rankTitle.setTextFill(Color.GRAY);
    rankList.getChildren().add(rankTitle);

    // 读取 summaries，生成排行榜
    List<Record> summaryRecords = loadRecordsFromCsv(currentView.equals("month") ? "summary_month" : currentView);
    Map<String, Double> filtered = new HashMap<>();

    for (Record r : summaryRecords) {
        if (!r.task.equalsIgnoreCase("Total Income") && !r.task.equalsIgnoreCase("Total Expenses")) {
            filtered.put(r.task, r.amount);
        }
    }

    List<Map.Entry<String, Double>> top5 = filtered.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toList());

    Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.PURPLE };
    int colorIndex = 0;
    Map<String, Double> budgetRecommendation = loadBudgetRecommendation();
    Map<String, Double> predictedAmountMap = new HashMap<>();
        predictedAmountMap.put("living", budgetRecommendation.getOrDefault("living", 0.0));
        predictedAmountMap.put("entertainment", budgetRecommendation.getOrDefault("entertainment", 0.0));
        predictedAmountMap.put("catering", budgetRecommendation.getOrDefault("catering", 0.0));
        predictedAmountMap.put("shopping", budgetRecommendation.getOrDefault("shopping", 0.0));
    for (Map.Entry<String, Double> entry : top5) {
        HBox rankItem = new HBox(10);
        rankItem.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(10, colors[colorIndex % colors.length]);

        Label typeLabel = new Label("type: " + entry.getKey());
        typeLabel.setFont(new Font("Arial", 18));
        typeLabel.setTextFill(Color.GRAY);
        typeLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(typeLabel, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label amountLabel = new Label("¥ " + String.format("%.2f", entry.getValue()));
        amountLabel.setFont(new Font("Arial", 18));
        amountLabel.setTextFill(colors[colorIndex % colors.length]);
        amountLabel.setMaxWidth(Double.MAX_VALUE);
        amountLabel.setMinWidth(100); 
        HBox.setHgrow(amountLabel, Priority.ALWAYS);
        amountLabel.setAlignment(Pos.CENTER_RIGHT);

        Label arrowLabel = new Label("→");
        arrowLabel.setFont(new Font("Arial", 18));
        arrowLabel.setTextFill(Color.BLACK);

        String category = entry.getKey().toLowerCase();
        Double predictedAmount = predictedAmountMap.getOrDefault(category, 0.0);
        Label predictedLabel = new Label("¥ " + String.format("%.2f", predictedAmount));
        predictedLabel.setFont(new Font("Arial", 18));
        predictedLabel.setTextFill(colors[colorIndex % colors.length]);
        predictedLabel.setAlignment(Pos.CENTER_RIGHT);

        rankItem.getChildren().addAll(icon, typeLabel, spacer, amountLabel, arrowLabel, predictedLabel);
        rankList.getChildren().add(rankItem);
        VBox.setVgrow(rankItem, Priority.ALWAYS);
        colorIndex++;
    }

        VBox plan = new VBox(10);
        plan.setPadding(new Insets(15));
        plan.setStyle(
                "-fx-background-color: #f5f0e6; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        VBox suggestionBox = createSuggestionSection();
//        Label planTitle = new Label("Financial Optimization Plan (Given by Fairy)");
//        planTitle.setFont(new Font("Arial", 14));
//        planTitle.setTextFill(Color.GRAY);
//        plan.getChildren().add(planTitle);
//
//        Label strategyTitle = new Label("Overall Strategy");
//        strategyTitle.setFont(new Font("Arial", 12));
//        strategyTitle.setTextFill(Color.GRAY);
//        plan.getChildren().add(strategyTitle);
//
//        Label strategyContent = new Label(
//                suggestion);
//        strategyContent.setFont(new Font("Arial", 11));
//        strategyContent.setTextFill(Color.GRAY);
//        plan.getChildren().add(strategyContent);

//        Label adjustmentTitle = new Label("2. Key Adjustments");
//        adjustmentTitle.setFont(new Font("Arial", 12));
//        adjustmentTitle.setTextFill(Color.GRAY);
//        plan.getChildren().add(adjustmentTitle);
//
//        Label expenseTitle = new Label("1) Expense Optimization");
//        expenseTitle.setFont(new Font("Arial", 11));
//        expenseTitle.setTextFill(Color.GRAY);
//        plan.getChildren().add(expenseTitle);
//
//        Label expenseContent = new Label(
//                "- Dining: ¥750 → ¥600 (save ¥150+)\n" +
//                        "- Entertainment: ¥5,000 → ¥3,500\n" +
//                        "- Transportation: ¥3,750 → ¥2,500\n" +
//                        "- Daily expenses: ¥3,000 → ¥2,750\n" +
//                        "- Social contact: ¥2,500 → ¥1,250\n" +
//                        "- Utilities: Invest in smart home devices to cut energy costs.\n" +
//                        "- Set aside ¥1,000/month for emergencies.");
//        expenseContent.setFont(new Font("Arial", 10));
//        expenseContent.setTextFill(Color.GRAY);
//        plan.getChildren().add(expenseContent);
//
//        Label incomeTitle = new Label("2) Income Growth");
//        incomeTitle.setFont(new Font("Arial", 11));
//        incomeTitle.setTextFill(Color.GRAY);
//        plan.getChildren().add(incomeTitle);
//
//        Label incomeContent = new Label(
//                "- Career: Pursue certifications (#150/month) for a 15% salary increase.\n" +
//                        "- Side Hustle: Earn ¥3k+/month via freelance work (e.g., design, transcription).\n" +
//                        "- Investments: Allocate 50% of savings to modern ETF portfolios (5-7% returns).");
//        incomeContent.setFont(new Font("Arial", 10));
//        incomeContent.setTextFill(Color.GRAY);
//        plan.getChildren().add(incomeContent);

//        ScrollPane planScroll = new ScrollPane(plan);
//        planScroll.setFitToWidth(true);

        rankList.setPrefWidth(400);
        suggestionBox.setPrefWidth(600);

        HBox bottomSection = new HBox(10, rankList, suggestionBox);
        HBox.setHgrow(rankList, Priority.ALWAYS);
        HBox.setHgrow(suggestionBox, Priority.ALWAYS);

        return bottomSection;

    }

    private VBox createCard(String title, String from, String to, Color color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15, 40, 30, 40));
        card.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 0px; " +
                "-fx-background-radius: 15px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Arial", 14));
        titleLabel.setTextFill(Color.GRAY);

        Label fromLabel = new Label(from);
        fromLabel.setFont(new Font("Arial", 25));
        fromLabel.setTextFill(color);

        Label arrowLabel = new Label("→");
        arrowLabel.setFont(new Font("Arial", 25));
        arrowLabel.setTextFill(Color.BLACK);

        Label toLabel = new Label(to);
        toLabel.setFont(new Font("Arial", 25));
        toLabel.setTextFill(color);

        HBox content = new HBox(10, fromLabel, arrowLabel, toLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(titleLabel, content);
        return card;
    }

    private void addScaleAnimation(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setFromX(1);
        st.setToX(1.2);
        st.setFromY(1);
        st.setToY(1.2);
        button.setOnMouseEntered(e -> st.play());
        button.setOnMouseExited(e -> {
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), button);
            shrink.setFromX(1.2);
            shrink.setToX(1);
            shrink.setFromY(1.2);
            shrink.setToY(1);
            shrink.play();
        });
    }
}
