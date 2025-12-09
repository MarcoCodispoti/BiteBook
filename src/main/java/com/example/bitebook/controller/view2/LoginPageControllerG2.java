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


public class LoginPageControllerG2{


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
        } catch (WrongCredentialsException e) {
            showError("Incorrect username or password.");
        } catch (FailedSearchException e) {
            showError("System error during login. Please try again.");
        } catch (Exception e) {
            showError("An unexpected error occurred.");
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
            showError("Please enter your email.");
            return false;
        }
        if (password.isEmpty()) {
            showError("Please enter your password.");
            return false;
        }


        loginBean.setEmail(email.trim());
        loginBean.setPassword(password);

        if (!loginBean.validateEmail()) {
            showError("Please insert a valid email format.");
            return false;
        }
        if (!loginBean.validatePassword()) {
            showError("Password format is invalid.");
            return false;
        }

        return true;
    }

    private void navigateByRole(Role role) {
        if (role == null) {
            showError("Login system error: Role not found.");
            return;
        }

        switch (role) {
            case CLIENT -> FxmlLoader2.setPage("ClientHomePage2");
            case CHEF -> FxmlLoader2.setPage("ChefHomePage2");
            default -> showError("Unknown user role.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



}
