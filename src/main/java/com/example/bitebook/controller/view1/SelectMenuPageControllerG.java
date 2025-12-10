package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.MenuBean;
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

public class SelectMenuPageControllerG{

    private static final Logger logger = Logger.getLogger(SelectMenuPageControllerG.class.getName());


private static final String SELECTED_STYLE = "-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;";
    private static final String DEFAULT_STYLE = "";

    private final ExplorationController explorationController = new ExplorationController();

    private ChefBean selectChefBean;
    private MenuBean selectedMenuBean;
    private List<MenuBean> chefMenuBeans = new ArrayList<>();
    private Parent selectedCardUI;

    @FXML private VBox menusVBox;
    @FXML private Label errorLabel;
    

    public void initData(ChefBean chefBean) {
        this.selectChefBean = chefBean;
        errorLabel.setText("");
        errorLabel.setVisible(false);
        try {
            this.chefMenuBeans = explorationController.getChefMenus(chefBean);
            if (this.chefMenuBeans == null || this.chefMenuBeans.isEmpty()) {
                displayError("This chef has no menus available.");
                return;
            }
            populateMenus();
        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "System Error: Unable to retrieve menus: ", e);
            displayError("System Error: Unable to retrieve menus.");
        }
    }

    private void populateMenus() {
        menusVBox.getChildren().clear();

        for (MenuBean menuBean : chefMenuBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(View1Paths.MENU_CARD_PATH));
                Parent menuCard = cardLoader.load();

                SelectMenuCardControllerG controller = cardLoader.getController();
                controller.initData(menuBean);
                controller.setCardUi(menuCard);
                controller.setParentController(this);
                menusVBox.getChildren().add(menuCard);
            } catch (IOException e){
                logger.log(Level.SEVERE, "Error loading card menu ", e);
                displayError("Error loading a menu ");
            }
        }
    }


    public void setSelectedMenu(MenuBean menuBean, Parent cardUI) {
        this.selectedMenuBean = menuBean;

        if (selectedCardUI != null) {
            selectedCardUI.setStyle(DEFAULT_STYLE);
        }

        selectedCardUI = cardUI;
        selectedCardUI.setStyle(SELECTED_STYLE);
    }

    @FXML
    void clickedOnSelectMenu() {
        if (selectedMenuBean == null) {
            displayError("Please select a menu first");
            return;
        }

        MenuDetailsPageControllerG controller = FxmlLoader.setPageAndReturnController("MenuDetailsPage");
        if (controller != null) {
            controller.initData(selectedMenuBean, selectChefBean);
        }
    }

    @FXML
    void clickedOnBackToChefs() {
        SelectChefPageControllerG controller = FxmlLoader.setPageAndReturnController("SelectChefPage");
        if (controller != null) {
            controller.initData(selectChefBean);
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

    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }


}
