package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.util.ViewsResourcesPaths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRequestsPageControllerG{

    private static final Logger logger = Logger.getLogger(ClientRequestsPageControllerG.class.getName());


    private List<ServiceRequestBean> serviceRequestBeans;


    @FXML
    private VBox requestsVBox;


    @FXML
    private Label messageLabel;


    @FXML
    void handleHomePage() {
        FxmlLoader.setPage("ClientHome" +
                "Page");
    }

    @FXML
    void handleAllergies() {
        FxmlLoader.setPage("AllergiesPage");
    }

    @FXML
    public void initialize(){
        RequestManagerController requestManagerController = new RequestManagerController();

        try {
            this.serviceRequestBeans = requestManagerController.getClientRequests();
            if (this.serviceRequestBeans != null && !this.serviceRequestBeans.isEmpty()) {
                populateRequests();
            } else {
                displayError("No active request at the moment.");
            }
        } catch (FailedSearchException e) {
            displayError("Error while obtaining request");
            logger.log(Level.WARNING, "Error while obtainig request", e);
        } catch (Exception e) {
            displayError("Unexpected error while obtaining request");
            logger.log(Level.SEVERE, "Unexpected error while obtaining request", e);
        }
    }


    private void populateRequests() {
        requestsVBox.getChildren().clear();

        for (ServiceRequestBean serviceRequestBean : serviceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(ViewsResourcesPaths.CLIENT_REQUEST_CARD_PATH));
                Parent clientRequestCard = cardLoader.load();
                ClientRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                requestsVBox.getChildren().add(clientRequestCard);

            } catch (IOException e){
                displayError("Error while loading request");
                logger.log(Level.WARNING, "Error while loading some requests" , e);
            }
        }
    }


    private void displayError(String message){
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
