package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.DishBean;
import com.example.bitebook.model.bean.MenuBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuDetailsPageControllerG{

    private final ExplorationController explorationController = new ExplorationController();


    private ChefBean menusChefBean;
    private MenuBean selectedMenuBean;
    private List<DishBean> courseBeans = new ArrayList<>();
    private List<AllergenBean> menuAllergenBeans = new ArrayList<>();


    @FXML private VBox coursesVBox;
    @FXML private Label nameLabel;
    @FXML private Label numberOfCoursesLabel;
    @FXML private Label dietTypeLabel;
    @FXML private Label pricePerPersonLabel;
    @FXML private Label allergensLabel;
    @FXML private Label errorLabel;




    @FXML
    void clickedOnHomepage() {
        FxmlLoader.setPage("ClientHomePage");
    }

    @FXML
    void clickedOnRequests() {
        navigateToIfLogged("ClientRequestsPage");
    }

    @FXML
    void clickedOnAllergies() {
        navigateToIfLogged("AllergiesPage");
    }

    @FXML
    void clickedOnBackToMenus() {
        SelectMenuPageControllerG controller = FxmlLoader.setPageAndReturnController("SelectMenuPage");
        if (controller != null) {
            controller.initData(menusChefBean);
        }
    }

    @FXML
    void clickedOnConfirmMenu() {
        if (explorationController.isLoggedClient()) {
            ServiceRequestPageControllerG controller = FxmlLoader.setPageAndReturnController("ServiceRequestPage");
            if (controller != null) {
                controller.initData(selectedMenuBean, menuAllergenBeans, menusChefBean);
            }
        } else {
            errorLabel.setText("You must be logged in to proceed");
        }
    }



    public void initData(MenuBean menuBean, ChefBean selectedChefBean) {
        this.selectedMenuBean = menuBean;
        this.menusChefBean = selectedChefBean;

        nameLabel.setText(menuBean.getName());
        dietTypeLabel.setText(menuBean.getDietType().toString().toLowerCase());
        numberOfCoursesLabel.setText(String.valueOf(menuBean.getNumberOfCourses()));
        pricePerPersonLabel.setText(menuBean.getPricePerPerson() + " €");

        errorLabel.setText("");
        errorLabel.setVisible(true);


        try {
            this.courseBeans = explorationController.getCourses(selectedMenuBean);
            this.menuAllergenBeans = explorationController.getMenuAllergens(courseBeans);

            populateCourses();
            allergensLabel.setText(formatAllergensList());
        } catch (FailedSearchException e){
            System.err.println("Error recovering menu details: " + e.getMessage());
            errorLabel.setText("Error recovering menu details");
        }
    }

    private void populateCourses() {
        coursesVBox.getChildren().clear();

        if (courseBeans == null || courseBeans.isEmpty()) {
            return;
        }

        for (DishBean dishBean : courseBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/DishCard.fxml"));
                Parent dishCard = cardLoader.load();

                DishCardControllerG controller = cardLoader.getController();
                controller.initData(dishBean);

                coursesVBox.getChildren().add(dishCard);

            } catch (IOException e){
                System.err.println("Unable to load a the card for the dish: " + dishBean.getName());

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
            errorLabel.setText("You must be logged in to access this page!");
        }
    }



}
