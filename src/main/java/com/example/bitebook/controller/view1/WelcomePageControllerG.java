package com.example.bitebook.controller.view1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;


public class WelcomePageControllerG {

    @FXML
    private Button proceedAsClientButton;

    @FXML
    private Button loginAsChefButton;

    @FXML
    void clickedOnProceedAsClient(ActionEvent event){

    }

    @FXML
    public void clickedOnLogin(ActionEvent actionEvent){
        FxmlLoader.setPage("LoginPage");
    }

    @FXML
    public void clickedOnProceedAsGuest(ActionEvent actionEvent){
        FxmlLoader.setPage("ClientHomePage");
    }
}
