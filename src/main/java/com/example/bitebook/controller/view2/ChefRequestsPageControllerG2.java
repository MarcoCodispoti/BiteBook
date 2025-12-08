package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.RequestManagerController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.model.enums.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.util.List;

public class ChefRequestsPageControllerG2{

//    private List<ServiceRequestBean> chefRequestBeans;
//    private RequestManagerController requestManagerController =  new RequestManagerController();
//    private ServiceRequestBean selectedServiceRequestBean = null;
//
//
//    @FXML
//    private Hyperlink menusHyperlink;
//
//    @FXML
//    private Hyperlink homepageHyperlink;
//
//    @FXML
//    private ListView<String> chefRequestsListView;
//
//    @FXML
//    private Button rejectRequestButton;
//
//    @FXML
//    private Button approveRequestButton;
//
//    @FXML
//    private Label errorLabel;
//
//    @FXML
//    void clickedOnMenus(ActionEvent event) {
//        errorLabel.setText("Not implemented yet, sorry :(");
//    }
//
//    @FXML
//    void clickedOnHomepage(ActionEvent event) {
//        FxmlLoader2.setPage("ChefHomePage2");
//    }
//
//    @FXML
//    void clickedOnRejectRequest(ActionEvent event) {
//        manageRequest(RequestStatus.REJECTED);
//        initialize();
//    }
//
//    @FXML
//    void clickedOnApproveRequest(ActionEvent event){
//        manageRequest(RequestStatus.APPROVED);
//        initialize();
//    }
//
//
//    @FXML
//    void initialize(){
//        try {
//            this.chefRequestBeans = requestManagerController.getChefRequests();
//        } catch (Exception e){
//            errorLabel.setText("Error occured while getting chef requests");
//        }
//        fillRequests();
//    }
//
//    private void fillRequests(){
//        chefRequestsListView.getItems().clear();
//
//        for(ServiceRequestBean serviceRequestBean : chefRequestBeans){
//            chefRequestsListView.getItems().add(getReservationAsString(serviceRequestBean));
//        }
//    }
//
//    private String getReservationAsString(ServiceRequestBean serviceRequestBean) {
//        String reservationString = "ID: " + serviceRequestBean.getId() + "  Client: " + serviceRequestBean.getClientBean().getName() + " " + serviceRequestBean.getClientBean().getSurname() + "   ";
//        reservationString = reservationString.concat("Menu: " + serviceRequestBean.getMenuBean().getName() + "   Level: " + serviceRequestBean.getReservationDetailsBean().getSelectedMenuLevel().toString().toLowerCase()) + "  ";
//        reservationString = reservationString.concat("Participants: " + serviceRequestBean.getReservationDetailsBean().getParticipantNumber() + "  Total price: " + serviceRequestBean.getTotalPrice() + " €  ");
//        reservationString = reservationString.concat("  Date: " + serviceRequestBean.getReservationDetailsBean().getDate() + "  Time: " + serviceRequestBean.getReservationDetailsBean().getTime() + "  Address: " + serviceRequestBean.getReservationDetailsBean().getAddress() + "  ");
//        reservationString = reservationString.concat("Status: " + String.valueOf(serviceRequestBean.getStatus()).toLowerCase());
//        return reservationString;
//    }
//
//
//    // Questo metodo usa l'ID estratto per trovare l'oggetto originale nel tuo List
//    private ServiceRequestBean extractServiceRequestBean(String listViewRequestString){
//        // 1. Ottieni l'ID dalla stringa
//        int id = getRequestIdFromString(listViewRequestString);
//        // 2. Se l'ID è valido, cerca nel vettore chefRequestBeans
//        if (id != -1 && this.chefRequestBeans != null) {
//            for (ServiceRequestBean bean : this.chefRequestBeans) {
//                if (bean.getId() == id) {
//                    return bean; // Trovato!
//                }
//            }
//        }
//        return null;
//    }
//
//
//    private int getRequestIdFromString(String listViewRequestString){
//        // Controllo di sicurezza: se la stringa è nulla o non inizia come previsto
//        if (listViewRequestString == null || !listViewRequestString.startsWith("ID: ")) {
//            return -1;
//        }
//        try {
//            // La stringa è formattata come: "ID: [NUMERO]  Client: ..."
//            // "ID: " sono 4 caratteri. L'ID inizia all'indice 4.
//            int startIndex = 4;
//            // Cerchiamo il primo spazio dopo l'ID
//            int endIndex = listViewRequestString.indexOf(" ", startIndex);
//            if (endIndex != -1) {
//                // Estraiamo la sottostringa che contiene solo il numero
//                String idString = listViewRequestString.substring(startIndex, endIndex);
//                // Convertiamo in intero
//                return Integer.parseInt(idString);
//            }
//        } catch (NumberFormatException | IndexOutOfBoundsException e) {
//            // Gestione errori di parsing
//            e.printStackTrace();
//        }
//
//        return -1; // Ritorna -1 in caso di errore
//    }
//
//    private void manageRequest(RequestStatus setStatus){
//        String selectedString = chefRequestsListView.getSelectionModel().getSelectedItem();
//
//        if (selectedString != null) {
//            // 2. Recupera l'oggetto Bean originale
//            ServiceRequestBean selectedStatusServiceRequestBean = extractServiceRequestBean(selectedString);
//            selectedStatusServiceRequestBean.setStatus(setStatus);
//            try {
//                requestManagerController.manageRequest(selectedStatusServiceRequestBean);
//            }catch(Exception e) {
//                errorLabel.setText("Error occured while trying to approve the request");
//            }
//        } else {
//            errorLabel.setText("Please select a request first.");
//        }
//    }




    private final RequestManagerController requestManagerController = new RequestManagerController();

    @FXML private Hyperlink menusHyperlink;
    @FXML private Hyperlink homepageHyperlink;

    @FXML private ListView<ServiceRequestBean> chefRequestsListView;

    @FXML private Button rejectRequestButton;
    @FXML private Button approveRequestButton;
    @FXML private Label errorLabel;

    @FXML
    void initialize() {
        setupListView();
        refreshRequests();
    }

    private void setupListView() {
        chefRequestsListView.setCellFactory(param -> new ListCell<ServiceRequestBean>() {
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
        return String.format("ID: %-4d | Client: %-20s | Menu: %-20s | Lvl: %-8s | Part.: %-3d | Tot: %-6.2f € | Date: %-12s | Status: %s",
                bean.getId(),
                bean.getClientBean().getName() + " " + bean.getClientBean().getSurname(),
                bean.getMenuBean().getName(),
                bean.getReservationDetailsBean().getSelectedMenuLevel().toString().toLowerCase(),
                bean.getReservationDetailsBean().getParticipantNumber(),
                bean.getTotalPrice(),
                bean.getReservationDetailsBean().getDate(),
                bean.getStatus().toString().toLowerCase()
        );
    }


    @FXML
    void clickedOnRejectRequest(ActionEvent event) {
        manageRequest(RequestStatus.REJECTED);
    }


    @FXML
    void clickedOnApproveRequest(ActionEvent event) {
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
    void clickedOnMenus(ActionEvent event) {
        errorLabel.setText("Not implemented yet, sorry :(");
    }


    @FXML
    void clickedOnHomepage(ActionEvent event) {
        FxmlLoader2.setPage("ChefHomePage2");
    }




}
