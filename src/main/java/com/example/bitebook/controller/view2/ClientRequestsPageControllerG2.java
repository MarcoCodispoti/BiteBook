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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRequestsPageControllerG2{


    private static final Logger logger = Logger.getLogger(ClientRequestsPageControllerG2.class.getName());


    @FXML
    private ListView<ServiceRequestBean> clientRequestsListView;
    @FXML
    private Label messageLabel;


    private final RequestManagerController requestManagerController = new RequestManagerController();



    @FXML
    void handleAllergies() {
        FxmlLoader2.setPage("AllergiesPage2");
    }



    @FXML
    void handleHomepage() {
        FxmlLoader2.setPage("ClientHomePage2");
    }



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
        if (messageLabel != null) messageLabel.setText("");

        try {
            List<ServiceRequestBean> clientRequests = requestManagerController.getClientRequests();

            if (clientRequests != null && !clientRequests.isEmpty()) {
                clientRequestsListView.getItems().addAll(clientRequests);
            } else {
                displayMessage("No requests found.");
            }

        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Unable to retrive requests" ,e);
            displayMessage("System Error: Unable to retrieve requests.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected system error" ,e);
            displayMessage("An unexpected error occurred.");
        }
    }



    private String formatRequestString(ServiceRequestBean bean) {
        return String.format(" %-4d  %-20s %-26s %-10s %-5d  %-4d €   %-9s    %-7s  %-30s  %12s",
                bean.getId(),
                truncate(bean.getChefBean().getName() + " " + bean.getChefBean().getSurname(),20),
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



    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
