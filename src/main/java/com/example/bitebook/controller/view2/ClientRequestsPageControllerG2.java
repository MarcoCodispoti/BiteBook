package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;

import java.util.List;

public class ClientRequestsPageControllerG2{

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private ListView<String> clientRequestsListView;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    void clickedOnAllergies(ActionEvent event) {
        FxmlLoader2.setPage("AllergiesPage2");
    }

    @FXML
    void clickedOnHomepage(ActionEvent event) {
        FxmlLoader2.setPage("ClientHomePage2");
    }

    @FXML
    void initialize(){
        fillClientRequestsListView();
    }

    private void fillClientRequestsListView(){
        clientRequestsListView.getItems().clear();

        RequestManagerController requestManagerController = new RequestManagerController();



        try{
            List<ServiceRequestBean> clientRequestListBeans = requestManagerController.getClientRequests();

            for (ServiceRequestBean serviceRequestBean : clientRequestListBeans) {
                String serviceRequestString = "";
                serviceRequestString = serviceRequestString.concat("ID: " + serviceRequestBean.getId() + "    " + " Chef:  " + serviceRequestBean.getChefBean().getName() + " " + serviceRequestBean.getChefBean().getSurname() + "      ");
                serviceRequestString = serviceRequestString.concat("Menu: " + serviceRequestBean.getMenuBean().getName() + "      Menu Level: " + serviceRequestBean.getReservationDetailsBean().getSelectedMenuLevel().toString().toLowerCase() + "    ");
                serviceRequestString = serviceRequestString.concat("Total Price: " + serviceRequestBean.getTotalPrice() + " €      Date: " + serviceRequestBean.getReservationDetailsBean().getDate() + "       Time: " + serviceRequestBean.getReservationDetailsBean().getTime() + "    ");
                serviceRequestString = serviceRequestString.concat("Address: " + serviceRequestBean.getReservationDetailsBean().getAddress() + "     Status: " + String.valueOf(serviceRequestBean.getStatus())).toLowerCase();

                clientRequestsListView.getItems().add(serviceRequestString);
            }
        }catch (FailedSearchException e){
            // stampare errore su una label
        }
    }

}
