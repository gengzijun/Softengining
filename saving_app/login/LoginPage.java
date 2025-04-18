package saving_app.login;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;

/** 登录页 —— 普通页面类 */
public class LoginPage {

    private final Stage stage;
    private final Scene scene;
    private MainPageGate mainPage; // 注入 MainPage 的桥

    /* ---------- 构造 ---------- */
    public LoginPage(Stage stage) {
        this.stage = stage;
        this.scene = buildScene();
    }

    /* ---------- 对外接口 ---------- */
    public Scene getScene() {
        return scene;
    }

    public void setMainPage(MainPageGate p) {
        this.mainPage = p;
    }

    /* ---------- 页面搭建 ---------- */
    private Scene buildScene() {

        /* 1. 标题 */
        Label title = new Label("Welcome to TallyAPP");
        title.setStyle("""
                -fx-font-family: 'Segoe UI', sans-serif;
                -fx-font-size: 28px;
                -fx-font-weight: bold;
                -fx-text-fill: #2c3e50;""");
        title.setPadding(new Insets(0, 0, 20, 0));

        /* 2. 邮箱 & 密码输入行 */
        HBox emailBox = createIconInput("/saving_app/login/images/email.png", "Email", false);
        TextField emailField = (TextField) emailBox.getChildren().get(1);

        HBox passBox = createIconInput("/saving_app/login/images/lock.png", "Password", true);
        PasswordField passField = (PasswordField) passBox.getChildren().get(1);

        /* 3. 登录按钮 */
        Button loginBtn = new Button("Login");
        setBtnStyle(loginBtn, "#3498db");
        VBox.setMargin(loginBtn, new Insets(20, 0, 0, 0));

        loginBtn.setOnMouseEntered(e -> setBtnStyle(loginBtn, "#2980b9"));
        loginBtn.setOnMouseExited(e -> setBtnStyle(loginBtn, "#3498db"));

        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String pass = passField.getText();

            if (!isValidEmail(email)) {
                showAlert("Invalid Email", "Please enter a valid email address.");
                return;
            }
            if (authenticate(email, pass)) {
                if (mainPage != null) { // ★ 切换到主页面
                    stage.setScene(mainPage.getScene());
                    stage.setTitle("main page");
                }
            } else {
                showAlert("Login Failed", "Incorrect email or password.");
            }
        });

        /* 4. 辅助链接 */
        Label forgot = createLink("Forgot password?", "Feature Not Implemented", "Coming soon...");
        Label register = createLink("Register", "Feature Not Implemented", "Registration coming soon...");

        HBox bottomLinks = new HBox(10, forgot, register);
        bottomLinks.setAlignment(Pos.CENTER);
        bottomLinks.setPadding(new Insets(20, 0, 0, 0));

        /* 5. 卡片容器 */
        VBox card = new VBox(20, title, emailBox, passBox, loginBtn, bottomLinks);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(450);
        card.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 20;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);""");

        /* 6. 背景图片 */
        ImageView bgView = new ImageView(loadImage("/saving_app/login/images/background.png"));
        bgView.setFitWidth(800);
        bgView.setFitHeight(600);
        bgView.setEffect(new BoxBlur(5, 5, 3));

        /* 7. 根布局 */
        Pane root = new Pane(bgView, card);

        /* 动态居中卡片 */
        stage.widthProperty().addListener((obs, o, n) -> card.setLayoutX((n.doubleValue() - card.getMaxWidth()) / 2));
        stage.heightProperty().addListener((obs, o, n) -> card.setLayoutY((n.doubleValue() - card.getHeight()) / 2));
        card.setLayoutX((800 - card.getMaxWidth()) / 2);
        card.setLayoutY((600 - card.getHeight()) / 2);

        /* 8. 淡入动画 */
        FadeTransition fade = new FadeTransition(Duration.seconds(1.0), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        return new Scene(root, 800, 600);
    }

    /* ---------- 工具方法，与原版一致 ---------- */

    private HBox createIconInput(String iconPath, String prompt, boolean isPassword) {
        ImageView icon = new ImageView(loadImage(iconPath));
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        HBox.setMargin(icon, new Insets(0, 15, 0, 0));

        TextInputControl input = isPassword ? new PasswordField() : new TextField();
        input.setPromptText(prompt);
        styleInput(input);

        HBox box = new HBox(icon, input);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(350);
        HBox.setHgrow(input, Priority.ALWAYS);
        return box;
    }

    private void styleInput(Control c) {
        c.setStyle(baseInput("#bdc3c7"));
        c.setOnMouseEntered(e -> c.setStyle(baseInput("#3498db")));
        c.setOnMouseExited(e -> c.setStyle(baseInput("#bdc3c7")));
    }

    private String baseInput(String borderColor) {
        return "-fx-font-family:'Segoe UI';-fx-background-radius:20;-fx-padding:12;"
                + "-fx-border-color:" + borderColor + ";-fx-border-width:1;-fx-border-radius:20;";
    }

    private void setBtnStyle(Button b, String color) {
        b.setStyle("""
                -fx-font-family:'Segoe UI';-fx-background-color:%s;-fx-text-fill:white;
                -fx-font-size:16;-fx-font-weight:bold;-fx-background-radius:25;-fx-padding:12 25;""".formatted(color));
        b.setMaxWidth(Double.MAX_VALUE);
    }

    private Label createLink(String text, String title, String msg) {
        Label link = new Label(text);
        link.setStyle("""
                -fx-font-family:'Segoe UI';-fx-text-fill:#7f8c8d;-fx-underline:true;-fx-cursor:hand;""");
        link.setOnMouseClicked(e -> showAlert(title, msg));
        link.setOnMouseEntered(e -> link.setTextFill(Color.web("#3498db")));
        link.setOnMouseExited(e -> link.setTextFill(Color.web("#7f8c8d")));
        return link;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^\\S+@\\S+\\.\\S+$");
    }

    private boolean authenticate(String e, String p) {
        return "admin@163.com".equalsIgnoreCase(e.trim()) && "admin".equals(p);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private Image loadImage(String path) {
        InputStream in = LoginPage.class.getResourceAsStream(path);
        if (in == null)
            throw new IllegalStateException("Resource not found: " + path);
        return new Image(in);
    }

    /* ========== 供 SwitchApp 注入的简单接口 ========== */
    public interface MainPageGate {
        Scene getScene();
    }
}
