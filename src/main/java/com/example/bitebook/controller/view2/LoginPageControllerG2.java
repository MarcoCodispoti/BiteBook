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


    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label messageLabel;


    private final LoginController loginController = new LoginController();
    private final LoginBean loginBean = new LoginBean();



    @FXML
    void handleLogin(){
        messageLabel.setText("");
        messageLabel.setVisible(false);

        if (!validateAndBindData()) {
            return;
        }
        try {
            loginController.authenticate(loginBean);
            Role actualRole = LoggedUser.getInstance().getRole();
            navigateByRole(actualRole);
        } catch (WrongCredentialsException _) {
            displayMessage("Incorrect username or password.");
        } catch (FailedSearchException e) {
            logger.log(Level.SEVERE, "Error while logging in" ,e);
            displayMessage("System error during login. Please try again.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected system error",e);
            displayMessage("An unexpected error occurred.");
        }
    }



    @FXML
    void handleProceedAsGuest() {
        FxmlLoader2.setPage("ClientHomePage2");
    }



    private boolean validateAndBindData() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isEmpty()) {
            displayMessage("Please enter your email.");
            return false;
        }
        if (password.isEmpty()) {
            displayMessage("Please enter your password.");
            return false;
        }

        loginBean.setEmail(email.trim());
        loginBean.setPassword(password);

        if (!loginBean.validateEmail()) {
            displayMessage("Please insert a valid email format.");
            return false;
        }
        if (!loginBean.validatePassword()) {
            displayMessage("Password format is invalid.");
            return false;
        }

        return true;
    }



    private void navigateByRole(Role role) {
        if (role == null) {
            displayMessage("Login system error: Role not found.");
            return;
        }
        switch (role) {
            case CLIENT -> FxmlLoader2.setPage("ClientHomePage2");
            case CHEF -> FxmlLoader2.setPage("ChefHomePage2");
            default -> displayMessage("Unknown user role.");
        }
    }



    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
