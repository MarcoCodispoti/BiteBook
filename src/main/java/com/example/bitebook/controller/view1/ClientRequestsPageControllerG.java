package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ClientRequestsPageControllerG{
    private List<ServiceRequestBean> serviceRequestBeans;

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private ScrollPane menusScrollPane;

    @FXML
    private VBox requestsVBox;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Label errorLabel;


    @FXML
    void clickedOnHomePage(ActionEvent event) {
        FxmlLoader.setPage("ClientHomePage");
    }

    @FXML
    void clickedOnAllergies(ActionEvent event) {
        FxmlLoader.setPage("AllergiesPage");
    }

    public void initialize(){
//        ExplorationController explorationController = new ExplorationController();
//        this.serviceRequestBeans = explorationController.getClientRequests();

        RequestManagerController requestManagerController = new RequestManagerController();
        try {
            this.serviceRequestBeans = requestManagerController.getClientRequests();
            populateRequests();
        } catch (FailedSearchException e){
            errorLabel.setText("Error while obtaining requests");
        }
    }


    private void populateRequests() {
        requestsVBox.getChildren().clear();

        for (ServiceRequestBean serviceRequestBean : serviceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/ClientRequestCard.fxml"));
                Parent clientRequestCard = cardLoader.load();

                ClientRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setParentController(this);

                requestsVBox.getChildren().add(clientRequestCard);


            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
                e.getMessage();
                return;
            }
        }
    }


}
