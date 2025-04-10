package software;

import javafx.application.Application;
import javafx.stage.Stage;

public class PageSwitchApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // 创建两个页面实例，并互相设置引用
        PageOne pageOne = new PageOne(primaryStage);
        PageTwo pageTwo = new PageTwo(primaryStage);

        pageOne.setPageTwo(pageTwo);
        pageTwo.setPageOne(pageOne);

        // 初始显示页面1
        primaryStage.setTitle("main page");
        primaryStage.setScene(pageOne.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
