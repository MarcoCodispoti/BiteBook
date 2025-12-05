package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.LoginController;
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

public class ChefHomePageControllerG {


    private final RequestManagerController requestManagerController = new RequestManagerController();
    private final LoginController loginController = new LoginController();

    private List<ServiceRequestBean> approvedServiceRequestBeans;

    @FXML private VBox approvedRequestsVBox;

    @FXML private Label errorLabel;



    @FXML
    void clickedOnRequests(){
        FxmlLoader.setPage("ChefRequestsPage");
    }

    @FXML
    void clickedOnMenus(){
        errorLabel.setText("Coming soon!");
    }

    @FXML
    void clickedOnLogout(){
        loginController.logout();
        FxmlLoader.setPage("WelcomePage");
    }



    @FXML
    void initialize() {
        refreshDashboard();
    }


    private void refreshDashboard(){
        errorLabel.setText("");
        approvedRequestsVBox.getChildren().clear();
        try {
            this.approvedServiceRequestBeans = requestManagerController.getApprovedServiceRequests();
            populateRequests();
        } catch (FailedSearchException e) {
            errorLabel.setText("System Error: Unable to load requests.");
        }
    }


    private void populateRequests() {
        if (approvedServiceRequestBeans == null || approvedServiceRequestBeans.isEmpty()) {
            errorLabel.setText("Nessuna richiesta attiva al momento.");
            return;
        }
        for (ServiceRequestBean serviceRequestBean : approvedServiceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/ApprovedRequestCard.fxml"));
                Parent approvedRequestCard = cardLoader.load();
                ApprovedRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setParentController(this);
                approvedRequestsVBox.getChildren().add(approvedRequestCard);
            } catch (IOException e){
                System.err.println("Error while loading the card for the request with ID: " + serviceRequestBean.getId());
            }
        }
    }

}
