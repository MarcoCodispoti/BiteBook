package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.LoginController;
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

public class ChefHomePageControllerG {


    private static final Logger logger = Logger.getLogger(ChefHomePageControllerG.class.getName());


    @FXML
    private VBox approvedRequestsVBox;
    @FXML
    private Label messageLabel;


    private final RequestManagerController requestManagerController = new RequestManagerController();
    private final LoginController loginController = new LoginController();
    private List<ServiceRequestBean> approvedServiceRequestBeans;



    @FXML
    void handleRequests(){
        FxmlLoader.setPage("ChefRequestsPage");
    }



    @FXML
    void handleMenus(){
        displayMessage("Coming soon!");
    }



    @FXML
    void handleLogout(){
        loginController.logout();
        FxmlLoader.setPage("WelcomePage");
    }



    @FXML
    void initialize() {
        refreshDashboard();
    }



    private void refreshDashboard(){
        messageLabel.setText("");
        approvedRequestsVBox.getChildren().clear();
        try {
            this.approvedServiceRequestBeans = requestManagerController.getApprovedServiceRequests();
            populateRequests();
        } catch (FailedSearchException e) {
            displayMessage("System Error: Unable to load requests.");
            logger.log(Level.SEVERE, "Unable to load requests", e);
        }
    }



    private void populateRequests(){
        if (approvedServiceRequestBeans == null || approvedServiceRequestBeans.isEmpty()) {
            displayMessage("No active request at the moment.");
            return;
        }
        for (ServiceRequestBean serviceRequestBean : approvedServiceRequestBeans) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(ViewsResourcesPaths.CHEF_APPROVED_REQUEST_CARD_PATH));
                Parent approvedRequestCard = cardLoader.load();
                ApprovedRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setParentController(this);
                approvedRequestsVBox.getChildren().add(approvedRequestCard);
            } catch (IOException e){
                logger.log(Level.WARNING, "Error while loading a card for a request ", e);
            }
        }
    }


    private void displayMessage(String message){
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
