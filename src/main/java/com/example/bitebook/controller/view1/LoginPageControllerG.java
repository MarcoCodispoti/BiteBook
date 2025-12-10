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

import static com.example.bitebook.model.enums.Role.CHEF;
import static com.example.bitebook.model.enums.Role.CLIENT;

public class LoginPageControllerG {

    private static final Logger logger = Logger.getLogger(LoginPageControllerG.class.getName());


    private final LoginBean loginBean = new LoginBean();
    private final LoginController loginController = new LoginController();

    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label errorLabel;

    @FXML
    public void clickedOnBack() {
        FxmlLoader.setPage("WelcomePage");
    }

    @FXML
    public void clickedOnLogin() {

        errorLabel.setVisible(false);
        errorLabel.setText("");

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
            displayError("Incorrect Username or Password.");
        } catch (FailedSearchException e){
            logger.log(Level.WARNING, "Error while finding user with such credentials", e);
            displayError("System Error: Try again later");
        } catch (Exception e){
            logger.log(Level.WARNING, "Systen error occurred", e);
            displayError("Unknown Error");
        }
    }


    private void bindDataToBean(){
        loginBean.setEmail(emailTextField.getText().trim());
        loginBean.setPassword(passwordTextField.getText());
    }



    private boolean validateBeanData(){


        if (!loginBean.validateEmail()) {
            displayError("Invalid email format");
            return false;
        }


        if (!loginBean.validatePassword()) {
            displayError("Invalid password format");
            return false;
        }

        return true;
    }


    private void handleNavigation(Role role) {
        if (role == CLIENT) {
            FxmlLoader.setPage("ClientHomePage");
        } else if (role == CHEF) {
            FxmlLoader.setPage("ChefHomePage");
        } else {
            displayError("Unrecognized role");
        }
    }


    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }


    private boolean checkEmptyFields(){
        if(emailTextField.getText().isEmpty()){
            displayError("email field is empty");
            return false;
        }
        if(passwordTextField.getText().isEmpty()){
            displayError("password field is empty");
            return false;
        }
        return true;
    }


}
