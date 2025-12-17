package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ChefBean;
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

public class SelectMenuPageControllerG{


    private static final Logger logger = Logger.getLogger(SelectMenuPageControllerG.class.getName());


    @FXML
    private VBox menusVBox;
    @FXML
    private Label messageLabel;


    private final ExplorationController explorationController = new ExplorationController();
    private ChefBean selectChefBean;
    private MenuBean selectedMenuBean;
    private List<MenuBean> chefMenuBeans = new ArrayList<>();
    private Parent selectedCardUI;

    private static final String SELECTED_STYLE = "-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;";
    private static final String UNSELECTED_STYLE = "-fx-border-color: #CCCCCC; -fx-border-width: 2; -fx-border-radius: 2;";



    @FXML
    void handleBackToChefs() {
        SelectChefPageControllerG controller = FxmlLoader.setPageAndReturnController("SelectChefPage");
        if (controller != null) {
            controller.initData(selectChefBean);
        }
    }



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
    void handleSelectMenu() {
        if (selectedMenuBean == null) {
            displayError("Please select a menu first");
            return;
        }

        MenuDetailsPageControllerG controller = FxmlLoader.setPageAndReturnController("MenuDetailsPage");
        if (controller != null) {
            controller.initData(selectedMenuBean, selectChefBean);
        }
    }



    private void navigateToIfLogged(String pageName) {
        if (explorationController.isLoggedClient()) {
            FxmlLoader.setPage(pageName);
        } else {
            displayError("You must be logged in to access this page!");
        }
    }



    public void initData(ChefBean chefBean) {
        this.selectChefBean = chefBean;
        messageLabel.setText("");
        messageLabel.setVisible(false);

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
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(ViewsResourcesPaths.MENU_CARD_PATH));
                Parent menuCard = cardLoader.load();

                menuCard.setStyle(UNSELECTED_STYLE);

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
            selectedCardUI.setStyle(UNSELECTED_STYLE);
        }

        selectedCardUI = cardUI;
        selectedCardUI.setStyle(SELECTED_STYLE);
    }



    private void displayError(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
