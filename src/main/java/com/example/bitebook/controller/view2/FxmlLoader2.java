package com.example.bitebook.controller.view2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class FxmlLoader2 extends Application {
    private static Stage stage2;
    private static final String fxmlPath = "/com/example/bitebook/view2/";

    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage stage;
        FXMLLoader fxmlloader = new FXMLLoader(FxmlLoader2.class.getResource(fxmlPath + "LoginPage2.fxml"));
        Scene scene = new Scene(fxmlloader.load(), 1200, 740);
        stage = primaryStage;
        stage.setTitle("BiteBook v2");
        stage.setResizable(false);
        stage.setScene(scene);
        setScene(stage);
        stage.show();
    }

    private static void setScene(Stage stage) {
        stage2 = stage;
    }

    public static void setPage(String fileName) {
        URL fileUrl = FxmlLoader2.class.getResource(fileName + ".fxml");
        FXMLLoader loader = new FXMLLoader(fileUrl);
        assert fileUrl != null;
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage2.setScene(scene);
        } catch (IOException _) {
            //not handled
        }
    }
}
