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


import static com.example.bitebook.model.enums.Role.CHEF;
import static com.example.bitebook.model.enums.Role.CLIENT;

public class LoginPageControllerG {

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
        } catch (WrongCredentialsException e) {
            showError("Incorrect Username or Password.");
        } catch (FailedSearchException e){
            System.err.println("System Error while login: " + e.getMessage());
            showError("System Error: Try again later");
        } catch (Exception e){
            showError("Unknown Error");
        }
    }


    private void bindDataToBean(){
        loginBean.setEmail(emailTextField.getText().trim());
        loginBean.setPassword(passwordTextField.getText());
    }



    private boolean validateBeanData(){


        if (!loginBean.validateEmail()) {
            showError("Invalid email format");
            return false;
        }


        if (!loginBean.validatePassword()) {
            showError("Invalid password format");
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
            showError("Unrecognized role");
        }
    }


    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
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
