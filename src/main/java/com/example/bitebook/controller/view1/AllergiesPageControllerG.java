package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.AllergiesController;
import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.AllergenBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AllergiesPageControllerG{

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
                errorLabel.setText("Allergia rimossa.");
                refreshPage();

            } catch (FailedRemoveException e) {
                errorLabel.setText("Error while removing allergy");
            }
        } else {
            errorLabel.setText("Please select the allergy to remove first");
        }
    }


    @FXML
    void clickedOnInsertAllergy() {
        AllergenBean newAllergyBean = selectAllergyComboBox.getValue();

        if (newAllergyBean == null) {
            errorLabel.setText("Please select the allergy to add first");
            return;
        }

        try {
            allergiesController.insertAllergy(newAllergyBean);

            errorLabel.setText("Allergy inserted successfully");
            refreshPage();

        } catch (FailedInsertException e) { // Usa FailedInsertException se preferisci
            errorLabel.setText("Error while inserting allergy: " + e.getMessage());
        }
    }




    @FXML
    void initialize(){
        refreshPage();
    }


    private void refreshPage(){
        errorLabel.setText("");
        allergiesVBox.getChildren().clear(); // Pulisce la grafica vecchia
        populateClientAllergies();           // Ricarica le card
        fillSelectAllergyComboBox();         // Ricarica il menu a tendina

        // Reset selezioni
        selectedAllergenBean = null;
        selectedCardUi = null;
    }



    private void populateClientAllergies(){

        allergiesVBox.getChildren().clear();

        List<AllergenBean> clientAllergyBeans = allergiesController.getClientAllergies();

        if(clientAllergyBeans == null || clientAllergyBeans.isEmpty()){
            errorLabel.setText("The client has no allergies");
            return;
        }
        allergiesVBox.getChildren().clear();
        for(AllergenBean allergyBean : clientAllergyBeans){
            try{
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/AllergyCard.fxml"));
                Parent allergyCard = cardLoader.load();

                AllergyCardControllerG controller = cardLoader.getController();
                controller.initData(allergyBean);
                controller.setCardUi(allergyCard);
                controller.setParentController(this);

                allergiesVBox.getChildren().add(allergyCard);

            } catch (IOException e){
                errorLabel.setText("Error while loading client allergy");
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
            errorLabel.setText("Error while searching for allergens list");
        }
    }


}
