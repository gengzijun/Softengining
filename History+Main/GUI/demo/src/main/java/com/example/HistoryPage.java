package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HistoryPage {

    private static TableView<Record> table;
    private static ObservableList<Record> originalData;
    private static ComboBox<String> comboBox;
    private static TextField searchField;

    public static Scene getScene(Stage stage) {
        // 窗口标题
        stage.setTitle("History");

        // —— 左侧菜单 —— 
        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: rgb(248, 236, 220);");
        leftPane.setPrefWidth(170);

        Label appTitle = new Label("TallyAPP");
        appTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        appTitle.setOnMouseClicked(e -> {
            stage.setTitle("TallyAPP");
            stage.setScene(MainPage.getMainScene(stage));
        });
        leftPane.getChildren().add(appTitle);

        for (String m : Month.month_list) {
            HBox item = new HBox(8);
            item.setPadding(new Insets(8));
            item.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #ddd;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;" +
                "-fx-cursor: hand;"
            );
            Circle dot = new Circle(6, Color.LIGHTGRAY);
            Label lbl = new Label(m);
            lbl.setStyle("-fx-font-size: 12px;");
            item.getChildren().addAll(dot, lbl);
            item.setOnMouseClicked(e -> {
                ObservableList<Record> newData = loadCSVData(m);
                originalData.setAll(newData);
                table.setItems(originalData);
                comboBox.setValue("All information");
                searchField.clear();
            });
            leftPane.getChildren().add(item);
        }

        // —— 右侧顶部：标题 & 筛选 —— 
        Label headerLabel = new Label("Transaction History");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox headerBar = new HBox(headerLabel);
        headerBar.setAlignment(Pos.CENTER_LEFT);
        headerBar.setPadding(new Insets(10, 0, 5, 10));

        comboBox = new ComboBox<>();
        comboBox.getItems().addAll("All information", "Task", "Detail", "Type", "Amount", "Date");
        comboBox.setValue("All information");
        comboBox.setPrefWidth(150);

        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(300);

        Button filterBtn = new Button("🔍");
        filterBtn.setOnAction(e -> {
            String cat = comboBox.getValue();
            String txt = searchField.getText().toLowerCase();
            table.setItems(filterData(cat, txt));
        });

        HBox filterBar = new HBox(10, comboBox, searchField, filterBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(0, 0, 10, 10));

        VBox topSection = new VBox(headerBar, filterBar);
        topSection.setStyle("-fx-background-color: white;");

        // —— 表格初始化 —— 
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No records"));

        TableColumn<Record, String> taskCol   = new TableColumn<>("Task");
        TableColumn<Record, String> detailCol = new TableColumn<>("Detail");
        TableColumn<Record, String> typeCol   = new TableColumn<>("Type");
        TableColumn<Record, String> amountCol = new TableColumn<>("Amount");
        TableColumn<Record, String> dateCol   = new TableColumn<>("Date");

        taskCol .setCellValueFactory(new PropertyValueFactory<>("task"));
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        typeCol .setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol .setCellValueFactory(new PropertyValueFactory<>("date"));

        // 列样式保持与 MainPage 一致
        for (TableColumn<?,?> col : new TableColumn[]{taskCol, detailCol, typeCol, amountCol, dateCol}) {
            col.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");
        }

        // —— 自定义 amount 列：Income 用“+￥”，其它类型统一用“-￥” —— 
        amountCol.setCellFactory(col -> new TableCell<Record, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setText(null);
                } else {
                    Record rec = getTableView().getItems().get(getIndex());
                    String type = rec.getType();
                    String prefix = type.equalsIgnoreCase("Income") ? "+￥" : "-￥";
                    setText(prefix + item.trim());
                    setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");
                }
            }
        });

        table.getColumns().setAll(taskCol, detailCol, typeCol, amountCol, dateCol);

        originalData = FXCollections.observableArrayList();
        table.setItems(originalData);

        // —— 右侧整体布局 —— 
        VBox tableBox = new VBox(table);
        tableBox.setPadding(new Insets(0, 10, 10, 10));
        tableBox.setStyle("-fx-background-color: white;");
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox rightPane = new VBox(topSection, tableBox);
        VBox.setVgrow(tableBox, Priority.ALWAYS);

        // —— 根布局 —— 
        HBox root = new HBox(leftPane, rightPane);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        return new Scene(root, 900, 600);
    }

    /**
     * 从 resources/data/<month>.csv 读取
     */
    private static ObservableList<Record> loadCSVData(String month) {
        ObservableList<Record> list = FXCollections.observableArrayList();
        String resourcePath = "/data/" + month + ".csv";
        try (InputStream is = HistoryPage.class.getResourceAsStream(resourcePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine();  // 跳过表头
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 5) {
                    list.add(new Record(p[0], p[1], p[2], p[3], p[4]));
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static ObservableList<Record> filterData(String category, String text) {
        ObservableList<Record> filtered = FXCollections.observableArrayList();
        String t = text == null ? "" : text.toLowerCase();
        for (Record r : originalData) {
            boolean ok;
            if ("All information".equals(category)) {
                ok = r.getTask().toLowerCase().contains(t)
                   || r.getDetail().toLowerCase().contains(t)
                   || r.getType().toLowerCase().contains(t)
                   || r.getAmount().toLowerCase().contains(t)
                   || r.getDate().toLowerCase().contains(t);
            } else {
                switch (category) {
                    case "Task":   ok = r.getTask().toLowerCase().contains(t); break;
                    case "Detail": ok = r.getDetail().toLowerCase().contains(t); break;
                    case "Type":   ok = r.getType().toLowerCase().contains(t); break;
                    case "Amount": ok = r.getAmount().toLowerCase().contains(t); break;
                    case "Date":   ok = r.getDate().toLowerCase().contains(t); break;
                    default:       ok = false;
                }
            }
            if (ok) filtered.add(r);
        }
        return filtered;
    }
}
