package com.example.app;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import com.opencsv.exceptions.CsvValidationException;
import com.example.app.main.MainPage;
import com.example.app.history.HistoryPage;
import com.example.app.chart.ChartPage;
import com.example.app.saving_goal.SavingGoalPage;
import com.example.app.login.LoginPage;

public class SwitchApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        HostServices host = getHostServices();

        /* --------- 主功能页 --------- */
        MainPage mainPage = new MainPage(primaryStage);
        ChartPage chartPage = new ChartPage(primaryStage);
        SavingGoalPage savingGoalPage = null;  // 提前定义

        try {
            savingGoalPage = new SavingGoalPage(primaryStage, host); // 初始化
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return;  // 初始化失败直接返回或给出错误提示
        }
        HistoryPage historyPage = new HistoryPage(primaryStage);

        /* --------- 登录页 --------- */
        LoginPage loginPage = new LoginPage(primaryStage);
        loginPage.setMainPage(mainPage::getScene); // 注入切换目标

        /* --------- 页面互链 --------- */
        mainPage.setChartPage(chartPage);
        mainPage.setPageTwo(historyPage);

        chartPage.setMainPage(mainPage);
        chartPage.setSavingGoalPage(savingGoalPage);

        savingGoalPage.setChartPage(chartPage);
        historyPage.setPageOne(mainPage);

        /* --------- 初始显示登录页 --------- */
        primaryStage.setTitle("TallyAPP Login");
        primaryStage.setScene(loginPage.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
