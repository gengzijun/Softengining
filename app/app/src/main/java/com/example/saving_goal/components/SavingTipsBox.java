package com.example.saving_goal.components;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.InputStream;

import javafx.scene.control.*;

public class SavingTipsBox extends VBox {

    // Array of URLs for the images
    private static final String[] IMAGE_URLS = {
            "http://xhslink.com/a/BAAY5VXskNjab",
            "http://xhslink.com/a/LOgWzLvDvNjab",
            "http://xhslink.com/a/QKsCUZLIHNjab",
            "http://xhslink.com/a/kZXHFZDNSNjab",
            "http://xhslink.com/a/8XYCuNQzaOjab"
    };

    private final HostServices hostServices;

    // Constructor that accepts HostServices
    public SavingTipsBox(HostServices hostServices) {
        this.hostServices = hostServices;
        initialize();
    }

    private void initialize() {
        // Basic layout settings
        setSpacing(40);
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(10));
        setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1px; -fx-border-radius: 10px;");

        // Create the main container (composed of title and images) and add it to the
        // VBox
        HBox mainContainer = createMainContainer();
        getChildren().add(mainContainer);
    }

    private HBox createMainContainer() {
        // Create left side with title and icon
        HBox titleSection = createTitleSection();

        // Create right side with images
        HBox imageSection = createImageSection();

        // Combine title and image sections in a horizontal layout (HBox)
        HBox container = new HBox(25);
        container.setAlignment(Pos.TOP_LEFT);
        container.getChildren().addAll(titleSection, imageSection);
        return container;
    }

    private HBox createTitleSection() {
        InputStream is = getClass().getResourceAsStream("/images/emoji2.png");

        if (is == null) {
            System.out.println("⚠ 图片 /images/emoji2.png 未找到");
            throw new RuntimeException("Saving tips icon not found");
        }

        Image icon = new Image(is);
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(20);
        iconView.setFitHeight(20);

        Label title = new Label("Saving Tips");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 17));

        HBox section = new HBox(10);
        section.setAlignment(Pos.TOP_LEFT);
        section.getChildren().addAll(iconView, title);
        return section;
    }

    private HBox createImageSection() {
        HBox imageContainer = new HBox(35);
        imageContainer.setAlignment(Pos.CENTER_LEFT);

        // Loop through the image URLs and create image views for each
        for (int i = 0; i < 5; i++) {
            ImageView imageView = createImageView(i);
            setupImageViewInteraction(imageView, i);
            imageContainer.getChildren().add(imageView);
        }
        return imageContainer;
    }

    private ImageView createImageView(int index) {
        String path = "/images/image" + (index + 1) + ".png";
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.out.println("⚠ 图像未找到：" + path);
            throw new RuntimeException("Image " + (index + 1) + " not found");
        }

        Image image = new Image(is);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setFitHeight(160);
        return imageView;
    }

    private void setupImageViewInteraction(ImageView imageView, int index) {
        // Set up tooltip for the image
        Tooltip tooltip = new Tooltip("Click to visit the website!");
        Tooltip.install(imageView, tooltip);
        tooltip.setShowDelay(javafx.util.Duration.ZERO);

        // Set up mouse click event to open the URL corresponding to the image
        imageView.setOnMouseClicked(event -> hostServices.showDocument(IMAGE_URLS[index]));
    }
}
