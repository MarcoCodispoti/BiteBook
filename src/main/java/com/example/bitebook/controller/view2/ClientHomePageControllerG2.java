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


    @FXML
    private TextField cityTextField;
    @FXML
    private Label messageLabel;


    private final ExplorationController menuExplorationController = new ExplorationController();



    @FXML
    void handleAllergies() {
        navigateToIfLogged("AllergiesPage2", "You must be logged in to view Allergies");
    }



    @FXML
    void handleRequests() {
        navigateToIfLogged("ClientRequestsPage2", "You must be logged in to view Requests");
    }



    @FXML
    void handleLogout() {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader2.setPage("LoginPage2");
    }



    @FXML
    void handleBook(){
        messageLabel.setText("");
        messageLabel.setVisible(false);

        String cityInput = cityTextField.getText().trim();

        if (cityInput.isEmpty()) {
            displayMessage("Please insert a city first");
            return;
        }

        ChefBean searchBean = new ChefBean();
        searchBean.setCity(cityInput);

        if (!searchBean.validateCity()) {
            displayMessage("You inserted an invalid city name");
            return;
        }
        try {
            boolean chefFound = menuExplorationController.areChefsAvailableInCity(cityInput);

            if (!chefFound) {
                displayMessage("No chef found in the inserted city!");
                return;
            }

            SelectMenuPageControllerG2 controller = FxmlLoader2.setPageAndReturnController("SelectMenuPage2");
            if (controller != null) {
                controller.initData(searchBean);
            }
        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Error while searching chefs in city" ,e);
            displayMessage("System Error: Unable to search. Please try again later");
        }
    }


    private void navigateToIfLogged(String pageName, String errorMessage) {
        messageLabel.setVisible(false);
        if (menuExplorationController.isLoggedClient()) {
            FxmlLoader2.setPage(pageName);
        } else {
            displayMessage(errorMessage);
        }
    }


    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }



}
