package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.LoginController;
import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.util.List;

public class ChefHomePageControllerG2{


    private final RequestManagerController requestManagerController = new RequestManagerController();

    @FXML private Label errorLabel;

    @FXML private ListView<ServiceRequestBean> approvedRequestsListView;

    @FXML
    void initialize() {
        setupListViewFactory();
        loadApprovedRequests();
    }

    private void setupListViewFactory() {
        approvedRequestsListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(ServiceRequestBean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setFont(Font.font("Monospaced", 12));
                    setText(formatRequestString(item));
                }
            }
        });
    }

    private void loadApprovedRequests() {
        approvedRequestsListView.getItems().clear();
        errorLabel.setText("");

        try {
            List<ServiceRequestBean> approvedRequests = requestManagerController.getApprovedServiceRequests();

            if (approvedRequests == null || approvedRequests.isEmpty()) {
                errorLabel.setText("No approved requests found");
                return;
            }
            approvedRequestsListView.getItems().addAll(approvedRequests);

        } catch (FailedSearchException e) {
            errorLabel.setText("Error occurred while obtaining approved requests");
        }
    }

    private String formatRequestString(ServiceRequestBean bean){
        return String.format(" %-22s %-23s %-10s %-11d  %-12s  %-9s %s",
                truncate(bean.getClientBean().getName() + " " + bean.getClientBean().getSurname(), 22),    // Helper per evitare che nomi lunghi rompano l'allineamento
                truncate(bean.getMenuBean().getName(), 20),
                bean.getReservationDetailsBean().getSelectedMenuLevel().toString().toLowerCase(),
                bean.getReservationDetailsBean().getParticipantNumber(),
                bean.getReservationDetailsBean().getDate(),
                bean.getReservationDetailsBean().getTime(),
                bean.getReservationDetailsBean().getAddress()
        );
    }


    private String truncate(String input, int width) {
        if (input == null) return "";
        return input.length() > width ? input.substring(0, width - 1) + "." : input;
    }

    @FXML
    void clickedOnMenus() {
        errorLabel.setText("Not implemented yet, sorry :(");
    }

    @FXML
    void clickedOnRequests() {
        FxmlLoader2.setPage("ChefRequestsPage2");
    }

    @FXML
    void clickedOnLogoutButton() {
        LoginController loginController = new LoginController();
        loginController.logout();
        FxmlLoader2.setPage("LoginPage2");
    }


}
