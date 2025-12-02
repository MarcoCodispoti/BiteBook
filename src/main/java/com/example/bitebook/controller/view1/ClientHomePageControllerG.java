package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.bean.ChefBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ClientHomePageControllerG{

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private Hyperlink profileHyperlink;

    @FXML
    private Hyperlink logoutHyperlink;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Label errorLabel;

    @FXML
    private Button findChefsButton;

    @FXML
    private TextField insertCityTextField;

    @FXML
    void clickedOnRequests(ActionEvent event) {
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("ClientRequestsPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }

    @FXML
    void clickedOnAllergies(ActionEvent event) {
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("AllergiesPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }

    @FXML
    void clickedOnLogout(ActionEvent event) {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader.setPage("WelcomePage");
    }

    @FXML
    void clickedOnFindChefs(ActionEvent event){
        ChefBean chefBean = new ChefBean();
        chefBean.setCity(insertCityTextField.getText());
        if(!checkCityField()){
            return;
        }
        if(!chefBean.validateCity()){
            errorLabel.setText("You inserted an invalid city!");
            return;
        }
        errorLabel.setText("You inserted a valid city");

        ExplorationController explorationController = new ExplorationController();
        boolean chefFound;

        try{
            chefFound = explorationController.checkCityChefs(chefBean);
        } catch(FailedSearchException e){
            errorLabel.setText("Error occurred while searching for chefs city!");
            return;
        }

        if(!chefFound){
            errorLabel.setText("No chef found in the inserted city!");
            return;
        }
        SelectChefPageControllerG selectChefPageControllerG = FxmlLoader.setPageAndReturnController("SelectChefPage");
        if(selectChefPageControllerG != null){
            System.out.println(("Passo alla schermata di scelta degli chef il bean: " + chefBean));
            selectChefPageControllerG.initData(chefBean);
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
