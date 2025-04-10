package software;

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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PageTwo {
    private Stage stage;
    private PageOne pageOne;
    private TableView<Record> table = new TableView<>();

    public PageTwo(Stage stage) {
        this.stage = stage;
    }

    public void setPageOne(PageOne pageOne) {
        this.pageOne = pageOne;
    }

    public Scene getScene() {
        // 左侧
        VBox leftPane = new VBox(20);
        leftPane.setStyle("-fx-background-color:rgb(238, 223, 207);");
        leftPane.setPadding(new Insets(20));

        Label title = new Label("Tally app");
        title.setFont(new Font("Arial", 20));
        title.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
        // 点击Tally app回到主页面
        title.setOnMouseClicked(e -> {
            stage.setTitle("main page");
            stage.setScene(pageOne.getScene());
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
            });

            menuList.getChildren().add(item);
        }

        leftPane.getChildren().addAll(title, menuList);

        // 右侧
        // 右上
        VBox rightPane = new VBox();
        VBox topSection = new VBox(10);
        topSection.setStyle("-fx-background-color:rgb(239, 230, 220);");

        Label titleLabel = new Label("Transaction History");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Task", "Detail", "Type", "Amount", "Date");
        comboBox.setPromptText("Please select an option.");
        comboBox.setStyle(
                "-fx-background-color: white;" + // 背景白色
                        "-fx-border-color: #ccc;" + // 灰色边框
                        "-fx-border-radius: 8;" + // 圆角
                        "-fx-background-radius: 8;" + // 背景圆角
                        "-fx-padding: 5 10 5 10;" + // 内边距
                        "-fx-font-size: 14px;" // 字体大小
        );
        TextField searchField = new TextField();
        searchField.setPromptText("Search tickets...");
        searchField.setPrefHeight(36);
        searchField.setPrefWidth(600);
        searchField.setStyle(
                "-fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #ccc; -fx-padding: 0 10;");

        Button filterButton = new Button("\uD83D\uDD0D Filter");
        filterButton.setStyle(
                "-fx-background-color: #F5F5F5; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 6 12; -fx-text-fill: #555;");

        HBox searchBar = new HBox(10, comboBox, searchField, filterButton);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        topSection.getChildren().addAll(titleLabel, searchBar);
        topSection.setPadding(new Insets(20, 0, 10, 10));

        // 右下
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(new Label(""));
        TableColumn<Record, String> taskCol = new TableColumn<>("Task");// 创建列
        taskCol.setCellValueFactory(new PropertyValueFactory<>("task"));// 返回值
        taskCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableColumn<Record, String> detailCol = new TableColumn<>("Detail");
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableColumn<Record, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableColumn<Record, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableColumn<Record, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-text-fill: #333;");

        if (table.getColumns().isEmpty()) {
            table.getColumns().addAll(taskCol, detailCol, typeCol, amountCol, dateCol);
        }

        table.setStyle("""
                    -fx-background-color: white;
                    -fx-table-cell-border-color: transparent;
                    -fx-border-color: transparent;
                    -fx-font-size: 14px;
                """);

        table.setRowFactory(tv -> {
            return new TableRow<>() {
                @Override
                protected void updateItem(Record item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("-fx-background-color: white;");
                    } else {
                        setStyle("-fx-background-color: white; -fx-padding: 12 10;");
                        setOnMouseEntered(e -> setStyle("-fx-background-color: #f8f8f8; -fx-padding: 12 10;"));
                        setOnMouseExited(e -> setStyle("-fx-background-color: white; -fx-padding: 12 10;"));
                    }
                }
            };
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

        return new Scene(rootLayout, 1300, 800);
    }

    private ObservableList<Record> loadCSVData(String month) {
        ObservableList<Record> data = FXCollections.observableArrayList();
        String filePath = "data/" + month + ".csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // 跳过第一行标题

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    data.add(new Record(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
