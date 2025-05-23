package com.example.app.saving_goal.components;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NavBox {
    private Button prevButton;
    private Button unifiedConfirmBtn; // 新增统一确认按钮
    private HBox titleWithIconBox; // 缓存标题栏容器
    // Method to create and return the navigation box
    public HBox getNavBox() {
        HBox navBox = new HBox(1280);
        navBox.setAlignment(Pos.CENTER);
        navBox.setStyle("-fx-background-color: #EEDFCC; -fx-border-color: #D3D3D3; -fx-border-width: 2px;");

        prevButton = new Button("<-previous");
        prevButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        HBox.setHgrow(prevButton, Priority.ALWAYS);

        Label appTitle = new Label("Tally app");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        HBox.setHgrow(appTitle, Priority.ALWAYS);

        navBox.getChildren().addAll(prevButton, appTitle);
        return navBox;
    }

    // Method to create and return the title with icon box
    public HBox getTitleWithIconBox() {
        if (titleWithIconBox == null) {
            // 仅首次调用时初始化
            titleWithIconBox = new HBox(10);
            titleWithIconBox.setAlignment(Pos.CENTER_LEFT);

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
        }
        return titleWithIconBox;
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
