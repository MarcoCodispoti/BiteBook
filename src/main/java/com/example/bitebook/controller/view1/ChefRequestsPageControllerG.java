package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.model.enums.RequestStatus;
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

public class ChefRequestsPageControllerG{


    private static final Logger logger = Logger.getLogger(ChefRequestsPageControllerG.class.getName());


    @FXML
    private VBox requestsVBox;
    @FXML
    private Label messageLabel;


    private final RequestManagerController requestManagerController = new RequestManagerController();
    private Parent selectedCardUi;
    private ServiceRequestBean selectedServiceRequestBean;
    private List<ServiceRequestBean> chefServiceRequestBeans;

    private static final String SELECTED_STYLE = "-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;";
    private static final String UNSELECTED_STYLE = "-fx-border-color: #CCCCCC; -fx-border-width: 2; -fx-border-radius: 2;";



    @FXML
    void handleHomePage() {
        FxmlLoader.setPage("ChefHomePage");
    }



    @FXML
    void handleMenus() {
        displayMessage("Coming soon!");
    }



    @FXML
    void handleApproveRequest() {
        updateRequestStatus(RequestStatus.APPROVED);
    }



    @FXML
    void handleRejectRequest() {
        updateRequestStatus(RequestStatus.REJECTED);
    }



    @FXML
    void initialize() {
        refreshPage();
    }



    private void refreshPage(){
        messageLabel.setText("");
        requestsVBox.getChildren().clear();
        selectedServiceRequestBean = null;
        selectedCardUi = null;
        try {
            this.chefServiceRequestBeans = requestManagerController.getChefRequests();
            populateRequests();
        } catch (FailedSearchException e) {
            displayMessage("Unable to load requests");
            logger.log(Level.SEVERE,"Unable to load requests",e);
        }
    }



    private void populateRequests() {
        if (chefServiceRequestBeans == null || chefServiceRequestBeans.isEmpty()) {
            displayMessage("No incoming requests found.");
            return;
        }
        for (ServiceRequestBean serviceRequestBean : chefServiceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(ViewsResourcesPaths.CHEF_REQUEST_CARD_PATH));
                Parent chefRequestCard = cardLoader.load();

                chefRequestCard.setStyle(UNSELECTED_STYLE);

                ChefRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setCardUi(chefRequestCard);
                controller.setParentController(this);
                requestsVBox.getChildren().add(chefRequestCard);

            } catch (IOException e){
                displayMessage("Error loading a request");
                logger.log(Level.WARNING,"Error loading a request", e );
            }
        }
    }



    private void updateRequestStatus(RequestStatus newStatus) {
        if (selectedServiceRequestBean == null) {
            displayMessage("Please select a request from the list first.");
            return;
        }
        try {
            selectedServiceRequestBean.setStatus(newStatus);
            requestManagerController.updateRequestStatus(selectedServiceRequestBean);
            refreshPage();
        } catch (FailedUpdateException e) {
            displayMessage("Error occurred while managing the request: ");
            logger.log(Level.WARNING,"Error occurred while managing the request",e);
        }
    }



    public void setSelectedRequest(ServiceRequestBean serviceRequestBean, Parent cardUi) {
        this.selectedServiceRequestBean = serviceRequestBean;
        if (selectedCardUi != null) {
            selectedCardUi.setStyle(UNSELECTED_STYLE);
        }
        selectedCardUi = cardUi;
        selectedCardUi.setStyle(SELECTED_STYLE);
    }



    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}

