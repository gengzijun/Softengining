package com.example;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

import com.example.main.MainPage;
import com.example.history.HistoryPage;
import com.example.chart.ChartPage;
import com.example.saving_goal.SavingGoalPage;
import com.example.login.LoginPage;

public class SwitchApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        HostServices host = getHostServices();

        /* --------- 主功能页 --------- */
        MainPage mainPage = new MainPage(primaryStage);
        ChartPage chartPage = new ChartPage(primaryStage);
        SavingGoalPage savingGoalPage = new SavingGoalPage(primaryStage, host);
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
