package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.bean.LoginBean;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.singleton.LoggedUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.security.auth.login.FailedLoginException;

import static com.example.bitebook.model.enums.Role.CHEF;
import static com.example.bitebook.model.enums.Role.CLIENT;

public class LoginPageControllerG {

    LoginBean loginBean =  new LoginBean();
    LoginController loginController = new LoginController();

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;


    @FXML
    private Label errorLabel;


    @FXML
    public void clickedOnBack() {
        FxmlLoader.setPage("WelcomePage");
    }


    @FXML
    public void clickedOnLogin(ActionEvent actionEvent){
        Role actualRole = null;
        if(!checkInput()){
            return;
        }
        // errorLabel.setText("Input format is valid");
        try {
            // LoginController loginController = new LoginController();
            loginController.authenticate(loginBean);
            actualRole = LoggedUser.getInstance().getRole();
            errorLabel.setText("Il tuo ruolo è: " + actualRole);
        } catch (WrongCredentialsException e){
            errorLabel.setText("Credentials are incorrect.");
            return;
        } catch (FailedSearchException e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            errorLabel.setText("Login Error");
            return;
        }
        if(actualRole == CLIENT ){
            FxmlLoader.setPage("ClientHomePage");
        } else if(actualRole == CHEF){
            FxmlLoader.setPage("ChefHomePage");
        } else{
            errorLabel.setText("Something went wrong :(");
        }
    }

    private boolean checkInput(){
        if(checkEmptyFields()){
            loginBean.setEmail(emailTextField.getText());
            loginBean.setPassword(passwordTextField.getText());

            if(!loginBean.validateEmail()){
                errorLabel.setVisible(true); errorLabel.setText("Invalid email format");
                return false;
            }
            if(!loginBean.validatePassword()){
                errorLabel.setVisible(true); errorLabel.setText("Invalid password format");
                return false;
            }
            return true;
        }
        return false;
    }


    private boolean checkEmptyFields(){
        if(emailTextField.getText().isEmpty()){
            errorLabel.setVisible(true); errorLabel.setText("email field is empty");
            return false;
        }
        if(passwordTextField.getText().isEmpty()){
            errorLabel.setVisible(true); errorLabel.setText("password field is empty");
            return false;
        }
        return true;
    }

}
