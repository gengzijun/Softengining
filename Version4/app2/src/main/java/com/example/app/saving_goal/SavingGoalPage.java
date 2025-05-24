package com.example.app.saving_goal;
import com.example.app.chart.ChartPage;
import com.example.app.util.BudgetPrediction;
import com.example.app.saving_goal.components.*;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SavingGoalPage {
    private final Stage stage;
    private final HostServices host;
    private final Scene scene;

    private ChartPage chartPage;     // 返回目标页
    private Button prevButton;       // ← 返回按钮

    private WatchService watchService;
    private DetailsBox detailsBox;
    private ProgressBox progressBox;
    private EmergencyBox emergencyBox;
    private NavBox navBox;
    private List<String[]> mergedData;

    public SavingGoalPage(Stage stage, HostServices host) throws CsvValidationException, IOException {
        this.stage = stage;
        this.host = host;
        this.navBox = new NavBox();
        this.scene = buildScene();   // 构建页面

        // 获取统一确认按钮
        Button unifiedConfirm = navBox.getUnifiedConfirmButton();

        // 绑定点击事件
        unifiedConfirm.setOnAction(e -> {
            // Step 1: Handle progress
            progressBox.handleConfirm();

            // Step 2: Save emergency input
            Platform.runLater(() -> emergencyBox.saveInputValue());

            // Step 3: Show AI prediction alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("AI Prediction");
            alert.setHeaderText("Predicting Budget...");
            alert.setContentText("The AI is analyzing your financial data. Please wait...");
            alert.setResizable(true);
            alert.show();

            // Step 4: Run AI prediction in background
            new Thread(() -> {
                try {
                    Map<String, File> latest = ChartPage.findLatestSummaryFiles("src/main/resources/summaries");

                    String spendingMonthlyCsv = latest.get("month").getAbsolutePath();
                    String spendingQuarterlyCsv = latest.get("quarter").getAbsolutePath();
                    String spendingYearlyCsv = latest.get("year").getAbsolutePath();
                    String savingsCsv = "src/main/resources/savinggoal/saving_data.csv";
                    String outputMonthlyCsv = "src/main/resources/prediction/budget_recommendation_month.csv";
                    String outputQuarterlyCsv = "src/main/resources/prediction/budget_recommendation_quarter.csv";
                    String outputYearlyCsv = "src/main/resources/prediction/budget_recommendation_year.csv";

                    BudgetPrediction.runBudgetPrediction(spendingMonthlyCsv, savingsCsv, outputMonthlyCsv);
                    System.out.println("AI prediction finished: " + outputMonthlyCsv);
                    BudgetPrediction.runBudgetPrediction(spendingQuarterlyCsv, savingsCsv, outputQuarterlyCsv);
                    System.out.println("AI prediction finished: " + outputQuarterlyCsv);
                    BudgetPrediction.runBudgetPrediction(spendingYearlyCsv, savingsCsv, outputYearlyCsv);
                    System.out.println("AI prediction finished: " + outputYearlyCsv);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println("AI prediction error: " + ex.getMessage());
                } finally {
                    // Step 5: Close alert in UI thread
                    Platform.runLater(alert::close);
                }
            }).start();
        });
    }

    public Scene getScene() {
        return scene;
    }

    public void setChartPage(ChartPage chartPage) {
        this.chartPage = chartPage;
        if (prevButton != null) {
            prevButton.setOnAction(e -> {
                stage.setScene(chartPage.getScene());
                stage.setTitle("chart page");
            });
        }
    }

    private Scene buildScene() throws IOException, CsvValidationException {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        HBox navBar = this.navBox.getNavBox();
        HBox titleBar = this.navBox.getTitleWithIconBox();
        prevButton = this.navBox.getPrevButton();

        VBox top = new VBox(10, navBar, titleBar);
        root.setTop(top);

        // 自动读取 data 文件夹下所有 csv 文件
        File historyDir = new File("src/main/resources/history");
        // 初始化文件监控
        setupFileWatcher(historyDir.toPath());

        // 初始读取文件
        refreshData(historyDir);

        // 创建主要内容区域
        refreshData(historyDir); // ✅ 提前加载数据
        this.detailsBox = new DetailsBox(mergedData); // 使用已加载的数据
        this.progressBox = new ProgressBox(this.detailsBox);
        this.emergencyBox = new EmergencyBox();
        SavingTipsBox tipsBox = new SavingTipsBox();

        // 双向绑定更新进度
        detailsBox.addUpdateListener(progressBox::updateProgress);

        VBox content = new VBox(30, progressBox, detailsBox, emergencyBox, tipsBox);
        Insets margin = new Insets(0, 10, 0, 10);
        VBox.setMargin(progressBox, margin);
        VBox.setMargin(detailsBox, margin);
        VBox.setMargin(emergencyBox, margin);
        VBox.setMargin(tipsBox, margin);

        root.setCenter(content);
        return new Scene(root, 1440, 850, Color.WHITE);
    }

    private void refreshData(File directory) throws IOException, CsvValidationException {
        List<String> files = Arrays.stream(directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv")))
                .map(File::getPath)
                .collect(Collectors.toList());

        // 读取数据前打印文件列表
        System.out.println("\n=== Loading files from " + directory.getAbsolutePath() + " ===");
        System.out.println("Detected CSV files: " + files);

        mergedData = CSVReaderUtility.readAndMergeCSVFiles(files);

        Platform.runLater(() -> {
            detailsBox.setMergedData(mergedData); // 传递新数据
            detailsBox.refreshWithNewData(mergedData); // 强制刷新
        });

        // 打印合并后的数据
        System.out.println("\n=== Merged Data (Total Rows: " + mergedData.size() + ") ===");
        for (int i = 0; i < mergedData.size(); i++) {
            String[] row = mergedData.get(i);
            // 格式化输出：行号 + 列数据
            System.out.printf("Row %3d: %s\n", i, Arrays.toString(row));

            // 如果需要更详细的列解析，可以这样：
            // System.out.printf("Row %3d | Date: %-10s | Type: %-8s | Category: %-15s | Amount: %-10s\n",
            //         i,
            //         row.length > 0 ? row[0] : "N/A",
            //         row.length > 1 ? row[1] : "N/A",
            //         row.length > 2 ? row[2] : "N/A",
            //         row.length > 3 ? row[3] : "N/A");
        }
        System.out.println("=== End of Merged Data ===\n");

        // 更新UI组件
        if (detailsBox != null) {
            Platform.runLater(() -> {
                System.out.println("Updating UI with new data...");
                detailsBox.setMergedData(mergedData);
                detailsBox.refreshWithNewData(mergedData);
            });
        }
    }

    private void setupFileWatcher(Path path) throws IOException {
        watchService = FileSystems.getDefault().newWatchService();

        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        Thread watchThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();
                    // 处理新增/修改事件
                    boolean shouldRefresh = false;
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE ||
                                event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            shouldRefresh = true;
                        }
                    }

                    if (shouldRefresh) {
                        Thread.sleep(500); // 等待文件写入完成
                        Platform.runLater(() -> {
                            try {
                                refreshData(path.toFile());
                            } catch (Exception e) {
                                System.err.println("刷新数据失败: " + e.getMessage());
                            }
                        });
                    }
                    key.reset();
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });

        watchThread.setDaemon(true);
        watchThread.start();
    }

    // 添加关闭监控的方法（在stage关闭时调用）
    public void shutdown() {
        try {
            if (watchService != null) {
                watchService.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
