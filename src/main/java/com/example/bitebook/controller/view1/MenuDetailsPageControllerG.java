package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.DishBean;
import com.example.bitebook.model.bean.MenuBean;
import com.example.bitebook.util.ViewsResourcesPaths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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


    private final ExplorationController explorationController = new ExplorationController();
    private ChefBean menusChefBean;
    private MenuBean selectedMenuBean;
    private List<DishBean> courseBeans = new ArrayList<>();
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
        if (explorationController.isLoggedClient()) {
            ServiceRequestPageControllerG controller = FxmlLoader.setPageAndReturnController("ServiceRequestPage");
            if (controller != null) {
                controller.initData(selectedMenuBean, menuAllergenBeans, menusChefBean);
            }
        } else {
            displayMessage("You must be logged in to proceed");
        }
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
            this.courseBeans = explorationController.getCourses(selectedMenuBean);
            this.menuAllergenBeans = explorationController.getMenuAllergens(courseBeans);

            populateCourses();
            allergensLabel.setText(formatAllergensList());
        } catch (FailedSearchException e){
            displayMessage("Error recovering menu details");
            logger.log(Level.SEVERE, "Error recovering menu details", e);
        }
    }



    private void populateCourses() {
        coursesVBox.getChildren().clear();

        if (courseBeans == null || courseBeans.isEmpty()) {
            return;
        }

        for (DishBean dishBean : courseBeans) {
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



    private void navigateToIfLogged(String pageName) {
        if (explorationController.isLoggedClient()) {
            FxmlLoader.setPage(pageName);
        } else {
            displayMessage("You must be logged in to access this page!");
        }
    }



    private void displayMessage(String message){
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
