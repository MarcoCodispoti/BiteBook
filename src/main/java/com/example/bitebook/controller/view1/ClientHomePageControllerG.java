package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ClientHomePageControllerG{


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
    void clickedOnFindChefs() {
        errorLabel.setText("");

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
