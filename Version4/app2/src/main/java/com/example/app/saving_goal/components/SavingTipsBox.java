package com.example.app.saving_goal.components;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SavingTipsBox extends VBox {

    public SavingTipsBox() {
        initialize();
    }

    private void initialize() {
        setSpacing(40);
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px;");

        HBox mainContainer = createMainContainer();
        getChildren().add(mainContainer);
    }

    private HBox createMainContainer() {
        HBox titleSection = createTitleSection();
        HBox imageSection = createImageSection();
        HBox container = new HBox(25);
        container.setAlignment(Pos.TOP_LEFT);
        container.getChildren().addAll(titleSection, imageSection);
        return container;
    }

    private HBox createTitleSection() {
        try {
            Image icon = new Image(new FileInputStream("src/main/resources/images/emoji2.png"));
            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(20);
            iconView.setFitHeight(20);

            Label title = new Label("Saving Tips");
            title.setFont(Font.font("Arial", FontWeight.BOLD, 17));

            HBox section = new HBox(10);
            section.setAlignment(Pos.TOP_LEFT);
            section.getChildren().addAll(iconView, title);
            return section;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Saving tips icon not found", e);
        }
    }

    private HBox createImageSection() {
        HBox imageContainer = new HBox(35);
        imageContainer.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < 5; i++) {
            ImageView imageView = createImageView(i);
            setupImageViewInteraction(imageView, i);
            imageContainer.getChildren().add(imageView);
        }
        return imageContainer;
    }

    private ImageView createImageView(int index) {
        try {
            Image image = new Image(new FileInputStream("src/main/resources/images/image" + (index + 1) + ".png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(140);
            imageView.setFitHeight(160);
            return imageView;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Image " + (index + 1) + " not found", e);
        }
    }

    private void setupImageViewInteraction(ImageView imageView, int index) {
        Tooltip tooltip = new Tooltip("Click to open corresponding book page!");
        Tooltip.install(imageView, tooltip);
        tooltip.setShowDelay(javafx.util.Duration.ZERO);

        imageView.setOnMouseClicked(event -> openRedBookWindow(index));
    }

    private void openRedBookWindow(int index) {
        Stage newStage = new Stage();

        switch (index) {
            case 0:
                RedBook1 redBook1 = new RedBook1();
                redBook1.start(newStage);
                break;
            case 1:
                RedBook2 redBook2 = new RedBook2();
                redBook2.start(newStage);
                break;
            case 2:
                RedBook3 redBook3 = new RedBook3();
                redBook3.start(newStage);
                break;
            case 3:
                RedBook4 redBook4 = new RedBook4();
                redBook4.start(newStage);
                break;
            case 4:
                RedBook5 redBook5 = new RedBook5();
                redBook5.start(newStage);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + index);
        }

        newStage.show();
    }
}

