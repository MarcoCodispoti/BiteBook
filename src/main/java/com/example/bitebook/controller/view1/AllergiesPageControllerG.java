package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.AllergiesController;
import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.util.ViewsResourcesPaths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllergiesPageControllerG{


    private static final Logger logger = Logger.getLogger(AllergiesPageControllerG.class.getName());


    private final AllergiesController allergiesController = new AllergiesController();
    private Parent selectedCardUi;
    private AllergenBean selectedAllergenBean;


    @FXML
    private VBox allergiesVBox;


    @FXML
    private ComboBox<AllergenBean> selectAllergyComboBox;


    @FXML
    private Label errorLabel;


    @FXML
    void clickedOnHomepage() {
        FxmlLoader.setPage("ClientHomePage");
    }

    @FXML
    void clickedOnRequests(){
        FxmlLoader.setPage("ClientRequestsPage");
    }



    @FXML
    void clickedOnRemoveAllergy() {
        if (selectedAllergenBean != null) {
            try {
                allergiesController.removeClientAllergy(selectedAllergenBean);
                displayError("Allergia rimossa.");
                refreshPage();

            } catch (FailedRemoveException e) {
                displayError("Error while removing allergy");
                logger.log(Level.SEVERE, "Error while removing allergy" , e);
            }
        } else {
            displayError("Please select the allergy to remove first");
        }
    }


    @FXML
    void clickedOnInsertAllergy() {
        AllergenBean newAllergyBean = selectAllergyComboBox.getValue();

        if (newAllergyBean == null) {
            displayError("Please select the allergy to add first");
            return;
        }

        try {
            allergiesController.insertAllergy(newAllergyBean);
            displayError("Allergy inserted successfully");
            refreshPage();
        } catch (FailedInsertException e) {
            displayError("Error while inserting allergy: ");
            logger.log(Level.SEVERE, "Error while inserting allergy" , e);
        }
    }




    @FXML
    void initialize(){
        refreshPage();
    }


    private void refreshPage(){
        errorLabel.setText("");
        allergiesVBox.getChildren().clear();
        populateClientAllergies();
        fillSelectAllergyComboBox();
        selectedAllergenBean = null;
        selectedCardUi = null;
    }



    private void populateClientAllergies(){
        allergiesVBox.getChildren().clear();
        List<AllergenBean> clientAllergyBeans = allergiesController.getClientAllergies();
        if(clientAllergyBeans == null || clientAllergyBeans.isEmpty()){
            displayError("The client has no allergies");
            return;
        }
        allergiesVBox.getChildren().clear();
        for(AllergenBean allergyBean : clientAllergyBeans){
            try{
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(ViewsResourcesPaths.ALLERGY_CARD_PATH));
                Parent allergyCard = cardLoader.load();
                AllergyCardControllerG controller = cardLoader.getController();
                controller.initData(allergyBean);
                controller.setCardUi(allergyCard);
                controller.setParentController(this);

                allergiesVBox.getChildren().add(allergyCard);

            } catch (IOException e){
                displayError("Error while loading client allergy");
                logger.log(Level.SEVERE, "Error while loading client allergy" , e);
                return;
            }
        }
    }


    public void setSelectedAllergy(AllergenBean allergyBean, Parent cardUi){
        this.selectedAllergenBean = allergyBean;

        if(selectedCardUi != null){
            selectedCardUi.setStyle("");
        }
        selectedCardUi = cardUi;
        selectedCardUi.setStyle("-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;");
    }




    public void fillSelectAllergyComboBox() {
        try {
            List<AllergenBean> allergenListBeans = allergiesController.getAllergens();
            selectAllergyComboBox.getItems().clear();
            if (allergenListBeans != null){
                selectAllergyComboBox.getItems().addAll(allergenListBeans);
            }
        } catch (FailedSearchException e) {
            displayError("Error while searching for allergens list");
            logger.log(Level.SEVERE, "Error while searching for allergens list" , e);
        }
    }


    private void displayError(String errorMessage){
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
    }

}
