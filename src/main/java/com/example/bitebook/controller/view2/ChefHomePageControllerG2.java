package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

public class ChefHomePageControllerG2{

    @FXML
    private Hyperlink menusHyperlink;

    @FXML
    private Button logoutButton;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Button manageRequestsButton;

    @FXML
    void clickedOnMenus(ActionEvent event) {

    }

    @FXML
    void clickedOnAllergies(ActionEvent event) {

    }

    @FXML
    void clickedOnLogoutButton(ActionEvent event) {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader2.setPage("LoginPage2");
    }

    @FXML
    void clickedOnManageRequests(ActionEvent event) {

    }

}
