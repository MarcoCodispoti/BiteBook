package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
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


    private final ExplorationController explorationController = new ExplorationController();
    private final LoginController loginController = new LoginController();

    @FXML private TextField insertCityTextField;
    @FXML private Label errorLabel;



    @FXML
    void clickedOnRequests() {
        if (checkLoginStatus()) {
            FxmlLoader.setPage("ClientRequestsPage");
        }
    }

    @FXML
    void clickedOnAllergies() {
        if (checkLoginStatus()) {
            FxmlLoader.setPage("AllergiesPage");
        }
    }

    @FXML
    void clickedOnLogout() {
        loginController.logout();
        FxmlLoader.setPage("WelcomePage");
    }



    @FXML
    void clickedOnFindChefs(){
        ChefBean chefBean = new ChefBean();
        chefBean.setCity(insertCityTextField.getText());
        if(!checkCityField()){
            return;
        }
        if (!chefBean.validateCity()){
            errorLabel.setText("Please insert a valid city");
            return;
        }
        try {
            boolean chefFound = explorationController.checkCityChefs(chefBean);
            if (!chefFound) {
                errorLabel.setText("No chef found in " + chefBean.getCity() + "!");
                return;
            }
            SelectChefPageControllerG nextController = FxmlLoader.setPageAndReturnController("SelectChefPage");
            if (nextController != null){
                nextController.initData(chefBean);
            }
        } catch (FailedSearchException e){
            errorLabel.setText("System Error while searching.");
            logger.log(Level.SEVERE, "System Error while searching.", e);
        }
    }


    private boolean checkLoginStatus() {
        if (explorationController.isLoggedClient()) {
            return true;
        } else {
            errorLabel.setText("You must be logged in");
            return false;
        }
    }


    public boolean checkCityField(){
        if(insertCityTextField.getText().isEmpty()){
            errorLabel.setText("Please enter a city");
            return false;
        }
        return true;
    }


}
