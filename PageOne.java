package software;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PageOne {
    private Stage stage;
    private PageTwo pageTwo; // 持有页面2的引用

    public PageOne(Stage stage) {
        this.stage = stage;
    }

    // 提供设置 PageTwo 的方法
    public void setPageTwo(PageTwo pageTwo) {
        this.pageTwo = pageTwo;
    }

    public Scene getScene() {
        Button switchButton = new Button("跳转到历史页面");

        switchButton.setOnAction(e -> {
            stage.setTitle("history");
            stage.setScene(pageTwo.getScene()); // 使用已创建的 pageTwo
        });

        StackPane layout = new StackPane(switchButton);
        return new Scene(layout, 1300, 800);
    }
}
