package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectChefPageControllerG {


    private static final String SELECTED_STYLE = "-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;";
    private static final String DEFAULT_STYLE = "";

    private final ExplorationController explorationController = new ExplorationController();

    private Parent selectedCardUI;
    private ChefBean cityChefBean;
    private ChefBean selectedChefBean;
    private List<ChefBean> chefInCityBeans = new ArrayList<>();

    @FXML private VBox chefsVBox;
    @FXML private Label errorLabel;




    public void initData(ChefBean chefBean) {
        this.cityChefBean = chefBean;

        errorLabel.setStyle("-fx-text-fill: black;");
        errorLabel.setText("Chefs in: " + chefBean.getCity());

        try {
            this.chefInCityBeans = explorationController.getChefsInCity(this.cityChefBean);

            if (this.chefInCityBeans == null || this.chefInCityBeans.isEmpty()) {
                showError("No chefs found in this city.");
                return;
            }

            populateChefs();

        } catch (FailedSearchException e) {
            System.err.println("System Error: Unable to retrieve chefs: " + e.getMessage());
            showError("System Error: Unable to retrieve chefs.");
        }
    }

    private void populateChefs() {
        chefsVBox.getChildren().clear();

        for (ChefBean chefBean : chefInCityBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/SelectChefCard.fxml"));
                Parent chefCard = cardLoader.load();

                SelectChefCardControllerG controller = cardLoader.getController();
                controller.initData(chefBean);
                controller.setCardUI(chefCard);
                controller.setParentController(this);

                chefsVBox.getChildren().add(chefCard);

            } catch (IOException e){
                System.err.println("Error loading the card for the chef: " + chefBean.getName());
            }
        }
    }


    public void setSelectedChef(ChefBean selectedChefBean, Parent cardUI) {
        this.selectedChefBean = selectedChefBean;
        if (selectedCardUI != null) {
            selectedCardUI.setStyle(DEFAULT_STYLE);
        }
        selectedCardUI = cardUI;
        selectedCardUI.setStyle(SELECTED_STYLE);
    }

    @FXML
    void clickedOnViewMenus() {
        if (selectedChefBean == null) {
            showError("Please select a chef first");
            return;
        }

        SelectMenuPageControllerG controller = FxmlLoader.setPageAndReturnController("SelectMenuPage");
        if (controller != null) {
            selectedChefBean.setCity(cityChefBean.getCity());
            controller.initData(selectedChefBean);
        }
    }


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


    private void navigateToIfLogged(String pageName) {
        if (explorationController.isLoggedClient()) {
            FxmlLoader.setPage(pageName);
        } else {
            showError("You must be logged in to access this page!");
        }
    }

    private void showError(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



}
