package com.example.app.saving_goal.components;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NavBox {
    private Button prevButton;
    private Button MainButton;
    private Button unifiedConfirmBtn; // 新增统一确认按钮
    private HBox titleWithIconBox; // 缓存标题栏容器
    // Method to create and return the navigation box
    public HBox getNavBox() {
        HBox navBox = new HBox(10);
        navBox.setAlignment(Pos.CENTER_LEFT);
//        navBox.setAlignment(Pos.CENTER);
        navBox.setPadding(new Insets(0, 10, 0, 10));
        navBox.setStyle("-fx-background-color: #F4E1D2; -fx-border-width: 5px;");
        navBox.setPrefHeight(50);


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setHgrow(prevButton, Priority.ALWAYS);

        Label appTitle = new Label("Tally App");
        appTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;");
        HBox.setHgrow(appTitle, Priority.ALWAYS);

        navBox.getChildren().addAll(spacer, appTitle);
        return navBox;
    }
    private String buttonStyle(String type) {
        String base = "-fx-text-fill: white; -fx-background-radius: 10px; -fx-pref-width: 90px; -fx-pref-height: 35px;";
        String bg = "#6A5ACD";
        return "-fx-background-color: " + bg + ";" + base;
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
    // Method to create and return the title with icon box
    public HBox getTitleWithIconBox() {
        HBox buttonBar = null;
        if (titleWithIconBox == null) {
            // 仅首次调用时初始化
            titleWithIconBox = new HBox(10);
            titleWithIconBox.setAlignment(Pos.CENTER_LEFT);
            prevButton = new Button("Chart");
            addScaleAnimation(prevButton);
            prevButton.setStyle(buttonStyle("saving"));
            MainButton = new Button("Main Page");
            addScaleAnimation(MainButton);
            MainButton.setStyle(buttonStyle("saving"));
            // 标签和图标初始化
            Label piggyBankLabel = new Label("Piggy bank for 2024");
            piggyBankLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
            piggyBankLabel.setTextFill(javafx.scene.paint.Color.GRAY);
            HBox.setMargin(piggyBankLabel, new Insets(0, 0, 4, 10));

            ImageView iconImageView = loadIcon();

            // 统一确认按钮初始化（仅一次）
            unifiedConfirmBtn = new Button("★ Unified Confirm");
            unifiedConfirmBtn.setStyle("-fx-background-color: #6A5ACD; "
                    + "-fx-text-fill: white; "
                    + "-fx-font-weight: bold; "
                    + "-fx-background-radius: 15px; "
                    + "-fx-padding: 5 15 5 15;");
            HBox.setMargin(unifiedConfirmBtn, new Insets(0, 10, 0, 0));

            // 单次添加子节点
            titleWithIconBox.getChildren().addAll(
                    piggyBankLabel,
                    iconImageView,
                    unifiedConfirmBtn
            );
            HBox rightButtonBox = new HBox(30, prevButton, MainButton);
            rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
            rightButtonBox.setPadding(new Insets(0, 40, 10, 0));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            buttonBar = new HBox(10, titleWithIconBox, spacer, rightButtonBox);


        }
        return buttonBar;
    }
    private ImageView loadIcon() {
        try {
            Image icon = new Image(new FileInputStream("src/main/resources/images/emoji3.png"));
            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(20);
            iconView.setFitHeight(20);
            return iconView;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Icon load failed", e);
        }
    }
    public Button getUnifiedConfirmButton() {
        return unifiedConfirmBtn;
    }

    /** 提供 prevButton 以便外部绑定返回事件 */
    public Button getPrevButton() {
        return prevButton;
    }
}
