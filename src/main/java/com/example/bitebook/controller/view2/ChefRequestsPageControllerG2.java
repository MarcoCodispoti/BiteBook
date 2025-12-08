package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.model.enums.RequestStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.util.List;

public class ChefRequestsPageControllerG2{
    
    private final RequestManagerController requestManagerController = new RequestManagerController();

    @FXML private ListView<ServiceRequestBean> chefRequestsListView;
    @FXML private Label errorLabel;

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
        errorLabel.setText("");

        try {
            List<ServiceRequestBean> chefRequestBeans = requestManagerController.getChefRequests();

            if (chefRequestBeans != null && !chefRequestBeans.isEmpty()) {
                chefRequestsListView.getItems().addAll(chefRequestBeans);
            } else {
                errorLabel.setText("No pending requests found.");
            }
        } catch (FailedSearchException e){
            errorLabel.setText("Error occurred while getting chef requests");
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


    @FXML
    void clickedOnRejectRequest() {
        manageRequest(RequestStatus.REJECTED);
    }


    @FXML
    void clickedOnApproveRequest() {
        manageRequest(RequestStatus.APPROVED);
    }


    private void manageRequest(RequestStatus setStatus) {
        errorLabel.setText("");
        ServiceRequestBean selectedBean = chefRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedBean != null) {
            selectedBean.setStatus(setStatus);
            try {
                requestManagerController.manageRequest(selectedBean);
                refreshRequests();
            } catch (FailedUpdateException e) {
                errorLabel.setText("Error occurred while trying to update the request");
            }
        } else {
            errorLabel.setText("Please select a request first.");
        }
    }


    @FXML
    void clickedOnMenus() {
        errorLabel.setText("Not implemented yet, sorry :(");
    }


    @FXML
    void clickedOnHomepage() {
        FxmlLoader2.setPage("ChefHomePage2");
    }

    private String truncate(String input, int width) {
        if (input == null) return "";
        return input.length() > width ? input.substring(0, width - 1) + "." : input;
    }


}
