package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientHomePageControllerG2{

    private static final Logger logger = Logger.getLogger(ClientHomePageControllerG2.class.getName());


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
            displayError("Please insert a city first");
            return;
        }

        ChefBean searchBean = new ChefBean();
        searchBean.setCity(cityInput);

        if (!searchBean.validateCity()) {
            displayError("You inserted an invalid city name");
            return;
        }
        try {
            boolean chefFound = explorationController.areChefsAvailableInCity(searchBean);

            if (!chefFound) {
                displayError("No chef found in the inserted city!");
                return;
            }

            SelectMenuPageControllerG2 controller = FxmlLoader2.setPageAndReturnController("SelectMenuPage2");
            if (controller != null) {
                controller.initData(searchBean);
            }
        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Error while searching chefs in city" ,e);
            displayError("System Error: Unable to search. Please try again later");
        }
    }


    private void navigateToIfLogged(String pageName, String errorMessage) {
        errorLabel.setVisible(false);
        if (explorationController.isLoggedClient()) {
            FxmlLoader2.setPage(pageName);
        } else {
            displayError(errorMessage);
        }
    }


    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



}
