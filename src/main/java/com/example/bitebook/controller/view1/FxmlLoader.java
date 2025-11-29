package com.example.bitebook.controller.view1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;

public class FxmlLoader extends Application {
    private static Stage stage1;
    private static final String fxmlPath = "/com/example/bitebook/view1/";

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(FxmlLoader.class.getResource(fxmlPath + "WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 740);
//        stage = primaryStage;
//        stage.setTitle("BiteBook");
//        stage.setResizable(false);
//        stage.setScene(scene);
//        setScene(stage);
//        stage.show();

        // aggiunto dopo al posto di quello sopra commentato
        primaryStage.setTitle("BiteBook");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        setScene(primaryStage);
        primaryStage.show();
    }



    private static void setScene(Stage stage){stage1 = stage;}

    public static void setPage(String fileName){
        try{
            URL fileUrl = FxmlLoader.class.getResource(fxmlPath + fileName + ".fxml");
            FXMLLoader loader = new FXMLLoader(fileUrl);

            // assert fileUrl != null;

            // aggiunto dopo
            if(fileName == null){
                throw new FileNotFoundException();
            }


            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage1.setScene(scene);
        } catch(Exception e){
            // to be handled
        }
    }


    public static <T> T setPageAndReturnController(String fileName) {
        try {
            URL fileUrl = FxmlLoader.class.getResource(fxmlPath + fileName + ".fxml");
            if (fileUrl == null) {
                throw new FileNotFoundException("FXML file not found: " + fileName + ".fxml");
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage1.setScene(scene);

            return loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
