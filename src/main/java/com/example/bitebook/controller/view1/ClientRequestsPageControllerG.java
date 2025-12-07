package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ClientRequestsPageControllerG{

private static final String REQUEST_CARD_PATH = "/com/example/bitebook/view1/ClientRequestCard.fxml";

    private List<ServiceRequestBean> serviceRequestBeans;


    @FXML
    private VBox requestsVBox;


    @FXML
    private Label errorLabel;


    @FXML
    void clickedOnHomePage() {
        FxmlLoader.setPage("ClientHomePage");
    }

    @FXML
    void clickedOnAllergies() {
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
                errorLabel.setText("No active request at the moment.");
            }

        } catch (FailedSearchException e) {
            errorLabel.setText("Error while obtaining request");
        } catch (Exception e) {
            errorLabel.setText("Unexpected error while obtaining request");
        }
    }


    private void populateRequests() {
        requestsVBox.getChildren().clear();

        for (ServiceRequestBean serviceRequestBean : serviceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(REQUEST_CARD_PATH));
                Parent clientRequestCard = cardLoader.load();


                ClientRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setParentController(this);

                requestsVBox.getChildren().add(clientRequestCard);

            } catch (IOException e){
                System.err.println("Error while loading request: " + serviceRequestBean.getId());

            }
        }
    }


}
