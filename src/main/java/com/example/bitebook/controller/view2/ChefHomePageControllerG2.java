package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class ChefHomePageControllerG2{

    private List<ServiceRequestBean> approvedServiceRequestBeans;
    ExplorationController explorationController =  new ExplorationController();
    RequestManagerController requestManagerController = new RequestManagerController();


    @FXML
    private Hyperlink menusHyperlink;

    @FXML
    private Button logoutButton;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Button manageRequestsButton;

    @FXML
    private Label errorLabel;

    @FXML
    private ListView<String> approvedRequestsListView;

    @FXML
    void clickedOnMenus(ActionEvent event) {
        errorLabel.setText("Not implemented yet, sorry :(");
    }

    @FXML
    void clickedOnRequests(ActionEvent event) {
        FxmlLoader2.setPage("ChefRequestsPage2");
    }

    @FXML
    void clickedOnLogoutButton(ActionEvent event) {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader2.setPage("LoginPage2");
    }

    @FXML
    void initialize(){
        fillApprovedRequestList();
    }

    private void fillApprovedRequestList(){
        approvedRequestsListView.getItems().clear();

        try {
            // this.approvedServiceRequestBeans = explorationController.getApprovedServiceRequests();

            this.approvedServiceRequestBeans = requestManagerController.getApprovedServiceRequests();


        }catch(Exception e){
            errorLabel.setText("Error occured while obtaining approved requests");
            return;
        }

        if(approvedServiceRequestBeans == null){
            errorLabel.setText("No approved requests found");
        }
        for(ServiceRequestBean serviceRequestBean : approvedServiceRequestBeans){
            approvedRequestsListView.getItems().add(convertRequestAsString(serviceRequestBean));
        }
    }

    private String convertRequestAsString(ServiceRequestBean serviceRequestBean){
        return "Client: " + serviceRequestBean.getClientBean().getName() + " " + serviceRequestBean.getClientBean().getSurname() + "  "
                + "Menu: " + serviceRequestBean.getMenuBean().getName() + "  "
                + "Level: " + String.valueOf(serviceRequestBean.getReservationDetails().getSelectedMenuLevel()).toLowerCase() + "  "
                + "Participants: " + serviceRequestBean.getReservationDetails().getParticipantNumber()+ "  "
                + "Date: " + serviceRequestBean.getReservationDetails().getDate() + "  "
                + "Time: " + serviceRequestBean.getReservationDetails().getTime() + "  "
                + "Address: " + serviceRequestBean.getReservationDetails().getAddress() ;
    }


}
