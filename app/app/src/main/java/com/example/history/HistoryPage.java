package com.example.history;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableCell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.example.main.MainPage;

public class HistoryPage {
    private final Stage stage;
    private MainPage mainpage;
    private final TableView<Record> table = new TableView<>();
    private ObservableList<Record> originalData = FXCollections.observableArrayList();

    public HistoryPage(Stage stage) {
        this.stage = stage;
    }

    public void setPageOne(MainPage mainpage) {
        this.mainpage = mainpage;
    }

    public Scene getScene() {
        TextField searchField = new TextField();
        ComboBox<String> comboBox = new ComboBox<>();

        VBox leftPane = new VBox(20);
        leftPane.setStyle("-fx-background-color: rgb(238,223,207);");
        leftPane.setPadding(new Insets(20));

        Label title = new Label("< Previous");
        title.setFont(new Font("Arial", 20));
        title.setStyle(
                "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 5px;");

        title.setOnMouseClicked(e -> {
            stage.setTitle("main page");
            stage.setScene(mainpage.getScene());
        });

        VBox menuList = new VBox(10);
        for (String month : Month.month_list) {
            HBox item = new HBox(10);
            item.setPadding(new Insets(10));
            item.setStyle(
                    "-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");

            Circle circle = new Circle(6, Color.LIGHTGRAY);
            Label label = new Label(month);
            label.setFont(new Font(12));

            item.getChildren().addAll(circle, label);

            item.setOnMouseClicked(e -> {
                System.out.println("点击了月份：" + month);
                ObservableList<Record> newData = loadCSVData(month);
                table.setItems(newData);
                originalData = FXCollections.observableArrayList(newData);
                searchField.clear();
                comboBox.setValue("All information");
            });

            menuList.getChildren().add(item);
        }

        leftPane.getChildren().addAll(title, menuList);

        VBox rightPane = new VBox();
        VBox topSection = new VBox(10);
        topSection.setStyle("-fx-background-color:rgb(239, 230, 220);");

        Label titleLabel = new Label("Transaction History");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        comboBox.getItems().addAll("All information", "Task", "Detail", "Type", "Amount", "Date");
        comboBox.setPromptText("All information");
        comboBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ccc;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-font-size: 14px;");

        searchField.setPromptText("Search tickets...");
        searchField.setPrefHeight(36);
        searchField.setPrefWidth(600);
        searchField.setStyle(
                "-fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #ccc; -fx-padding: 0 10;");

        Button filterButton = new Button("\uD83D\uDD0D Filter");
        filterButton.setStyle(
                "-fx-background-color: #F5F5F5; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 6 12; -fx-text-fill: #555;");

        filterButton.setOnAction(e -> {
            String selectedCategory = comboBox.getValue();
            String searchText = searchField.getText().toLowerCase();
            ObservableList<Record> filteredData = filterData(selectedCategory, searchText);
            table.setItems(filteredData);
        });

        HBox searchBar = new HBox(10, comboBox, searchField, filterButton);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        topSection.getChildren().addAll(titleLabel, searchBar);
        topSection.setPadding(new Insets(20, 0, 10, 10));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(new Label(""));

        // 清除旧列，防止重复添加
        table.getColumns().clear();

        TableColumn<Record, String> taskCol = new TableColumn<>("Task");
        taskCol.setCellValueFactory(new PropertyValueFactory<>("task"));
        taskCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableColumn<Record, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableColumn<Record, String> detailCol = new TableColumn<>("Detail");
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableColumn<Record, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Record record = getTableView().getItems().get(getIndex());
                    if (record.getType().equalsIgnoreCase("Income")) {
                        setText("￥" + item);
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setText("-￥" + item);
                        setStyle("-fx-text-fill: black; -fx-font-weight: normal;");
                    }
                }
            }
        });
        amountCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Record, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-text-fill: #333;");

        table.getColumns().addAll(taskCol, detailCol, typeCol, amountCol, dateCol);

        table.setStyle("""
                    -fx-background-color: white;
                    -fx-table-cell-border-color: transparent;
                    -fx-border-color: transparent;
                    -fx-font-size: 14px;
                """);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Record item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle(
                            "-fx-background-color: white; -fx-border-color: transparent transparent #ddd transparent; -fx-border-width: 0 0 1px 0;");
                } else {
                    setStyle(
                            "-fx-background-color: white; -fx-padding: 12 10; -fx-border-color: transparent transparent #ddd transparent; -fx-border-width: 0 0 1px 0;");
                    setOnMouseEntered(e -> setStyle(
                            "-fx-background-color: #f8f8f8; -fx-padding: 12 10; -fx-border-color: transparent transparent #ddd transparent; -fx-border-width: 0 0 1px 0;"));
                    setOnMouseExited(e -> setStyle(
                            "-fx-background-color: white; -fx-padding: 12 10; -fx-border-color: transparent transparent #ddd transparent; -fx-border-width: 0 0 1px 0;"));
                }
            }
        });

        VBox contentArea = new VBox(table);
        contentArea.setSpacing(10);
        contentArea.setStyle("-fx-border-color:rgb(255,255,255); -fx-border-width: 1;");
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox proportionBox = new VBox();
        proportionBox.getChildren().addAll(topSection, contentArea);

        proportionBox.heightProperty().addListener((obs, oldVal, newVal) -> {
            double totalHeight = newVal.doubleValue();
            topSection.setPrefHeight(totalHeight * 0.1);
            contentArea.setPrefHeight(totalHeight * 0.9);
        });

        rightPane.getChildren().addAll(proportionBox);
        VBox.setVgrow(proportionBox, Priority.ALWAYS);

        HBox rootLayout = new HBox(leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        rootLayout.widthProperty().addListener((obs, oldVal, newVal) -> {
            double total = newVal.doubleValue();
            leftPane.setPrefWidth(total * 0.2);
            rightPane.setPrefWidth(total * 0.8);
        });

        return new Scene(rootLayout, 1200, 900);
    }

    private ObservableList<Record> loadCSVData(String month) {
        ObservableList<Record> data = FXCollections.observableArrayList();
        String filePath = "app/src/main/resources/data/save_data/" + month + ".csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // 跳过标题

            String line;
            int taskCounter = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String taskName = "Ex-" + taskCounter++;
                    String date = parts[0];
                    String detail = parts[1];
                    String amount = parts[2];
                    String type = parts[3];
                    data.add(new Record(taskName, detail, type, amount, date));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private ObservableList<Record> filterData(String category, String searchText) {
        ObservableList<Record> filteredData = FXCollections.observableArrayList();

        if (category == null || category.equals("All information")) {
            for (Record record : originalData) {
                if (record.toString().toLowerCase().contains(searchText)) {
                    filteredData.add(record);
                }
            }
        } else {
            for (Record record : originalData) {
                boolean matches = false;
                switch (category) {
                    case "Detail":
                        matches = record.getDetail().toLowerCase().contains(searchText);
                        break;
                    case "Type":
                        matches = record.getType().toLowerCase().contains(searchText);
                        break;
                    case "Amount":
                        matches = record.getAmount().toLowerCase().contains(searchText);
                        break;
                    case "Date":
                        matches = record.getDate().toLowerCase().contains(searchText);
                        break;
                }

                if (matches) {
                    filteredData.add(record);
                }
            }
        }

        return filteredData;
    }
}
