package com.example.bitebook.controller.view2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class AllergiesPageControllerG2{

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private ListView<?> clientAllergiesListView;

    @FXML
    private Button removeAllergyButton;

    @FXML
    private ListView<?> allergensListView;

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
    void clickedOnRemoveAllergy(ActionEvent event) {

    }

    @FXML
    void clickedOnAddAllergy(ActionEvent event) {

    }

}
