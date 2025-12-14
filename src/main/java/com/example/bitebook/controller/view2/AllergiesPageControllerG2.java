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


    private final AllergiesController allergiesController = new AllergiesController();

    private List<AllergenBean> clientAllergyBeans = new ArrayList<>();
    private List<AllergenBean> serverAllergyBeans = new ArrayList<>();

    @FXML private Label errorLabel;

    @FXML private ListView<AllergenBean> clientAllergiesListView;
    @FXML private ListView<AllergenBean> allergensListView;

    @FXML
    void initialize(){
        setupListViewFactory(clientAllergiesListView);
        setupListViewFactory(allergensListView);

        refreshData();
    }

    private void refreshData() {
        errorLabel.setText("");
        errorLabel.setVisible(false);

        try {
            this.clientAllergyBeans = allergiesController.getClientAllergies();
            this.serverAllergyBeans = allergiesController.getAllergens();

            populateLists();

        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Error while getting allergens list.", e);
            displayError("Error while getting allergies, please try again");
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


    @FXML
    void clickedOnRemoveAllergy() {
        errorLabel.setText("");
        AllergenBean selectedBean = clientAllergiesListView.getSelectionModel().getSelectedItem();

        if (selectedBean == null) {
            displayError("Please select an allergy to remove first");
            return;
        }

        try {
            allergiesController.removeClientAllergy(selectedBean);
            refreshData();
        } catch (FailedRemoveException e){
            logger.log(Level.WARNING, "Error while removing allergy.", e);
            displayError("Error occurred while removing allergy");
        }
    }


    @FXML
    void clickedOnAddAllergy() {
        errorLabel.setText("");

        AllergenBean selectedBean = allergensListView.getSelectionModel().getSelectedItem();

        if (selectedBean == null) {
            displayError("Please select an allergen to add first");
            return;
        }

        try {
            allergiesController.insertAllergy(selectedBean);
            refreshData();
        } catch (FailedInsertException e){
            logger.log(Level.WARNING, "Error while adding allergy.", e);
            displayError("Error occurred while adding allergy");
        }
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


    @FXML
    void clickedOnRequests() {
        FxmlLoader2.setPage("ClientRequestsPage2");
    }

    @FXML
    void clickedOnHomepage() {
        FxmlLoader2.setPage("ClientHomePage2");
    }


    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }


}
