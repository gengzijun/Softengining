package com.example.saving_goal.components;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NavBox {

    private Button prevButton;

    /** 导航条 */
    public HBox getNavBox() {
        HBox navBox = new HBox(1280);
        navBox.setAlignment(Pos.CENTER);
        navBox.setStyle(
                "-fx-background-color: #EEDFCC; -fx-border-color: #D3D3D3; -fx-border-width: 2px;");

        prevButton = new Button("< Previous"); // 文字统一
        prevButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        Label appTitle = new Label("Tally App");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox.setHgrow(prevButton, Priority.ALWAYS);
        HBox.setHgrow(appTitle, Priority.ALWAYS);

        navBox.getChildren().addAll(prevButton, appTitle);
        return navBox;
    }

    /** “Tally App”下方标题栏 */
    public HBox getTitleWithIconBox() {
        HBox titleWithIconBox = new HBox(10);
        titleWithIconBox.setAlignment(Pos.CENTER_LEFT);

        Label piggyBankLabel = new Label("Piggy Bank for 2024");
        piggyBankLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        piggyBankLabel.setTextFill(javafx.scene.paint.Color.GRAY);
        HBox.setMargin(piggyBankLabel, new Insets(0, 0, 4, 10));

        javafx.scene.image.Image iconImage;
        java.io.InputStream is = getClass().getResourceAsStream("/images/emoji3.png");

        if (is != null) {
            iconImage = new javafx.scene.image.Image(is);
        } else {
            System.out.println("⚠ 图片 /images/emoji3.png 未找到");
            iconImage = new javafx.scene.image.Image("https://via.placeholder.com/20"); // 网络兜底图（可替换）
        }

        javafx.scene.image.ImageView iconView = new javafx.scene.image.ImageView(iconImage);
        iconView.setFitWidth(20);
        iconView.setFitHeight(20);

        titleWithIconBox.getChildren().addAll(piggyBankLabel, iconView);
        return titleWithIconBox;
    }

    /** 提供 Previous 按钮给外部绑定事件 */
    public Button getPrevButton() {
        return prevButton;
    }
}
