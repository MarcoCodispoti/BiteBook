package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ClientHomePageControllerG2{


    private final ExplorationController explorationController = new ExplorationController();

    @FXML private TextField cityTextField;
    @FXML private Label errorLabel;

    @FXML
    void clickedOnAllergies() {
        navigateToIfLogged("AllergiesPage2", "You must be logged in to view Allergies");
    }

    @FXML
    void clickedOnRequests() {
        navigateToIfLogged("ClientRequestsPage2", "You must be logged in to view Requests");
    }

    @FXML
    void clickedOnLogout() {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader2.setPage("LoginPage2");
    }

    @FXML
    void clickedOnBook(){
        errorLabel.setText("");
        errorLabel.setVisible(false);

        String cityInput = cityTextField.getText().trim();

        if (cityInput.isEmpty()) {
            showError("Please insert a city first");
            return;
        }

        ChefBean searchBean = new ChefBean();
        searchBean.setCity(cityInput);

        if (!searchBean.validateCity()) {
            showError("You inserted an invalid city name");
            return;
        }


        try {
            boolean chefFound = explorationController.checkCityChefs(searchBean);

            if (!chefFound) {
                showError("No chef found in the inserted city!");
                return;
            }

            SelectMenuPageControllerG2 controller = FxmlLoader2.setPageAndReturnController("SelectMenuPage2");
            if (controller != null) {
                controller.initData(searchBean);
            }

        } catch (FailedSearchException e) {
            showError("System Error: Unable to search. Please try again later");
        }
    }


    private void navigateToIfLogged(String pageName, String errorMessage) {
        errorLabel.setVisible(false);
        if (explorationController.isLoggedClient()) {
            FxmlLoader2.setPage(pageName);
        } else {
            showError(errorMessage);
        }
    }


    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



}
