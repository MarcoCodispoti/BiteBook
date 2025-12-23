package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.bean.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SelectMenuPageControllerG2{


    private static final Logger logger = Logger.getLogger(SelectMenuPageControllerG2.class.getName());


    @FXML
    private ComboBox<ChefBean> chefComboBox;
    @FXML
    private ComboBox<MenuBean> menuComboBox;
    @FXML
    private ListView<DishBean> menuDetailsListView;
    @FXML
    private Label allergensLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private AnchorPane selectMenuAnchorPane;
    @FXML
    private AnchorPane topbarAnchorPane;
    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label loginMessageLabel;


    private final ExplorationController explorationController = new ExplorationController();
    private ChefBean selectedChefBean;
    private MenuBean selectedMenuBean;
    private List<AllergenBean> menuAllergenBeans = new ArrayList<>();


    @FXML
    void handleLogin(){
        if(areEmptyLoginFields()){
            return;
        }

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(emailTextField.getText());
        loginBean.setPassword(passwordTextField.getText());

        if(!loginBean.validate()){
            displayLoginMessage("Invalid Login credentials");
            return;
        }
        try{
            LoginController loginController = new LoginController();
            loginController.loginAsClient(loginBean);
        } catch (WrongCredentialsException _) {
            displayLoginMessage("Wrong username or password");
            return;
        } catch (FailedSearchException e){
            logger.log(Level.WARNING, "Error while finding user with such credentials", e);
            displayLoginMessage("System Error: Try again later");
            return;
        } catch (Exception e){
            logger.log(Level.WARNING, "System error occurred", e);
            displayLoginMessage("Unknown Error");
            return;
        }

        hideLoginPopup();

    }



    @FXML
    void handleLoginBack(){
        hideLoginPopup();
    }



    @FXML
    void clickedOnBack() {
        FxmlLoader2.setPage("ClientHomePage2");
    }



    @FXML
    void clickedOnAllergies(){
        navigateToIfLogged("AllergiesPage2", "You must be logged in to view Allergies");
    }



    @FXML
    void clickedOnRequests(){
        navigateToIfLogged("ClientRequestsPage2", "You must be logged in to view Requests");
    }



    @FXML
    void clickedOnBook(){
        if (!explorationController.isLoggedClient()) {
            setLoginPopup();
            return;
        }

        if (selectedChefBean == null || selectedMenuBean == null) {
            displayMesssage("Please select a chef and a menu first.");
            return;
        }

        ServiceRequestPageControllerG2 serviceRequestPageControllerG2 = FxmlLoader2.setPageAndReturnController("ServiceRequestPage2");
        if(serviceRequestPageControllerG2 != null){
            serviceRequestPageControllerG2.initData(selectedChefBean,selectedMenuBean,menuAllergenBeans);
        }
    }



    @FXML
    public void initialize(){
        setupChefComboBox();
        setupMenuComboBox();
        setupDishListView();

        chefComboBox.valueProperty().addListener((_, _, newVal) -> onChefSelected(newVal));
        menuComboBox.valueProperty().addListener((_, _, newVal) -> onMenuSelected(newVal));
        loginAnchorPane.setVisible(false);
    }



    public void initData(ChefBean chefBean) {
        try {
            List<ChefBean> chefsInCity = explorationController.getChefsInCity(chefBean);

            chefComboBox.getItems().clear();
            if (chefsInCity != null) {
                chefComboBox.getItems().addAll(chefsInCity);
            }
        } catch (FailedSearchException e) {
            logger.log(Level.SEVERE, "Error while getting chefs list.", e);
            displayMesssage("System Error: Unable to retrieve chefs in city.");
        }
    }



    private void onChefSelected(ChefBean chef) {
        this.selectedChefBean = chef;
        menuComboBox.getItems().clear();
        menuDetailsListView.getItems().clear();
        allergensLabel.setText("");
        menuAllergenBeans.clear();

        if (chef != null) {
            try {
                List<MenuBean> chefMenus = explorationController.getChefMenus(chef);
                if (chefMenus != null) {
                    menuComboBox.getItems().addAll(chefMenus);
                }
            } catch (FailedSearchException e) {
                logger.log(Level.SEVERE, "Error while getting menus list.", e);
                displayMesssage("Error occurred while searching menus.");
            }
        }
    }



    private void onMenuSelected(MenuBean menu) {
        this.selectedMenuBean = menu;
        menuDetailsListView.getItems().clear();
        allergensLabel.setText("");
        if (menu != null) {
            try {
                List<DishBean> dishes = explorationController.getCourses(menu);
                if (dishes != null) {
                    menuDetailsListView.getItems().addAll(dishes);
                    this.menuAllergenBeans = explorationController.getMenuAllergens(dishes);
                    allergensLabel.setText(formatAllergensList(menuAllergenBeans));
                }
            } catch (FailedSearchException e){
                logger.log(Level.SEVERE, "Error while getting menu details.", e);
                displayMesssage("Error occurred while getting menu details.");
            }
        }
    }



    private void setupChefComboBox() {
        chefComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ChefBean chef) {
                if (chef == null) return null;
                String specs = chef.getSpecializationTypes().stream()
                        .map(Enum::toString)
                        .collect(Collectors.joining(", "));
                return String.format("%s %s (%s - %s)",
                        chef.getName(), chef.getSurname(), chef.getCookingStyle(), specs);
            }

            @Override
            public ChefBean fromString(String string) { return null; }
        });
    }



    private void setupMenuComboBox() {
        menuComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MenuBean menu) {
                if (menu == null) return null;
                return String.format("%s  ( Courses: %d | Style: %s | Price per person: %d € )",
                        menu.getName(), menu.getNumberOfCourses(), String.valueOf(menu.getDietType()).toLowerCase(), menu.getPricePerPerson());
            }

            @Override
            public MenuBean fromString(String string) { return null; }
        });
    }



    private void setupDishListView() {
        menuDetailsListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(DishBean dish, boolean empty) {
                super.updateItem(dish, empty);
                if (empty || dish == null) {
                    setText(null);
                } else {
                    setText(String.format("%s: %s - %s",
                            dish.getCourseType().toString().replace("_", " "),
                            dish.getName(),
                            dish.getDescription()));
                }
            }
        });
    }



    private String formatAllergensList(List<AllergenBean> allergens) {
        if (allergens == null || allergens.isEmpty()) return "None";
        return allergens.stream()
                .map(AllergenBean::getName)
                .distinct()
                .collect(Collectors.joining(", "));
    }



    private void navigateToIfLogged(String pageName, String errorMsg) {
        if (explorationController.isLoggedClient()) {
            FxmlLoader2.setPage(pageName);
        } else {
            displayMesssage(errorMsg);
        }
    }



    private void displayMesssage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }



    private void displayLoginMessage(String message) {
        loginMessageLabel.setText(message);
    }



    private void setLoginPopup(){
        selectMenuAnchorPane.setDisable(true);
        topbarAnchorPane.setDisable(true);
        loginAnchorPane.setVisible(true);
    }



    private void hideLoginPopup(){
        selectMenuAnchorPane.setDisable(false);
        topbarAnchorPane.setDisable(false);
        loginAnchorPane.setVisible(false);
    }



    private boolean areEmptyLoginFields(){
        if(emailTextField.getText().isEmpty()){
            displayLoginMessage("Please enter a email address.");
            return true;
        }
        if(passwordTextField.getText().isEmpty()){
            displayLoginMessage("Please enter a password.");
            return true;
        }
        return false;
    }


}
