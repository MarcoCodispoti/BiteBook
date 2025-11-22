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
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(FxmlLoader.class.getResource(fxmlPath + "WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 740);
        stage = primaryStage;
        stage.setTitle("BiteBook");
        stage.setResizable(false);
        stage.setScene(scene);
        setScene(stage);
        stage.show();
    }



    private static void setScene(Stage stage){stage1 = stage;}

    public static void setPage(String fileName){
        URL fileUrl = FxmlLoader.class.getResource(fxmlPath + fileName + ".fxml");
        FXMLLoader loader = new FXMLLoader(fileUrl);
        assert fileUrl != null;
        try{
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 740);
            stage1.setScene(scene);
        } catch(Exception e){
            // not handled
        }
    }


    public static <T> T setPageAndReturnController(String fileName) {
        try {
            // 1. Ottiene l'URL del file FXML
            URL fileUrl = FxmlLoader.class.getResource(fxmlPath + fileName + ".fxml");
            if (fileUrl == null) {
                throw new FileNotFoundException("FXML file not found: " + fileName + ".fxml");
            }

            // 2. Inizializza il Loader
            FXMLLoader loader = new FXMLLoader(fileUrl);

            // 3. Carica il nodo radice (View)
            Parent root = loader.load();

            // 4. Crea la nuova Scena e la imposta sullo Stage statico
            Scene scene = new Scene(root, 1200, 740);
            stage1.setScene(scene);

            // 5. Restituisce il Controller, castato automaticamente al tipo T
            return loader.getController();
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento della pagina FXML: " + fileName);
            e.printStackTrace();
            return null; // Restituisce null in caso di errore
        }
    }


}
