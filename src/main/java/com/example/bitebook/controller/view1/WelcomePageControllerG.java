package com.example.bitebook.controller.view1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class WelcomePageControllerG {

    @FXML
    public void clickedOnLogin(ActionEvent actionEvent){
        FxmlLoader.setPage("LoginPage");
    }

    @FXML
    public void clickedOnProceedAsGuest(ActionEvent actionEvent){
        FxmlLoader.setPage("ClientHomePage");
    }
}
