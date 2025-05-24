module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.example.app.model to com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.databind;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires langchain4j.open.ai;
    requires io.github.cdimascio.dotenv.java;
    requires com.opencsv;
    opens com.example.app.history to javafx.base; // 开放 com.example.app.history 包给 javafx.base 模块
    opens com.example.app.main to javafx.graphics; // 如果需要，开放其他包给其他模块
    opens com.example.app.model to javafx.base;
    opens com.example.app to javafx.fxml;
    exports com.example.app;
}