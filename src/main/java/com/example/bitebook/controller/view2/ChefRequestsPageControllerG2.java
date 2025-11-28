package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ManageRequestController;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ServiceRequestBean;
import com.example.bitebook.model.enums.RequestStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Vector;

public class ChefRequestsPageControllerG2{

    private Vector<ServiceRequestBean> chefRequestBeans;
    private ManageRequestController manageRequestController =  new ManageRequestController();
    private ServiceRequestBean selectedServiceRequestBean = null;


    @FXML
    private Hyperlink menusHyperlink;

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private ListView<String> chefRequestsListView;

    @FXML
    private Button rejectRequestButton;

    @FXML
    private Button approveRequestButton;

    @FXML
    private Label errorLabel;

    @FXML
    void clickedOnMenus(ActionEvent event) {
        errorLabel.setText("Not implemented yet, sorry :(");
    }

    @FXML
    void clickedOnHomepage(ActionEvent event) {
        FxmlLoader2.setPage("ChefHomePage2");
    }

    @FXML
    void clickedOnRejectRequest(ActionEvent event) {
        manageRequest(RequestStatus.REJECTED);
        initialize();
    }

    @FXML
    void clickedOnApproveRequest(ActionEvent event){
        manageRequest(RequestStatus.APPROVED);
        initialize();
    }


    @FXML
    void initialize(){
        try {
            this.chefRequestBeans = manageRequestController.getChefRequests();
        } catch (Exception e){
            errorLabel.setText("Error occured while getting chef requests");
        }
        fillRequests();
    }

    private void fillRequests(){
        chefRequestsListView.getItems().clear();

        for(ServiceRequestBean serviceRequestBean : chefRequestBeans){
            chefRequestsListView.getItems().add(getReservationAsString(serviceRequestBean));
        }
    }

    private String getReservationAsString(ServiceRequestBean serviceRequestBean) {
        String reservationString = "ID: " + serviceRequestBean.getId() + "  Client: " + serviceRequestBean.getClientBean().getName() + " " + serviceRequestBean.getClientBean().getSurname() + "   ";
        reservationString = reservationString.concat("Menu: " + serviceRequestBean.getMenuBean().getName() + "   Level: " + serviceRequestBean.getReservationDetails().getSelectedMenuLevel().toString().toLowerCase()) + "  ";
        reservationString = reservationString.concat("Participants: " + serviceRequestBean.getReservationDetails().getParticipantNumber() + "  Total price: " + serviceRequestBean.getTotalPrice() + " €  ");
        reservationString = reservationString.concat("  Date: " + serviceRequestBean.getReservationDetails().getDate() + "  Time: " + serviceRequestBean.getReservationDetails().getTime() + "  Address: " + serviceRequestBean.getReservationDetails().getAddress() + "  ");
        reservationString = reservationString.concat("Status: " + String.valueOf(serviceRequestBean.getStatus()).toLowerCase());
        return reservationString;
    }


    // Questo metodo usa l'ID estratto per trovare l'oggetto originale nel tuo Vector
    private ServiceRequestBean extractServiceRequestBean(String listViewRequestString){
        // 1. Ottieni l'ID dalla stringa
        int id = getRequestIdFromString(listViewRequestString);
        // 2. Se l'ID è valido, cerca nel vettore chefRequestBeans
        if (id != -1 && this.chefRequestBeans != null) {
            for (ServiceRequestBean bean : this.chefRequestBeans) {
                if (bean.getId() == id) {
                    return bean; // Trovato!
                }
            }
        }
        return null;
    }


    private int getRequestIdFromString(String listViewRequestString){
        // Controllo di sicurezza: se la stringa è nulla o non inizia come previsto
        if (listViewRequestString == null || !listViewRequestString.startsWith("ID: ")) {
            return -1;
        }
        try {
            // La stringa è formattata come: "ID: [NUMERO]  Client: ..."
            // "ID: " sono 4 caratteri. L'ID inizia all'indice 4.
            int startIndex = 4;
            // Cerchiamo il primo spazio dopo l'ID
            int endIndex = listViewRequestString.indexOf(" ", startIndex);
            if (endIndex != -1) {
                // Estraiamo la sottostringa che contiene solo il numero
                String idString = listViewRequestString.substring(startIndex, endIndex);
                // Convertiamo in intero
                return Integer.parseInt(idString);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            // Gestione errori di parsing
            e.printStackTrace();
        }

        return -1; // Ritorna -1 in caso di errore
    }

    private void manageRequest(RequestStatus setStatus){
        String selectedString = chefRequestsListView.getSelectionModel().getSelectedItem();

        if (selectedString != null) {
            // 2. Recupera l'oggetto Bean originale
            ServiceRequestBean selectedStatusServiceRequestBean = extractServiceRequestBean(selectedString);
            selectedStatusServiceRequestBean.setStatus(setStatus);
            try {
                manageRequestController.manageRequest(selectedStatusServiceRequestBean);
            }catch(Exception e) {
                errorLabel.setText("Error occured while trying to approve the request");
            }
        } else {
            errorLabel.setText("Please select a request first.");
        }
    }






}
