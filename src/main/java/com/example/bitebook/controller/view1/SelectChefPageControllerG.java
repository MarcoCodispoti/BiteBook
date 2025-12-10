package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.util.View1Paths;
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

public class SelectChefPageControllerG {

    private static final Logger logger = Logger.getLogger(SelectChefPageControllerG.class.getName());


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
                displayError("No chefs found in this city.");
                return;
            }
            populateChefs();

        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "System Error: Unable to retrieve chefs: ", e);
            displayError("System Error: Unable to retrieve chefs.");
        }
    }

    private void populateChefs() {
        chefsVBox.getChildren().clear();

        for (ChefBean chefBean : chefInCityBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(View1Paths.SELECT_CHEF_CARD_PATH));
                Parent chefCard = cardLoader.load();

                SelectChefCardControllerG controller = cardLoader.getController();
                controller.initData(chefBean);
                controller.setCardUI(chefCard);
                controller.setParentController(this);

                chefsVBox.getChildren().add(chefCard);
            } catch (IOException e){
                logger.log(Level.WARNING, "Error loading come chef " , e);
                displayError("Error loading come chef");
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
            displayError("Please select a chef first");
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
            displayError("You must be logged in to access this page!");
        }
    }

    private void displayError(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



}
