package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.model.enums.RequestStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ChefRequestsPageControllerG{

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
        errorLabel.setText("Coming soon!");
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
            errorLabel.setText("Unable to load requests");
        }
    }


    private void populateRequests() {
        if (chefServiceRequestBeans == null || chefServiceRequestBeans.isEmpty()) {
            errorLabel.setText("No incoming requests found.");
            return;
        }

        for (ServiceRequestBean serviceRequestBean : chefServiceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/ChefRequestCard.fxml"));
                Parent chefRequestCard = cardLoader.load();

                ChefRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setCardUi(chefRequestCard);
                controller.setParentController(this);

                requestsVBox.getChildren().add(chefRequestCard);

            } catch (IOException e){
                System.err.println("Error loading request ID: " + serviceRequestBean.getId());
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
            errorLabel.setText("Seleziona una richiesta dalla lista prima di procedere.");
            return;
        }
        try {
            selectedServiceRequestBean.setStatus(newStatus);
            requestManagerController.manageRequest(selectedServiceRequestBean);
            refreshPage();
        } catch (FailedUpdateException e) {
            errorLabel.setText("Error occurred while managing the request: " + e.getMessage());
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

}

