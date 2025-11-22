package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.SendServiceRequestController;
import com.example.bitebook.model.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

import java.util.Vector;

public class PaymentPageControllerG {
    MenuBean requestMenuBean;
    ReservationDetailsBean requestReservationDetailsBean;
    Vector<AllergenBean> reservationAllergenBeans;
    ChefBean reservationChefBean;
    ServiceRequestBean serviceRequestBean;

    @FXML
    private Button sendRequestButton;

    @FXML
    private Hyperlink requestsHyperlink;


    @FXML
    private Button backButton;


    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Label errorLabel;



    @FXML
    private Label nameLabel;

    @FXML
    private Label numberOfCoursesLabel;

    @FXML
    private Label themeLabel;

    @FXML
    private Label dietTypeLabel;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label numberOfParticipantsLabel;

    @FXML
    private Label ingredientsLevelLabel;


    @FXML
    void clickedOnSendRequest(ActionEvent event) {

        try{
            serviceRequestBean = SendServiceRequestController.fillServiceRequest(requestMenuBean,requestReservationDetailsBean);
            serviceRequestBean.validate();  // è dummy -> Implementare dopo
            System.out.println("Mando la SendServiceRequest");
            SendServiceRequestController.sendServiceRequest(serviceRequestBean);
        } catch (Exception e){
            errorLabel.setText("Error completing the request");
            e.getMessage();
            e.getCause();
            e.printStackTrace();
            return;
        }

        FxmlLoader.setPage("ConfirmationPage");
    }

    @FXML
    void clickedOnBack(ActionEvent event) {
        ServiceRequestPageControllerG serviceRequestPageControllerG = FxmlLoader.setPageAndReturnController("ServiceRequestPage");
        if (serviceRequestPageControllerG != null) {
            serviceRequestPageControllerG.initData(requestMenuBean,reservationAllergenBeans,reservationChefBean);
        }
        // FxmlLoader.setPage("ServiceRequestPage");
    }


    public void initData(MenuBean menuBean, ReservationDetailsBean reservationDetailsBean, Vector<AllergenBean> menuAllergenBeans, ChefBean menuChefBean) {
        this.requestMenuBean = menuBean;
        this.requestReservationDetailsBean = reservationDetailsBean;
        this.reservationAllergenBeans = menuAllergenBeans;
        this.reservationChefBean = menuChefBean;

        nameLabel.setText(menuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(menuBean.getNumberOfCourses()));
        dietTypeLabel.setText(menuBean.getDietType().toString().toLowerCase());
        pricePerPersonLabel.setText(String.valueOf(menuBean.getPricePerPerson()) + " €");
        dateLabel.setText(reservationDetailsBean.getDate().toString());
        timeLabel.setText(reservationDetailsBean.getTime().toString());
        addressLabel.setText(reservationDetailsBean.getAddress());
        numberOfParticipantsLabel.setText(String.valueOf(reservationDetailsBean.getParticipantNumber()));
        ingredientsLevelLabel.setText(reservationDetailsBean.getSelectedMenuLevel().toString().toLowerCase());
        totalPriceLabel.setText(String.valueOf(SendServiceRequestController.calculateTotalPrice(reservationDetailsBean,menuBean)));

    }





}
