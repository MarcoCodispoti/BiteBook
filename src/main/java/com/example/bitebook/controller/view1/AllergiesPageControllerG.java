package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.AllergiesController;
import com.example.bitebook.model.bean.AllergenBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class AllergiesPageControllerG{

    private AllergiesController allergiesController = new AllergiesController();
    private Parent selectedCardUi;
    private List<AllergenBean> clientAllergyBeans;
    private List<AllergenBean> allergenListBeans;
    private AllergenBean selectedAllergenBean;
    private AllergenBean newAllergenBean;

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private VBox allergiesVBox;

    @FXML
    private Button insertAllergyButton;

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private ComboBox selectAllergyComboBox;

    @FXML
    private Button removeAllergyButton;

    @FXML
    private ScrollPane allergiesScrollPane;

    @FXML
    private Label errorLabel;


    @FXML
    void clickedOnHomepage(ActionEvent event) {
        FxmlLoader.setPage("ClientHomePage");
    }

    @FXML
    void clickedOnRequests(ActionEvent event){
        FxmlLoader.setPage("ClientRequestsPage");
    }

    @FXML
    void clickedOnRemoveAllergy(ActionEvent event){
        if(selectedAllergenBean!=null){
            try {
                allergiesController.removeClientAllergy(selectedAllergenBean);
            } catch (Exception e) {
                errorLabel.setText("Error while removing allergy");
            }
        } else{
            errorLabel.setText("Please select the allergy to remove first");
        }
        initialize();
    }

    @FXML
    void insertAllergyTextField(ActionEvent event) {

    }

    @FXML
    void clickedOnInsertAllergy(ActionEvent event) {
        newAllergenBean = getNewAllergenBean();
        if(newAllergenBean!=null){
            errorLabel.setText("hai selezionato: ID " +  newAllergenBean.getId() + "  " + newAllergenBean.getName());
        } else {
            errorLabel.setText("Please select the allergy to add first");
            return;
        }
        try {
            allergiesController.insertAllergy(newAllergenBean);
        } catch (Exception e) {
            errorLabel.setText("Error while inserting allergy");
        }
        initialize();
    }


    @FXML
    void initialize(){
        try {
            this.clientAllergyBeans = allergiesController.getClientAllergies();
        } catch (Exception e){
            e.printStackTrace();
            errorLabel.setText("Error while getting allergies, please try again");
            return;
        }
        populateClientAllergies();
        fillSelectAllergyComboBox();
    }

    private void populateClientAllergies(){
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

            } catch (Exception e){
                e.printStackTrace();
                e.getCause();
                e.getMessage();
                errorLabel.setText(e.getMessage());
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


    public void fillSelectAllergyComboBox(){
        try {
            allergenListBeans = allergiesController.getAllergens();
            System.out.println("" + allergenListBeans.size());
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
            e.getMessage();
            errorLabel.setText("Unable to load allergies");
            return;
        }

        selectAllergyComboBox.getItems().clear();
        for(AllergenBean allergyBean : allergenListBeans) {
            selectAllergyComboBox.getItems().add("  " + allergyBean.getName());
        }
    }


    private AllergenBean getNewAllergenBean() {
        String selectedString = String.valueOf(selectAllergyComboBox.getValue());

        if (selectedString == null || selectedString.isEmpty()) {
            return null; // Nessuna selezione
        }
        String cleanName = selectedString.trim();
        if (allergenListBeans != null) {
            for (AllergenBean bean : allergenListBeans) {
                if (bean.getName().equalsIgnoreCase(cleanName)) {
                    return bean; // Trovato!
                }
            }
        }
        return null; // Non trovato (caso di errore)
    }




}
