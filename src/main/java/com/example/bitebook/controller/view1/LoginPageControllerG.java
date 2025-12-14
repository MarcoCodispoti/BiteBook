package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.bean.LoginBean;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.session.LoggedUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginPageControllerG {


    private static final Logger logger = Logger.getLogger(LoginPageControllerG.class.getName());


    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label messageLabel;


    private final LoginBean loginBean = new LoginBean();
    private final LoginController loginController = new LoginController();



    @FXML
    public void handleBack() {
        FxmlLoader.setPage("WelcomePage");
    }



    @FXML
    public void handleLogin(){
        messageLabel.setVisible(false);
        messageLabel.setText("");

        if(!checkEmptyFields()){
            return;
        }

        bindDataToBean();
        if (!validateBeanData()) {
            return;
        }

        try {
            loginController.authenticate(loginBean);
            Role actualRole = LoggedUser.getInstance().getRole();
            handleNavigation(actualRole);
        } catch (WrongCredentialsException _) {
            displayMessage("Incorrect Username or Password.");
        } catch (FailedSearchException e){
            logger.log(Level.WARNING, "Error while finding user with such credentials", e);
            displayMessage("System Error: Try again later");
        } catch (Exception e){
            logger.log(Level.WARNING, "System error occurred", e);
            displayMessage("Unknown Error");
        }
    }



    private void bindDataToBean(){
        loginBean.setEmail(emailTextField.getText().trim());
        loginBean.setPassword(passwordTextField.getText());
    }



    private boolean validateBeanData(){

        if (!loginBean.validateEmail()) {
            displayMessage("Invalid email format");
            return false;
        }

        if (!loginBean.validatePassword()) {
            displayMessage("Invalid password format");
            return false;
        }
        return true;
    }



    private void handleNavigation(Role role){
        switch (role){
            case CLIENT: FxmlLoader.setPage("ClientHomePage"); break;
            case CHEF: FxmlLoader.setPage("ChefHomePage"); break;
            default: displayMessage("Unrecognized role");
        }
    }



    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }



    private boolean checkEmptyFields(){
        if(emailTextField.getText().isEmpty()){
            displayMessage("Email field is empty");
            return false;
        }
        if(passwordTextField.getText().isEmpty()){
            displayMessage("Password field is empty");
            return false;
        }
        return true;
    }


}
