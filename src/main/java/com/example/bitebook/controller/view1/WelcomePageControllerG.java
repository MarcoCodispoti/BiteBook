package com.example.bitebook.controller.view1;

import javafx.fxml.FXML;


public class WelcomePageControllerG {

    @FXML
    public void clickedOnLogin(){
        FxmlLoader.setPage("LoginPage");
    }

    @FXML
    public void clickedOnProceedAsGuest(){
        FxmlLoader.setPage("ClientHomePage");
    }
}
