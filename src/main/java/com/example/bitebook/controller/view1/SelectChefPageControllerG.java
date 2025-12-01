package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.model.bean.ChefBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class SelectChefPageControllerG {

    private Parent selectedCardUI;
    private ChefBean cityChefBean;
    ChefBean selectedChefBean;
    // private List<Chef>  chefsInCity;
    private List<ChefBean> chefInCityBeans;

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private VBox chefsVBox;

    @FXML
    private Button viewMenusButton;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Label errorLabel;

    @FXML
    private ScrollPane chefsScrollPane;

    @FXML
    private Hyperlink homepageHyperlink;





    public void initData(ChefBean chefBean){
        this.cityChefBean = chefBean;
        errorLabel.setText(chefBean.getCity());
        ExplorationController explorationController = new ExplorationController();
        System.out.println("Vado a prendere gli chef a: "  + chefBean.getCity());
        // this.chefsInCity = explorationController.getChefsInCity(this.cityChefBean);
        this.chefInCityBeans = explorationController.getChefsInCity(this.cityChefBean);
        System.out.println("Ho inizializato: " + chefInCityBeans.size() + " chefs in the selected city: " + chefInCityBeans);


        if(this.chefInCityBeans == null || this.chefInCityBeans.isEmpty()){
            errorLabel.setText("An error occurred while trying to populate the chefs in city, please restart the app");
            return;
        }
        populateChefs();
    }


    @FXML
    void clickedOnHomepage(ActionEvent event){
        FxmlLoader.setPage("ClientHomePage");
    }


    @FXML
    void clickedOnRequests(ActionEvent event){
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("ClientRequestsPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }

    @FXML
    void clickedOnAllergies(ActionEvent event) {
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("AllergiesPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }



    @FXML
    void clickedOnViewMenus(ActionEvent event){
        if(selectedChefBean == null){
            errorLabel.setText("Please select a chef first");
            return;
        }
        SelectMenuPageControllerG selectMenuPageControllerG = FxmlLoader.setPageAndReturnController("SelectMenuPage");
        if(selectMenuPageControllerG != null){
            selectedChefBean.setCity(cityChefBean.getCity());
            selectMenuPageControllerG.initData(selectedChefBean);
        }

    }

    void populateChefs(){
        chefsVBox.getChildren().clear();

        for(ChefBean chefBean : chefInCityBeans){
            try{
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/SelectChefCard.fxml"));
                Parent chefCard = cardLoader.load();

                SelectChefCardControllerG controller = cardLoader.getController();
                // controller.initData(new ChefBean(chef));
                controller.initData(chefBean);
                controller.setCardUI(chefCard);
                controller.setParentController(this);

                chefsVBox.getChildren().add(chefCard);

            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
                e.getCause();
                errorLabel.setText(e.getMessage());
                return;
            }
        }
    }

    public void setSelectedChef(ChefBean selectedChefBean, Parent cardUI){
        this.selectedChefBean = selectedChefBean;

        if(selectedCardUI != null){
            selectedCardUI.setStyle("");
        }
        selectedCardUI = cardUI;
        selectedCardUI.setStyle("-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;");
    }


}
