package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.SendServiceRequestController;
import com.example.bitebook.model.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class PaymentPageControllerG2 {

    SendServiceRequestController sendServiceRequestController =  new SendServiceRequestController();

    private ChefBean chefBean;
    private MenuBean selectedMenuBean;
    private ReservationDetailsBean reservationDetailsBean;
    private List<AllergenBean> selectedMenuAllergenBeans;

    private ServiceRequestBean serviceRequestBean;



    @FXML
    private Button payButton;

    @FXML
    private Label numberOfCoursesLabel;

    @FXML
    private AnchorPane paymentAnchorPane;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private AnchorPane confirmationAnchorPane;

    @FXML
    private Button proceedButton;

    @FXML
    private Label participantsNumberLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label luxeLevelLabel;

    @FXML
    private Label premiumLevelLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Label menuNameLabel;

//    @FXML
//    private Label menuAllergensLabel;

    @FXML
    private Button backButton;

    @FXML
    private Label dietTypeLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label menuLevelLabel;

    @FXML
    void clickedOnPay(ActionEvent event) {
        try {
            this.serviceRequestBean = sendServiceRequestController.fillServiceRequest(selectedMenuBean, reservationDetailsBean);
            serviceRequestBean.validate();  // è dummy -> Implementare dopo
            System.out.println("Mando la SendServiceRequest");
            sendServiceRequestController.sendServiceRequest(serviceRequestBean);
        } catch (Exception e) {
            errorLabel.setText("Error occurred while");
        }
        confirmationAnchorPane.setVisible(true);
        paymentAnchorPane.setDisable(true);
    }

    @FXML
    void clickedOnBack(ActionEvent event) {
        ServiceRequestPageControllerG2 serviceRequestPageControllerG2 = FxmlLoader2.setPageAndReturnController("ServiceRequestPage2");
        if(serviceRequestPageControllerG2 != null){
            serviceRequestPageControllerG2.initData(chefBean, selectedMenuBean, selectedMenuAllergenBeans);
        }
    }

    @FXML
    void clickedOnBackToHomepage(ActionEvent event) {
        FxmlLoader2.setPage("ClientHomePage2");
    }


    public void initData(ReservationDetailsBean reservationDetailsBean, MenuBean selectedMenuBean, List<AllergenBean> selectedMenuAllergenBeans, ChefBean chefBean) {
        this.reservationDetailsBean = reservationDetailsBean;
        this.selectedMenuBean = selectedMenuBean;
        this.selectedMenuAllergenBeans = selectedMenuAllergenBeans;
        this.chefBean = chefBean;

        menuNameLabel.setText(selectedMenuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(selectedMenuBean.getNumberOfCourses()));
        dietTypeLabel.setText(String.valueOf(selectedMenuBean.getDietType()).toLowerCase());
        pricePerPersonLabel.setText(String.valueOf(selectedMenuBean.getPricePerPerson()).toLowerCase());

        dateLabel.setText(reservationDetailsBean.getDate().toString());
        timeLabel.setText(reservationDetailsBean.getTime().toString());
        addressLabel.setText(reservationDetailsBean.getAddress());
        participantsNumberLabel.setText(String.valueOf(reservationDetailsBean.getParticipantNumber()));
        menuLevelLabel.setText(String.valueOf(reservationDetailsBean.getSelectedMenuLevel()).toLowerCase());

        totalPriceLabel.setText(sendServiceRequestController.calculateTotalPrice(reservationDetailsBean,selectedMenuBean) + " €");
    }

}
