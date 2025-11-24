package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.controller.view1.SelectChefPageControllerG;
import com.example.bitebook.model.bean.ChefBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
    private Label errorLabel;


    ChefBean chefBean;


    @FXML
    void clickedOnAllergies(ActionEvent event) {

    }

    @FXML
    void clickedOnRequests(ActionEvent event) {

    }

    @FXML
    void clickedOnLogout(ActionEvent event) {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader2.setPage("LoginPage2");
    }

    @FXML
    void clickedOnBook(ActionEvent event) {
        chefBean = new ChefBean();
        if(!cityTextField.getText().isEmpty()){
            errorLabel.setText("Please insert a city first");
        }
        chefBean.setCity(cityTextField.getText());
        if(!chefBean.validateCity()){
            errorLabel.setText("You inserted an invalid city");
        }
        ExplorationController explorationController = new ExplorationController();
        SelectMenuPageControllerG2 selectMenuPageControllerG2 = null;
        if(explorationController.checkCityChefs(chefBean)){
            selectMenuPageControllerG2 = FxmlLoader2.setPageAndReturnController("SelectMenuPage2");
        }
        if(selectMenuPageControllerG2!=null){
            selectMenuPageControllerG2.initData(chefBean);
        }

        chefBean.setCity(cityTextField.getText());
    }

}
