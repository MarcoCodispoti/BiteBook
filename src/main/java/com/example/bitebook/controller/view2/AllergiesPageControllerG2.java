package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.AllergiesController;
import com.example.bitebook.model.bean.AllergenBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Vector;

public class AllergiesPageControllerG2{

    AllergiesController allergiesController = new AllergiesController();
    Vector<AllergenBean> clientAllergyBeans;
    Vector<AllergenBean> serverAllergyBeans;
    AllergenBean allergyToRemoveBean;
    AllergenBean allergyToAddBean;


    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private ListView<String> clientAllergiesListView;

    @FXML
    private Button removeAllergyButton;

    @FXML
    private ListView<String> allergensListView;

    @FXML
    private Button addAllergyButton;

    @FXML
    private Label errorLabel;

    @FXML
    void clickedOnRequests(ActionEvent event) {
        FxmlLoader2.setPage("ClientRequestsPage2");
    }

    @FXML
    void clickedOnHomepage(ActionEvent event) {
        FxmlLoader2.setPage("ClientHomePage2");
    }

    @FXML
    void clickedOnRemoveAllergy(ActionEvent event){
        allergyToRemoveBean = extractAllergenBean(clientAllergiesListView.getSelectionModel().getSelectedItem());
        if(allergyToRemoveBean==null){
            errorLabel.setText("Please select a allergy to remove first");
            return;
        }
        try{
            allergiesController.removeClientAllergy(allergyToRemoveBean);
            initialize();
        } catch(Exception e){
            errorLabel.setText("Error occured while trying to remove allergy");
        }
    }

    @FXML
    void clickedOnAddAllergy(ActionEvent event){
        allergyToAddBean = extractAllergenBean(allergensListView.getSelectionModel().getSelectedItem());
        if(allergyToAddBean==null){
            errorLabel.setText("Please select a allergy to add first");
            return;
        }
        try{
            allergiesController.insertAllergy(allergyToAddBean);
            initialize();
        } catch(Exception e){
            errorLabel.setText("Error occured while trying to add allergy");
        }
    }

    @FXML
    void initialize(){
        try{
            this.clientAllergyBeans = allergiesController.getClientAllergies();
            this.serverAllergyBeans = allergiesController.getAllergens();
        } catch(Exception e){
            e.printStackTrace();
            errorLabel.setText("Error while getting allergies, please try again");
            return;
        }
        populateClientAllergies();
        fillAddAllergyListView();
    }

    private void populateClientAllergies(){
        clientAllergiesListView.getItems().clear();

        if(clientAllergyBeans != null){
            for(AllergenBean allergyBean : clientAllergyBeans){
                clientAllergiesListView.getItems().add("ID: " + allergyBean.getId() + "  Allergen: " +  allergyBean.getName());
            }
        }

    }

    private void fillAddAllergyListView(){
        allergensListView.getItems().clear();

        for(AllergenBean allergyBean : serverAllergyBeans){
            if(!isClientAllergy(allergyBean)) {
                allergensListView.getItems().add("ID: " + allergyBean.getId() + "  Allergen: " + allergyBean.getName());
            }
        }
    }

    private boolean isClientAllergy(AllergenBean allergyBean){
        for(AllergenBean clientAllergyBean : clientAllergyBeans){
            if(clientAllergyBean.getId() == allergyBean.getId()){
                return true;
            }
        }
        return false;
    }

    private AllergenBean extractAllergenBean(String listViewString){
        if (listViewString == null || listViewString.isEmpty()) {
            return null;
        }

        try {
            // Definiamo i marcatori che abbiamo usato per costruire la stringa
            String prefix = "ID: ";
            String separator = "  Allergen: ";

            // 2. Troviamo dove finisce il prefisso e dove inizia il separatore
            int startIndex = listViewString.indexOf(prefix);
            int endIndex = listViewString.indexOf(separator);

            // Se non troviamo i marcatori, la stringa non è valida
            if (startIndex == -1 || endIndex == -1) {
                System.err.println("Formato stringa non valido: " + listViewString);
                return null;
            }

            // Spostiamo l'indice di inizio dopo "ID: "
            startIndex += prefix.length();

            // 3. Estraiamo la sottostringa che contiene solo il numero (es. "5")
            // .trim() è una sicurezza in più per rimuovere eventuali spazi bianchi accidentali
            String idString = listViewString.substring(startIndex, endIndex).trim();

            // 4. Convertiamo in intero
            for(AllergenBean allergyBean : serverAllergyBeans){
                if(Integer.parseInt(idString) == allergyBean.getId()){
                    return allergyBean;
                }
            }
            return null;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            // Gestione dell'errore se il numero non è leggibile
            System.err.println("Errore durante l'estrazione dell'ID: " + e.getMessage());
            return null;
        }
    }

}
