package com.example.app.saving_goal;

import com.example.app.saving_goal.components.*;
import com.example.app.chart.ChartPage;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SavingGoalPage {
    private final Stage stage;
    private final HostServices host;
    private final Scene scene;

    private ChartPage chartPage;     // 返回目标页
    private Button prevButton;       // ← 返回按钮

    public SavingGoalPage(Stage stage, HostServices host) throws CsvValidationException, IOException {
        this.stage = stage;
        this.host = host;
        this.scene = buildScene();   // 构建页面
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

        // 顶部导航栏
        NavBox navBox = new NavBox();
        HBox navBar = navBox.getNavBox();
        HBox titleBar = navBox.getTitleWithIconBox();
        prevButton = navBox.getPrevButton();

        VBox top = new VBox(10, navBar, titleBar);
        root.setTop(top);

        // 自动读取 data 文件夹下所有 csv 文件
        File dataDir = new File("src/main/resources/data");
        List<String> files = Arrays.stream(dataDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv")))
                .map(File::getPath)
                .collect(Collectors.toList());

        List<String[]> mergedData = CSVReaderUtility.readAndMergeCSVFiles(files);

        // 创建主要内容区域
        DetailsBox detailsBox = new DetailsBox(mergedData);
        ProgressBox progressBox = new ProgressBox(detailsBox);
        EmergencyBox emergencyBox = new EmergencyBox();
        SavingTipsBox tipsBox = new SavingTipsBox(host);

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
}
