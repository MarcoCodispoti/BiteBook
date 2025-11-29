package com.example.bitebook.controller.view1;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;


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
