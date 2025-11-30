package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.controller.application.ManageRequestController;
import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.Vector;

public class ChefHomePageControllerG {
    private Vector<ServiceRequestBean> approvedServiceRequestBeans;

    @FXML
    private Hyperlink menusHyperlink;

    @FXML
    private VBox approvedRequestsVBox;

    @FXML
    private ScrollPane activeReservationsScrollPane;

    @FXML
    private Hyperlink reservationsHyperlink;

    @FXML
    private Hyperlink profileHyperlink;

    @FXML
    private Hyperlink logoutHyperlink;

    @FXML
    private Label errorLabel;

    @FXML
    void clickedOnRequests(ActionEvent event) {
        FxmlLoader.setPage("ChefRequestsPage");
    }

    @FXML
    void clickedOnMenus(ActionEvent event) {
        errorLabel.setText("Not implemented yet :(");
        errorLabel.setVisible(true);
    }


    @FXML
    void clickedOnLogout(ActionEvent event) {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader.setPage("WelcomePage");
    }

    public void initialize(){
        try {
            ExplorationController explorationController = new ExplorationController();
            this.approvedServiceRequestBeans = explorationController.getApprovedServiceRequests();
        } catch (Exception e){
            this.approvedServiceRequestBeans = new Vector<>();
        }
        errorLabel.setText("Ho trovato " + approvedServiceRequestBeans.size() + " richieste accettate dal DAO");
        populateRequests();
    }

    private void populateRequests(){
        approvedRequestsVBox.getChildren().clear();

        for(ServiceRequestBean serviceRequestBean : approvedServiceRequestBeans){
            try{
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/ApprovedRequestCard.fxml"));
                Parent approvedRequestCard = cardLoader.load();

                ApprovedRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                // controller.setCardUi()
                controller.setParentController(this);

                approvedRequestsVBox.getChildren().add(approvedRequestCard);

            } catch (Exception e) {
                {
                    e.printStackTrace();
                    e.getCause();
                    e.getMessage();
                    return;
                }
            }
        }
    }



}
