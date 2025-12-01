package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.model.enums.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChefRequestsPageControllerG{
    private Parent selectedCardUi;
    private List<ServiceRequestBean> chefServiceRequestBeans;
    private ServiceRequestBean selectedServiceRequestBean;

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private Hyperlink menusHyperlink;

    @FXML
    private VBox requestsVBox;

    @FXML
    private ScrollPane requestsScrollPane;

    @FXML
    private Label errorLabel;

    @FXML
    private Button rejectRequestButton;

    @FXML
    private Button approveRequestButton;


    @FXML
    void clickedOnHomePage(ActionEvent event) {
        FxmlLoader.setPage("ChefHomePage");
    }

    @FXML
    void clickedOnMenus(ActionEvent event) {
        errorLabel.setText("Not implemented yet :");
        errorLabel.setVisible(true);
    }

    @FXML
    void clickedOnRejectRequest(ActionEvent event) {
//        if(selectedServiceRequestBean==null){
//            errorLabel.setText("Select a request first");
//            return;
//        }
//        selectedServiceRequestBean.setStatus(RequestStatus.REJECTED);
//        try {
//            ManageRequestController.manageRequest(selectedServiceRequestBean);
//        } catch (Exception e) {
//            errorLabel.setText("Unable to reject request");
//        }
        manageRequest(RequestStatus.REJECTED);
        initialize();
    }

    @FXML
    void clickedOnApproveRequest(ActionEvent event){
//        if(selectedServiceRequestBean==null){
//            errorLabel.setText("Select a request first");
//            return;
//        }
//        selectedServiceRequestBean.setStatus(RequestStatus.APPROVED);
//        try {
//            ManageRequestController.manageRequest(selectedServiceRequestBean);
//        } catch (Exception e) {
//            errorLabel.setText("Unable to manage request");
//        }
        manageRequest(RequestStatus.APPROVED);
        initialize();
    }


    public void initialize(){
        try {
            RequestManagerController requestManagerController = new RequestManagerController();
            this.chefServiceRequestBeans = requestManagerController.getChefRequests();
            selectedServiceRequestBean = null;
            errorLabel.setText( "caricate " + chefServiceRequestBeans.size() + " richieste ottenute ");
            populateRequests();
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            errorLabel.setText("Error :");
        }
    }

    public void populateRequests(){
        requestsVBox.getChildren().clear();

        for(ServiceRequestBean serviceRequestBean : chefServiceRequestBeans){
            try{
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/ChefRequestCard.fxml"));
                Parent chefRequestCard = cardLoader.load();

                ChefRequestCardControllerG controller = cardLoader.getController();
                controller.initData(serviceRequestBean);
                controller.setCardUi(chefRequestCard);
                controller.setParentController(this);

                requestsVBox.getChildren().add(chefRequestCard);
            } catch (Exception e){
                e.printStackTrace();
                e.getCause();
                e.getMessage();
                errorLabel.setText("Error ");
            }
        }
    }

    public void setSelectedSequest(ServiceRequestBean serviceRequestBean, Parent cardUi){
        this.selectedServiceRequestBean = serviceRequestBean;

        if(selectedCardUi !=null){
            selectedCardUi.setStyle("");
        }
        selectedCardUi = cardUi;
        selectedCardUi.setStyle("-fx-border-color: #383397; -fx-border-width: 3; -fx-border-radius: 2;");
    }

    public void manageRequest(RequestStatus requestStatus){
        if(selectedServiceRequestBean==null){
            errorLabel.setText("Select a request first");
            return;
        }
        selectedServiceRequestBean.setStatus(requestStatus);
        try {
            RequestManagerController requestManagerController = new RequestManagerController();
            requestManagerController.manageRequest(selectedServiceRequestBean);
        } catch (Exception e) {
            errorLabel.setText("Unable to manage request");
        }
    }

}

