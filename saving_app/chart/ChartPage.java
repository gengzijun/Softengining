package saving_app.chart;

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

import saving_app.main.MainPage;
import saving_app.saving_goal.SavingGoalPage;

public class ChartPage {
    private final Stage stage;
    private MainPage mainPage;
    private SavingGoalPage savingGoalPage;
    private final Scene scene;

    private BorderPane root;
    private SimpleDoubleProperty fontSizeFactor = new SimpleDoubleProperty(1.0);
    private String currentView = "month";

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

        return new Scene(root, 1200, 900);
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
        VBox expenditure = createCard("Expenditure", "¥ 25,000", "¥ 20,000", Color.RED);
        VBox income = createCard("Income", "¥ 40,000", "¥ 50,000", Color.GREEN);
        VBox budget = createCard("Budget", "¥ 20,000", "¥ 20,000", Color.BLACK);
        HBox hbox = new HBox(30, expenditure, income, budget); // 卡片之间间隔增大为30
        hbox.setPadding(new Insets(10, 60, 10, 60)); // 上右下左，左右留白60
        hbox.setAlignment(Pos.CENTER); // 居中排列

        // 让每一块卡片在HBox中均分宽度
        HBox.setHgrow(expenditure, Priority.ALWAYS);
        HBox.setHgrow(income, Priority.ALWAYS);
        HBox.setHgrow(budget, Priority.ALWAYS);

        // 同时给每张卡片设定最大宽度适配
        expenditure.setMaxWidth(Double.MAX_VALUE);
        income.setMaxWidth(Double.MAX_VALUE);
        budget.setMaxWidth(Double.MAX_VALUE);

        return hbox;

    }

    private HBox createCharts(String view) {
        LineChart<?, Number> lineChart;
        if ("quarter".equals(view)) {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Month/Day");
            lineChart = new LineChart<>(xAxis, new NumberAxis("Amount (¥)", 2000, 4000, 500));
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            String[] labels = { "4/1", "4/5", "4/10", "4/15", "4/20", "4/25", "4/30", "5/5", "5/10", "5/15", "5/20",
                    "5/25", "5/30" };
            int val = 2500;
            for (String label : labels) {
                series.getData().add(new XYChart.Data<>(label, val));
                val += 50;
            }
            ((LineChart<String, Number>) lineChart).getData().add(series);
        } else {
            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("month".equals(view) ? "Date" : "Year");
            lineChart = new LineChart<>(xAxis, new NumberAxis("Amount (¥)", 2000, 4000, 500));
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            int days = "month".equals(view) ? 30 : 12;
            for (int i = 1; i <= days; i++) {
                series.getData().add(new XYChart.Data<>(i, 2500 + i * 50));
            }
            ((LineChart<Number, Number>) lineChart).getData().add(series);
        }
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Frame");
        pieChart.getData().addAll(
                new PieChart.Data("Catering", 34.78),
                new PieChart.Data("Entertainment", 23.64),
                new PieChart.Data("Transaction", 13.69),
                new PieChart.Data("Daily expenses", 16.1),
                new PieChart.Data("Social contact", 9.76),
                new PieChart.Data("Others", 11.42));
        pieChart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // 包装图表，设置外边框
        VBox lineChartBox = new VBox(lineChart);
        lineChartBox.setPadding(new Insets(10));
        lineChartBox.setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 1; -fx-background-color: white;");
        VBox.setVgrow(lineChart, Priority.ALWAYS);

        VBox pieChartBox = new VBox(pieChart);
        pieChartBox.setPadding(new Insets(10));
        pieChartBox.setStyle("-fx-border-color: #dcdcdc; -fx-border-width: 1; -fx-background-color: white;");
        VBox.setVgrow(pieChart, Priority.ALWAYS);

        HBox charts = new HBox(20, lineChartBox, pieChartBox); // 图表之间间隔
        charts.setPadding(new Insets(10, 60, 10, 60)); // 外部留白
        charts.setAlignment(Pos.CENTER);
        HBox.setHgrow(lineChartBox, Priority.ALWAYS);
        HBox.setHgrow(pieChartBox, Priority.ALWAYS);

        return charts;
    }

    private HBox createBottomSection() {
        // 左侧 Rank 区域
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

        String[] types = { "Catering", "Entertainment", "Transaction", "Daily expenses", "Social contact" };
        String[] fromAmounts = { "¥6,750", "¥5,000", "¥3,750", "¥2,300", "¥2,500" };
        String[] toAmounts = { "¥7,500", "¥7,500", "¥3,750", "¥3,750", "¥1,250" };
        Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.PURPLE };

        for (int i = 0; i < types.length; i++) {
            HBox rankItem = new HBox(10);
            rankItem.setAlignment(Pos.CENTER_LEFT);

            Circle icon = new Circle(10);
            icon.setFill(colors[i]);

            Label typeLabel = new Label("type: " + types[i]);
            typeLabel.setFont(new Font("Arial", 18));
            typeLabel.setTextFill(Color.GRAY);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label fromLabel = new Label(fromAmounts[i]);
            fromLabel.setFont(new Font("Arial", 18));
            fromLabel.setTextFill(colors[i]);

            Label arrowLabel = new Label(" → ");
            arrowLabel.setFont(new Font("Arial", 18));
            arrowLabel.setTextFill(Color.GRAY);

            Label toLabel = new Label(toAmounts[i]);
            toLabel.setFont(new Font("Arial", 18));
            toLabel.setTextFill(colors[i]);

            HBox valueBox = new HBox(5, fromLabel, arrowLabel, toLabel);
            rankItem.getChildren().addAll(icon, typeLabel, spacer, valueBox);

            rankList.getChildren().add(rankItem);
            VBox.setVgrow(rankItem, Priority.ALWAYS); // 关键：垂直均分每一项
        }

        VBox plan = new VBox(10);
        plan.setPadding(new Insets(15));
        plan.setStyle(
                "-fx-background-color: #f5f0e6; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        Label planTitle = new Label("Financial Optimization Plan (Given by Fairy)");
        planTitle.setFont(new Font("Arial", 14));
        planTitle.setTextFill(Color.GRAY);
        plan.getChildren().add(planTitle);

        Label strategyTitle = new Label("1. Overall Strategy");
        strategyTitle.setFont(new Font("Arial", 12));
        strategyTitle.setTextFill(Color.GRAY);
        plan.getChildren().add(strategyTitle);

        Label strategyContent = new Label(
                "Aim to boost income by 12.3% (#40k→#45k) and reduce expenses by 20% (#25k→#20k) while maintaining a 20% savings rate.\n"
                        +
                        "- Budget: Focus on a 50-30-20 allocation\n" +
                        "- Debt: Pay off credit card debt\n" +
                        "- Savings: 20% savings/investments (#4k)\n" +
                        "Create a \"Growth Fund\" (#500/month) for skill development to support long-term income growth.");
        strategyContent.setFont(new Font("Arial", 11));
        strategyContent.setTextFill(Color.GRAY);
        plan.getChildren().add(strategyContent);

        Label adjustmentTitle = new Label("2. Key Adjustments");
        adjustmentTitle.setFont(new Font("Arial", 12));
        adjustmentTitle.setTextFill(Color.GRAY);
        plan.getChildren().add(adjustmentTitle);

        Label expenseTitle = new Label("1) Expense Optimization");
        expenseTitle.setFont(new Font("Arial", 11));
        expenseTitle.setTextFill(Color.GRAY);
        plan.getChildren().add(expenseTitle);

        Label expenseContent = new Label(
                "- Dining: ¥750 → ¥600 (save ¥150+)\n" +
                        "- Entertainment: ¥5,000 → ¥3,500\n" +
                        "- Transportation: ¥3,750 → ¥2,500\n" +
                        "- Daily expenses: ¥3,000 → ¥2,750\n" +
                        "- Social contact: ¥2,500 → ¥1,250\n" +
                        "- Utilities: Invest in smart home devices to cut energy costs.\n" +
                        "- Set aside ¥1,000/month for emergencies.");
        expenseContent.setFont(new Font("Arial", 10));
        expenseContent.setTextFill(Color.GRAY);
        plan.getChildren().add(expenseContent);

        Label incomeTitle = new Label("2) Income Growth");
        incomeTitle.setFont(new Font("Arial", 11));
        incomeTitle.setTextFill(Color.GRAY);
        plan.getChildren().add(incomeTitle);

        Label incomeContent = new Label(
                "- Career: Pursue certifications (#150/month) for a 15% salary increase.\n" +
                        "- Side Hustle: Earn ¥3k+/month via freelance work (e.g., design, transcription).\n" +
                        "- Investments: Allocate 50% of savings to modern ETF portfolios (5-7% returns).");
        incomeContent.setFont(new Font("Arial", 10));
        incomeContent.setTextFill(Color.GRAY);
        plan.getChildren().add(incomeContent);

        ScrollPane planScroll = new ScrollPane(plan);
        planScroll.setFitToWidth(true);

        rankList.setPrefWidth(400);
        planScroll.setPrefWidth(600);

        HBox bottomSection = new HBox(10, rankList, planScroll);
        HBox.setHgrow(rankList, Priority.ALWAYS);
        HBox.setHgrow(planScroll, Priority.ALWAYS);

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
