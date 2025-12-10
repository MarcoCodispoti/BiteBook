package com.example.bitebook.controller.view1;

import com.example.bitebook.util.ViewsResourcesPaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FxmlLoader extends Application {

    private static final Logger logger = Logger.getLogger(FxmlLoader.class.getName());


    private static Stage stage;
    @Override
    @SuppressWarnings("java:S2696")
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;

            stage.setTitle("BiteBook");
            stage.setResizable(false);

            loadDockIcon();
            setPage("WelcomePage");

            stage.show();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Critical Error: Unable to start the application", e);
        }
    }


    public static void setPage(String fileName){
        try {
            FXMLLoader loader = loadFxml(fileName);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage.setScene(scene);
        } catch (IOException e){
            logger.log(Level.SEVERE, "Error: Unable to change page", e);
        }
    }


    public static <T> T setPageAndReturnController(String fileName) {
        try {
            FXMLLoader loader = loadFxml(fileName);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage.setScene(scene);

            return loader.getController();

        } catch (IOException e){
            logger.log(Level.SEVERE, "Error while changing page with controller ", e);
            return null;
        }
    }


    private static FXMLLoader loadFxml(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("xmfl file name cannot be null or empty");
        }

        String fullPath = ViewsResourcesPaths.VIEW_PACKAGE_PATH + fileName + ".fxml";
        URL fileUrl = FxmlLoader.class.getResource(fullPath);

        if (fileUrl == null) {
            throw new IOException("XML file not found: " + fullPath);
        }
        return new FXMLLoader(fileUrl);
    }

    private void loadDockIcon() {
        try {
            URL iconUrl = getClass().getResource(ViewsResourcesPaths.VIEW_ICON_PATH);
            if (iconUrl != null){
                if (Taskbar.isTaskbarSupported()) {
                    var taskbar = Taskbar.getTaskbar();

                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        java.awt.Image awtImage = Toolkit.getDefaultToolkit().getImage(iconUrl);
                        taskbar.setIconImage(awtImage);
                    }
                }
            } else {
                logger.warning("Icon not found in the path: " + ViewsResourcesPaths.VIEW_ICON_PATH);
            }
        } catch (Exception e){
            logger.log(Level.WARNING, "Error while loading icon", e);
        }
    }



}
