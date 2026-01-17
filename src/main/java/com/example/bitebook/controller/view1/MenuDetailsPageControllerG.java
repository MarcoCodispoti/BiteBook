package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExploreController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.util.ViewsResourcesPaths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuDetailsPageControllerG{


    private static final Logger logger = Logger.getLogger(MenuDetailsPageControllerG.class.getName());


    @FXML
    private VBox coursesVBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label numberOfCoursesLabel;
    @FXML
    private Label dietTypeLabel;
    @FXML
    private Label pricePerPersonLabel;
    @FXML
    private Label allergensLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private AnchorPane sidebarAnchorPane;
    @FXML
    private ScrollPane menusScrollPane;
    @FXML
    private Button confirmMenuButton;
    @FXML
    private Button backToMenusButton;
    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label loginMessageLabel;


    private final ExploreController menuExploreController = new ExploreController();
    private ChefBean menusChefBean;
    private MenuBean selectedMenuBean;
    private List<DishBean> menuCoursesBeans = new ArrayList<>();
    private List<AllergenBean> menuAllergenBeans = new ArrayList<>();



    @FXML
    void handleHomepage() {
        FxmlLoader.setPage("ClientHomePage");
    }



    @FXML
    void handleRequests() {
        navigateToIfLogged("ClientRequestsPage");
    }



    @FXML
    void handleAllergies() {
        navigateToIfLogged("AllergiesPage");
    }



    @FXML
    void handleBackToMenus() {
        SelectMenuPageControllerG controller = FxmlLoader.setPageAndReturnController("SelectMenuPage");
        if (controller != null) {
            controller.initData(menusChefBean);
        }
    }



    @FXML
    void handleConfirmMenu() {
        if (menuExploreController.isLoggedClient()) {
            ServiceRequestPageControllerG controller = FxmlLoader.setPageAndReturnController("ServiceRequestPage");
            if (controller != null) {
                controller.initData(selectedMenuBean, menuAllergenBeans, menusChefBean);
            }
        } else {
            setLoginPopup();
        }
    }



    @FXML
    void handleLogin(){
        if(areEmptyLoginFields()){
            return;
        }

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(emailTextField.getText());
        loginBean.setPassword(passwordTextField.getText());

        if(!loginBean.validate()){
            displayLoginMessage("Invalid Login Credentials");
            return;
        }
        try{
            LoginController loginController = new LoginController();
            loginController.loginAsClient(loginBean);
        } catch (FailedSearchException e){
            logger.log(Level.WARNING, "Error while finding user with such credentials", e);
            displayLoginMessage("System Error: Try again later");
            return;
        } catch (WrongCredentialsException _) {
            displayLoginMessage("Wrong username or password");
            return;
        } catch (Exception e){
            logger.log(Level.WARNING, "System error occurred", e);
            displayLoginMessage("Unknown Error");
            return;
        }

        hideLoginPopup();
    }



    @FXML
    void handleBack(){
        hideLoginPopup();
    }



    public void initData(MenuBean menuBean, ChefBean selectedChefBean) {
        this.selectedMenuBean = menuBean;
        this.menusChefBean = selectedChefBean;

        nameLabel.setText(menuBean.getName());
        dietTypeLabel.setText(menuBean.getDietType().toString().toLowerCase());
        numberOfCoursesLabel.setText(String.valueOf(menuBean.getNumberOfCourses()));
        pricePerPersonLabel.setText(menuBean.getPricePerPerson() + " €");

        messageLabel.setText("");


        try {
            this.menuCoursesBeans = menuExploreController.getCourses(selectedMenuBean);
            this.menuAllergenBeans = menuExploreController.getCoursesAllergens(menuCoursesBeans);

            populateCourses();
            allergensLabel.setText(formatAllergensList());
        } catch (FailedSearchException e){
            displayMessage("Error recovering menu details");
            logger.log(Level.SEVERE, "Error recovering menu details", e);
        }
    }



    private void populateCourses() {
        coursesVBox.getChildren().clear();

        if (menuCoursesBeans == null || menuCoursesBeans.isEmpty()) {
            return;
        }

        for (DishBean dishBean : menuCoursesBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(ViewsResourcesPaths.DISH_CARD_PATH));
                Parent dishCard = cardLoader.load();

                DishCardControllerG controller = cardLoader.getController();
                controller.initData(dishBean);

                coursesVBox.getChildren().add(dishCard);

            } catch (IOException e){
                logger.log(Level.WARNING, "Error recovering some menu details", e);
                displayMessage("Error recovering some menu details");
            }
        }
    }
    


    private String formatAllergensList() {
        if (menuAllergenBeans == null || menuAllergenBeans.isEmpty()) {
            return "No Allergens";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < menuAllergenBeans.size(); i++) {
            sb.append(menuAllergenBeans.get(i).getName());
            if (i < menuAllergenBeans.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();

    }



    private void setLoginPopup(){
        loginAnchorPane.setVisible(true);
        sidebarAnchorPane.setDisable(true);
        backToMenusButton.setDisable(true);
        confirmMenuButton.setDisable(true);
        menusScrollPane.setDisable(true);
    }



    private void hideLoginPopup(){
        loginAnchorPane.setVisible(false);
        sidebarAnchorPane.setDisable(false);
        backToMenusButton.setDisable(false);
        confirmMenuButton.setDisable(false);
        menusScrollPane.setDisable(false);
    }



    private void navigateToIfLogged(String pageName) {
        if (menuExploreController.isLoggedClient()) {
            FxmlLoader.setPage(pageName);
        } else {
            displayMessage("You must be logged in to access this page!");
        }
    }



    private void displayMessage(String message){
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }



    private void displayLoginMessage(String message){
        loginMessageLabel.setText(message);
        loginMessageLabel.setVisible(true);
    }



    private boolean areEmptyLoginFields() {
        if(emailTextField.getText().isEmpty()){
            displayLoginMessage("Email field is empty!");
            return true;
        }
        if(passwordTextField.getText().isEmpty()){
            displayLoginMessage("Password field is empty!");
            return true;
        }
        return false;
    }


}
