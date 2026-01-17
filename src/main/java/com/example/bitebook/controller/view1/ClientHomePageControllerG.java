package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExploreController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientHomePageControllerG{


    private static final Logger logger = Logger.getLogger(ClientHomePageControllerG.class.getName());


    @FXML
    private TextField insertCityTextField;
    @FXML
    private Label messageLabel;


    private final ExploreController menuExploreController = new ExploreController();
    private final LoginController loginController = new LoginController();



    @FXML
    void handleRequests() {
        if (checkLoginStatus()) {
            FxmlLoader.setPage("ClientRequestsPage");
        }
    }



    @FXML
    void handleAllergies() {
        if (checkLoginStatus()) {
            FxmlLoader.setPage("AllergiesPage");
        }
    }



    @FXML
    void handleLogout() {
        loginController.logout();
        FxmlLoader.setPage("WelcomePage");
    }



    @FXML
    void handleFindChefs(){
        displayMessage("");
        String insertedCity = insertCityTextField.getText();
        ChefBean chefBean = new ChefBean();
        chefBean.setCity(insertedCity);
        if(!checkCityField()){
            return;
        }
        if (!chefBean.validateCity()){
            displayMessage("Please insert a valid city");
            return;
        }
        try {
            boolean chefFound = menuExploreController.areChefsAvailableInCity(insertedCity);
            if (!chefFound) {
                displayMessage("No chef found in " + chefBean.getCity() + "!");
                return;
            }
            SelectChefPageControllerG nextController = FxmlLoader.setPageAndReturnController("SelectChefPage");
            if (nextController != null){
                nextController.initData(insertedCity);
            }
        } catch (FailedSearchException e){
            displayMessage("System Error while searching.");
            logger.log(Level.SEVERE, "System Error while searching.", e);
        }
    }



    private boolean checkLoginStatus() {
        if (menuExploreController.isLoggedClient()) {
            return true;
        } else {
            displayMessage("You must be logged in");
            return false;
        }
    }



    private boolean checkCityField(){
        if(insertCityTextField.getText().isEmpty()){
            displayMessage("Please enter a city");
            return false;
        }
        return true;
    }



    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
