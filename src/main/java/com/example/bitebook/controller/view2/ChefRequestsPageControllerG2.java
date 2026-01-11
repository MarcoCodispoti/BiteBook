package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ManageServiceRequestController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.model.enums.RequestStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChefRequestsPageControllerG2{


    private static final Logger logger = Logger.getLogger(ChefRequestsPageControllerG2.class.getName());


    @FXML
    private ListView<ServiceRequestBean> chefRequestsListView;
    @FXML
    private Label messageLabel;


    private final ManageServiceRequestController manageServiceRequestController = new ManageServiceRequestController();



    @FXML
    void handleRejectRequest() {
        manageRequest(RequestStatus.REJECTED);
    }



    @FXML
    void handleApproveRequest() {
        manageRequest(RequestStatus.APPROVED);
    }



    @FXML
    void handleMenus() {
        displayError("Not implemented yet, sorry :(");
    }



    @FXML
    void handleHomepage() {
        FxmlLoader2.setPage("ChefHomePage2");
    }



    @FXML
    void initialize() {
        setupListView();
        refreshRequests();
    }



    private void setupListView() {
        chefRequestsListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(ServiceRequestBean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatRequestString(item));
                    setFont(Font.font("Monospaced", 12));
                }
            }
        });
    }



    private void refreshRequests() {
        chefRequestsListView.getItems().clear();
        messageLabel.setText("");

        try {
            List<ServiceRequestBean> chefRequestBeans = manageServiceRequestController.getChefRequests();

            if (chefRequestBeans != null && !chefRequestBeans.isEmpty()) {
                chefRequestsListView.getItems().addAll(chefRequestBeans);
            } else {
                displayError("No pending requests found.");
            }
        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Error occurred while getting chef requests",e);
            displayError("Error occurred while getting chef requests");
        }
    }



    private String formatRequestString(ServiceRequestBean bean) {
        return String.format(" %-4d  %-20s %-20s %-10s %-5d  %-4d €   %-9s    %-7s  %-30s  %12s",
                bean.getId(),
                truncate(bean.getClientBean().getName() + " " + bean.getClientBean().getSurname(),20),
                truncate(bean.getMenuBean().getName(),20),
                bean.getReservationDetailsBean().getSelectedMenuLevel().toString().toLowerCase(),
                bean.getReservationDetailsBean().getParticipantNumber(),
                bean.getTotalPrice(),
                bean.getReservationDetailsBean().getDate(),
                bean.getReservationDetailsBean().getTime(),
                truncate(bean.getReservationDetailsBean().getAddress(), 30),
                bean.getStatus().toString().toLowerCase()
        );
    }



    private void manageRequest(RequestStatus setStatus) {
        messageLabel.setText("");
        ServiceRequestBean selectedBean = chefRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedBean != null) {
            selectedBean.setStatus(setStatus);
            try {
                manageServiceRequestController.updateRequestStatus(selectedBean);
                refreshRequests();
            } catch (FailedUpdateException e){
                logger.log(Level.SEVERE, "Error occurred while managing request",e);
                displayError("Error occurred while trying to update the request");
            }
        } else {
            displayError("Please select a request first.");
        }
    }



    private String truncate(String input, int width) {
        if (input == null) return "";
        return input.length() > width ? input.substring(0, width - 1) + "." : input;
    }



    private void displayError(String message){
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
