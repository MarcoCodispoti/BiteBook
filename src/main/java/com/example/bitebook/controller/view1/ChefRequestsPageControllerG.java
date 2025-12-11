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


    private final RequestManagerController requestManagerController = new RequestManagerController();

    private Parent selectedCardUi;
    private ServiceRequestBean selectedServiceRequestBean;

    private List<ServiceRequestBean> chefServiceRequestBeans;

    @FXML private VBox requestsVBox;
    @FXML private Label errorLabel;


    @FXML
    void clickedOnHomePage() {
        FxmlLoader.setPage("ChefHomePage");
    }

    @FXML
    void clickedOnMenus() {
        displayError("Coming soon!");
    }

    @FXML
    void initialize() {
        refreshPage();
    }

    private void refreshPage(){
        errorLabel.setText("");
        requestsVBox.getChildren().clear();
        selectedServiceRequestBean = null;
        selectedCardUi = null;
        try {
            this.chefServiceRequestBeans = requestManagerController.getChefRequests();
            populateRequests();
        } catch (FailedSearchException e) {
            displayError("Unable to load requests");
            logger.log(Level.SEVERE,"Unable to load requests",e);
        }
    }


    private void populateRequests() {
        if (chefServiceRequestBeans == null || chefServiceRequestBeans.isEmpty()) {
            displayError("No incoming requests found.");
            return;
        }

        for (ServiceRequestBean serviceRequestBean : chefServiceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(ViewsResourcesPaths.CHEF_REQUEST_CARD_PATH));
                Parent chefRequestCard = cardLoader.load();

                ChefRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setCardUi(chefRequestCard);
                controller.setParentController(this);
                requestsVBox.getChildren().add(chefRequestCard);

            } catch (IOException e){
                displayError("Error loading a request");
                logger.log(Level.WARNING,"Error loading a request", e );
            }
        }
    }


    @FXML
    void clickedOnApproveRequest() {
        updateRequestStatus(RequestStatus.APPROVED);
    }

    @FXML
    void clickedOnRejectRequest() {
        updateRequestStatus(RequestStatus.REJECTED);
    }


    private void updateRequestStatus(RequestStatus newStatus) {
        if (selectedServiceRequestBean == null) {
            displayError("Seleziona una richiesta dalla lista prima di procedere.");
            return;
        }
        try {
            selectedServiceRequestBean.setStatus(newStatus);
            requestManagerController.updateRequestStatus(selectedServiceRequestBean);
            refreshPage();
        } catch (FailedUpdateException e) {
            displayError("Error occurred while managing the request: ");
            logger.log(Level.WARNING,"Error occurred while managing the request",e);
        }
    }


    public void setSelectedRequest(ServiceRequestBean serviceRequestBean, Parent cardUi) {
        this.selectedServiceRequestBean = serviceRequestBean;
        if (selectedCardUi != null) {
            selectedCardUi.setStyle("");
        }
        selectedCardUi = cardUi;
        selectedCardUi.setStyle("-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;");
    }


    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

}

