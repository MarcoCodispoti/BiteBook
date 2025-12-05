package com.example.bitebook.controller.view1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class FxmlLoader extends Application {
    
    private static Stage stage;
    private static final String VIEW_PATH = "/com/example/bitebook/view1/";
    private static final String ICON_PATH = "/com/example/bitebook/images/icon1.png"; // Percorso icona

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;

            stage.setTitle("BiteBook");
            stage.setResizable(false);

            loadDockIcon();
            setPage("WelcomePage");

            stage.show();
        } catch (Exception e) {
            System.err.println("Critical error while starting the application:" + e.getMessage());
        }
    }


    public static void setPage(String fileName){
        try {
            FXMLLoader loader = loadFxml(fileName);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage.setScene(scene);
        } catch (IOException e){
            System.err.println("Error while changing page: " + fileName);
        }
    }


    public static <T> T setPageAndReturnController(String fileName) {
        try {
            FXMLLoader loader = loadFxml(fileName);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage.setScene(scene);

            return loader.getController();

        } catch (IOException e) {
            System.err.println("Error while changing page with controller: " + fileName);
            return null;
        }
    }


    private static FXMLLoader loadFxml(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("xmfl file name cannot be null or empty");
        }

        String fullPath = VIEW_PATH + fileName + ".fxml";
        URL fileUrl = FxmlLoader.class.getResource(fullPath);

        if (fileUrl == null) {
            throw new IOException("XML file not found: " + fullPath);
        }
        return new FXMLLoader(fileUrl);
    }

    private void loadDockIcon() {
        try {
            URL iconUrl = getClass().getResource(ICON_PATH);
            if (iconUrl != null){
                if (Taskbar.isTaskbarSupported()) {
                    var taskbar = Taskbar.getTaskbar();

                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        java.awt.Image awtImage = Toolkit.getDefaultToolkit().getImage(iconUrl);
                        taskbar.setIconImage(awtImage);
                    }
                }
            } else {
                System.err.println("Icon not found in the path: " + ICON_PATH);
            }
        } catch (Exception e){
            // ignore
        }
    }



}
