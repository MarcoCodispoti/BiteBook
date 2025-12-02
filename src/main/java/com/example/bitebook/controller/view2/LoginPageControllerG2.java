package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.bean.LoginBean;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.singleton.LoggedUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.security.auth.login.FailedLoginException;

public class LoginPageControllerG2{

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
    private Hyperlink guestHyperlink;


    LoginBean loginBean;


    @FXML
    void clickedOnBack(ActionEvent event) {

    }


    @FXML
    void clickedOnLogin(ActionEvent event){
        Role actualRole = null;
        if(!areValidCredentials()){
            return;
        }
        try{
            LoginController loginController = new LoginController();
            loginController.authenticate(loginBean);
            actualRole = LoggedUser.getInstance().getRole();  // o sostituire con un metodo del LoginController
        }catch(WrongCredentialsException e){
            errorLabel.setText("Incorrect username or password."); return;
        } catch(FailedSearchException e){
            errorLabel.setText("Something went wrong");    return;
        }
        switch(actualRole){
            case CLIENT -> FxmlLoader2.setPage("ClientHomePage2");
            case CHEF -> FxmlLoader2.setPage("ChefHomePage2");
        }
        errorLabel.setText("Login system error");
    }

    @FXML
    void clickedOnProceedAsGuest(ActionEvent event) {
        FxmlLoader2.setPage("ClientHomePage2");
    }


    public boolean areValidCredentials() {
        this.loginBean = new LoginBean(emailTextField.getText(), passwordTextField.getText());
        if(emailTextField.getText().isEmpty()){
            errorLabel.setText("Please enter your email first"); return  false;
        }
        if(passwordTextField.getText().isEmpty()){
            errorLabel.setText("Please enter your password first");  return  false;
        }
        if(!loginBean.validateEmail()){
            errorLabel.setText("Please insert a valid email format");  return  false;
        }
        if(!loginBean.validatePassword()){
            errorLabel.setText("Please enter a valid password");   return  false;
        }
        return true;
    }
}
