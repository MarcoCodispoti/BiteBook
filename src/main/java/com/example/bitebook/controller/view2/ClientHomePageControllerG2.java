package com.example.bitebook.controller.view2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

public class ClientHomePageControllerG2{

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private TextField cityTextField;

    @FXML
    private Button bookButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    void clickedOnAllergies(ActionEvent event) {

    }

    @FXML
    void clickedOnRequests(ActionEvent event) {

    }

    @FXML
    void clickedOnLogout(ActionEvent event) {
        FxmlLoader2.setPage("LoginPage2");
    }

    @FXML
    void clickedOnBook(ActionEvent event) {

    }

}
