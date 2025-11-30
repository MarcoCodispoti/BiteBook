package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.MenuBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Vector;

import static java.awt.SystemColor.menu;

public class SelectMenuPageControllerG{
    private ChefBean selectChefBean;
    private Vector<MenuBean> chefMenuBeans;

    private Parent selectedCardUI;
    private MenuBean menuBean;
    MenuBean selectedMenuBean;


    @FXML
    private ScrollPane menusScrollPane;

    @FXML
    private Button sendRequestButton;

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private VBox menusVBox;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private Label errorLabel;

    @FXML
    private Button backToChefsButton;


    public void initData(ChefBean chefBean){
        this.selectChefBean = chefBean;
        errorLabel.setText("Menu dello chef con ID: " + chefBean.getId());
        ExplorationController explorationController = new ExplorationController();
        this.chefMenuBeans = explorationController.getChefMenus(chefBean);
        errorLabel.setText("Ho trovato il menu con" + chefMenuBeans.size() + " piatti");

        if(this.chefMenuBeans == null || this.chefMenuBeans.isEmpty()){
            errorLabel.setText("An error occured while getting menu");
            return;
        }
        populateMenus();
    }


    @FXML
    void clickedOnHomepage(ActionEvent event){
        FxmlLoader.setPage("ClientHomePage");
    }


    @FXML
    void clickedOnRequests(ActionEvent event) {
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("ClientRequestsPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }


    @FXML
    void clickedOnAllergies(ActionEvent event){
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("AllergiesPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }

    @FXML
    void clickedOnSelectMenu(ActionEvent event) {
        if(selectedMenuBean == null){
            errorLabel.setText("Please select a menu first");
            return;
        }
        MenuDetailsPageControllerG menuDetailsPageControllerG = FxmlLoader.setPageAndReturnController("MenuDetailsPage");
//        FxmlLoader.setPage("MenuDetailsPage");
        if(menuDetailsPageControllerG != null){
            menuDetailsPageControllerG.initData(selectedMenuBean,selectChefBean);
        }
    }

    @FXML
    void clickedOnBackToChefs(ActionEvent event){
        SelectChefPageControllerG selectChefPageControllerG = FxmlLoader.setPageAndReturnController("SelectChefPage");
        selectChefPageControllerG.initData(selectChefBean);
    }



    public void populateMenus(){
        menusVBox.getChildren().clear();

        for(MenuBean menuBean : chefMenuBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/SelectMenuCard.fxml"));
                Parent menuCard = cardLoader.load();

                SelectMenuCardControllerG controller = cardLoader.getController();
                controller.initData(menuBean);
                controller.setCardUi(menuCard);
                controller.setParentController(this);

                menusVBox.getChildren().add(menuCard);

            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
                e.getCause();
                errorLabel.setText(e.getMessage());
                return;
            }
        }
    }


    public void setSelectedMenu(MenuBean menuBean, Parent cardUI){
        this.selectedMenuBean = menuBean;

        if(selectedCardUI != null){
            selectedCardUI.setStyle("");
        }
        selectedCardUI = cardUI;
        selectedCardUI.setStyle("-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;");
    }


}
