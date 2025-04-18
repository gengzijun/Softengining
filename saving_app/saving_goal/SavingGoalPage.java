package saving_app.saving_goal;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import saving_app.saving_goal.components.*;
import saving_app.chart.ChartPage;

public class SavingGoalPage {

    private final Stage stage;
    private final HostServices host;
    private final Scene scene;

    private ChartPage chartPage;      // 返回目标
    private Button prevButton;        // ← 记录导航按钮

    /* ---------- 构造 ---------- */
    public SavingGoalPage(Stage stage, HostServices host) {
        this.stage = stage;
        this.host  = host;
        this.scene = buildScene();    // 先搭界面
    }

    public Scene getScene()                { return scene; }

    /** 由外部注入 ChartPage，并在此时绑定返回事件 */
    public void setChartPage(ChartPage p) {
        this.chartPage = p;
        if (prevButton != null) {
            prevButton.setOnAction(e -> {
                stage.setScene(chartPage.getScene());
                stage.setTitle("chart page");
            });
        }
    }

    /* ---------- 页面搭建 ---------- */
    private Scene buildScene() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        /* —— 顶栏 —— */
        NavBox navBox = new NavBox();
        HBox navBar   = navBox.getNavBox();
        HBox titleBar = navBox.getTitleWithIconBox();
        prevButton    = navBox.getPrevButton();   // ← 保存按钮引用

        VBox top = new VBox(10, navBar, titleBar);
        root.setTop(top);

        /* —— 主体内容 —— */
        ProgressBox   progressBox   = new ProgressBox();
        DetailsBox    detailsBox    = new DetailsBox();
        EmergencyBox  emergencyBox  = new EmergencyBox();
        SavingTipsBox tipsBox       = new SavingTipsBox(host);

        VBox content = new VBox(30, progressBox, detailsBox, emergencyBox, tipsBox);
        Insets m = new Insets(0, 10, 0, 10);
        VBox.setMargin(progressBox, m);
        VBox.setMargin(detailsBox,  m);
        VBox.setMargin(emergencyBox,m);
        VBox.setMargin(tipsBox,     m);
        root.setCenter(content);

        return new Scene(root, 1440, 850, Color.WHITE);
    }
}
