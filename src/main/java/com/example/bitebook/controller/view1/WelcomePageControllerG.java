package com.example.bitebook.controller.view1;

import javafx.fxml.FXML;


public class WelcomePageControllerG {



    @FXML
    public void handleLogin(){
        FxmlLoader.setPage("LoginPage");
    }



    @FXML
    public void handleProceedAsGuest(){
        FxmlLoader.setPage("ClientHomePage");
    }


}
