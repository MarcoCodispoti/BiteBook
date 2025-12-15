package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.AllergiesController;
import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.AllergenBean;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllergiesPageControllerG2{


    private static final Logger logger = Logger.getLogger(AllergiesPageControllerG2.class.getName());


    @FXML
    private Label messageLabel;
    @FXML
    private ListView<AllergenBean> clientAllergiesListView;
    @FXML
    private ListView<AllergenBean> allergensListView;


    private final AllergiesController allergiesController = new AllergiesController();
    private List<AllergenBean> clientAllergyBeans = new ArrayList<>();
    private List<AllergenBean> serverAllergyBeans = new ArrayList<>();



    @FXML
    void handleRequests() {
        FxmlLoader2.setPage("ClientRequestsPage2");
    }



    @FXML
    void handleHomepage() {
        FxmlLoader2.setPage("ClientHomePage2");
    }



    @FXML
    void handleRemoveAllergy() {
        messageLabel.setText("");
        AllergenBean selectedBean = clientAllergiesListView.getSelectionModel().getSelectedItem();

        if (selectedBean == null) {
            displayMessage("Please select an allergy to remove first");
            return;
        }

        try {
            allergiesController.removeClientAllergy(selectedBean);
            refreshData();
        } catch (FailedRemoveException e){
            logger.log(Level.WARNING, "Error while removing allergy.", e);
            displayMessage("Error occurred while removing allergy");
        }
    }



    @FXML
    void handleAddAllergy() {
        messageLabel.setText("");

        AllergenBean selectedBean = allergensListView.getSelectionModel().getSelectedItem();

        if (selectedBean == null) {
            displayMessage("Please select an allergen to add first");
            return;
        }

        try {
            allergiesController.insertAllergy(selectedBean);
            refreshData();
        } catch (FailedInsertException e){
            logger.log(Level.WARNING, "Error while adding allergy.", e);
            displayMessage("Error occurred while adding allergy");
        }
    }



    @FXML
    void initialize(){
        setupListViewFactory(clientAllergiesListView);
        setupListViewFactory(allergensListView);
        refreshData();
    }



    private void refreshData() {
        messageLabel.setText("");

        try {
            this.clientAllergyBeans = allergiesController.getClientAllergies();
            this.serverAllergyBeans = allergiesController.getAllergens();

            populateLists();

        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Error while getting allergens list.", e);
            displayMessage("Error while getting allergies, please try again");
        }
    }



    private void setupListViewFactory(ListView<AllergenBean> listView) {
        listView.setCellFactory(_ -> new ListCell<>() {

            @Override
            protected void updateItem(AllergenBean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }



    private void populateLists(){
        clientAllergiesListView.getItems().clear();
        allergensListView.getItems().clear();

        if (clientAllergyBeans != null) {
            clientAllergiesListView.getItems().addAll(clientAllergyBeans);
        }

        if (serverAllergyBeans != null) {
            for (AllergenBean bean : serverAllergyBeans) {
                if (!isClientAllergy(bean)) {
                    allergensListView.getItems().add(bean);
                }
            }
        }
    }



    private boolean isClientAllergy(AllergenBean allergyBean) {
        if (clientAllergyBeans == null) return false;
        for (AllergenBean clientBean : clientAllergyBeans) {
            if (clientBean.getId() == allergyBean.getId()) {
                return true;
            }
        }
        return false;
    }



    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
