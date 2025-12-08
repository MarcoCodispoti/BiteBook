package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;

import java.util.List;

public class ClientRequestsPageControllerG2{


    private final RequestManagerController requestManagerController = new RequestManagerController();

    @FXML
    private ListView<ServiceRequestBean> clientRequestsListView;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() {
        setupListView();
        loadClientRequests();
    }


    private void setupListView() {
        clientRequestsListView.setCellFactory(_ -> new ListCell<>() {
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

    private void loadClientRequests() {
        clientRequestsListView.getItems().clear();
        if (errorLabel != null) errorLabel.setText("");

        try {
            List<ServiceRequestBean> clientRequests = requestManagerController.getClientRequests();

            if (clientRequests != null && !clientRequests.isEmpty()) {
                clientRequestsListView.getItems().addAll(clientRequests);
            } else {
                showError("No requests found.");
            }

        } catch (FailedSearchException e){
            showError("System Error: Unable to retrieve requests.");
        } catch (Exception e) {
            showError("An unexpected error occurred.");
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


    private String truncate(String input, int width) {
        if (input == null) return "";
        return input.length() > width ? input.substring(0, width - 1) + "." : input;
    }


    @FXML
    void clickedOnAllergies() {
        FxmlLoader2.setPage("AllergiesPage2");
    }

    @FXML
    void clickedOnHomepage() {
        FxmlLoader2.setPage("ClientHomePage2");
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }



}
