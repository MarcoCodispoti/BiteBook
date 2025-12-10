package com.example.bitebook.controller.view2;

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


public class LoginPageControllerG2{

    private static final Logger logger = Logger.getLogger(LoginPageControllerG2.class.getName());


    private final LoginController loginController = new LoginController();
    private final LoginBean loginBean = new LoginBean();

    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label errorLabel;


    @FXML
    void clickedOnLogin(){
        errorLabel.setText("");
        errorLabel.setVisible(false);

        if (!validateAndBindData()) {
            return;
        }
        try {
            loginController.authenticate(loginBean);
            Role actualRole = LoggedUser.getInstance().getRole();
            navigateByRole(actualRole);
        } catch (WrongCredentialsException _) {
            displayError("Incorrect username or password.");
        } catch (FailedSearchException e) {
            logger.log(Level.SEVERE, "Error while logging in" ,e);
            displayError("System error during login. Please try again.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected system error",e.getCause());
            displayError("An unexpected error occurred.");
        }
    }

    @FXML
    void clickedOnProceedAsGuest() {
        FxmlLoader2.setPage("ClientHomePage2");
    }


    private boolean validateAndBindData() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isEmpty()) {
            displayError("Please enter your email.");
            return false;
        }
        if (password.isEmpty()) {
            displayError("Please enter your password.");
            return false;
        }

        loginBean.setEmail(email.trim());
        loginBean.setPassword(password);

        if (!loginBean.validateEmail()) {
            displayError("Please insert a valid email format.");
            return false;
        }
        if (!loginBean.validatePassword()) {
            displayError("Password format is invalid.");
            return false;
        }

        return true;
    }

    private void navigateByRole(Role role) {
        if (role == null) {
            displayError("Login system error: Role not found.");
            return;
        }
        switch (role) {
            case CLIENT -> FxmlLoader2.setPage("ClientHomePage2");
            case CHEF -> FxmlLoader2.setPage("ChefHomePage2");
            default -> displayError("Unknown user role.");
        }
    }

    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



}
