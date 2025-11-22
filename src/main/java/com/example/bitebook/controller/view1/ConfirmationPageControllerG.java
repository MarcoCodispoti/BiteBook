package com.example.bitebook.controller.view1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConfirmationPageControllerG{

        @FXML
        private Button backToHomePageButton;

        @FXML
        private Label errorLabel;

        @FXML
        void clickedOnBackToHomePage(ActionEvent event) {
            FxmlLoader.setPage("ClientHomePage");
        }

}
