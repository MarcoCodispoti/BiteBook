package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.SendServiceRequestController;
import com.example.bitebook.model.bean.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class PaymentPageControllerG2 {


    private final SendServiceRequestController sendServiceRequestController = new SendServiceRequestController();

    private ChefBean chefBean;
    private MenuBean selectedMenuBean;
    private ReservationDetailsBean reservationDetailsBean;
    private List<AllergenBean> selectedMenuAllergenBeans;


    @FXML private AnchorPane paymentAnchorPane;
    @FXML private AnchorPane confirmationAnchorPane;

    @FXML private Label menuNameLabel;
    @FXML private Label numberOfCoursesLabel;
    @FXML private Label dietTypeLabel;
    @FXML private Label pricePerPersonLabel;
    @FXML private Label totalPriceLabel;

    @FXML private Label dateLabel;
    @FXML private Label timeLabel;
    @FXML private Label addressLabel;
    @FXML private Label participantsNumberLabel;
    @FXML private Label menuLevelLabel;

    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        confirmationAnchorPane.setVisible(false);
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    public void initData(ReservationDetailsBean reservationDetailsBean, MenuBean selectedMenuBean, List<AllergenBean> selectedMenuAllergenBeans, ChefBean chefBean) {
        this.reservationDetailsBean = reservationDetailsBean;
        this.selectedMenuBean = selectedMenuBean;
        this.selectedMenuAllergenBeans = selectedMenuAllergenBeans;
        this.chefBean = chefBean;

        menuNameLabel.setText(selectedMenuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(selectedMenuBean.getNumberOfCourses()));

        if (selectedMenuBean.getDietType() != null) {
            dietTypeLabel.setText(selectedMenuBean.getDietType().toString().toLowerCase());
        }

        pricePerPersonLabel.setText(selectedMenuBean.getPricePerPerson() + " €");

        dateLabel.setText(reservationDetailsBean.getDate().toString());
        timeLabel.setText(reservationDetailsBean.getTime().toString());
        addressLabel.setText(reservationDetailsBean.getAddress());
        participantsNumberLabel.setText(String.valueOf(reservationDetailsBean.getParticipantNumber()));

        if (reservationDetailsBean.getSelectedMenuLevel() != null) {
            menuLevelLabel.setText(reservationDetailsBean.getSelectedMenuLevel().toString().toLowerCase());
        }

        double total = sendServiceRequestController.calculateTotalPrice(reservationDetailsBean, selectedMenuBean);
        totalPriceLabel.setText(total + " €");
    }

    @FXML
    void clickedOnPay(){
        errorLabel.setVisible(false);

        try {
            ServiceRequestBean serviceRequestBean = sendServiceRequestController.fillServiceRequest(selectedMenuBean, reservationDetailsBean);

            if (serviceRequestBean != null) {
                serviceRequestBean.validate();
                sendServiceRequestController.sendServiceRequest(serviceRequestBean);

                confirmationAnchorPane.setVisible(true);
                paymentAnchorPane.setDisable(true);
            }
        } catch (Exception e) {
            errorLabel.setText("Error occurred while processing payment");
            errorLabel.setVisible(true);
        }
    }


    @FXML
    void clickedOnBackToHomepage(){
        FxmlLoader2.setPage("ClientHomePage2");
    }

    @FXML
    void clickedOnBack(){
        ServiceRequestPageControllerG2 serviceRequestPageControllerG2 = FxmlLoader2.setPageAndReturnController("ServiceRequestPage2");
        if(serviceRequestPageControllerG2 != null){
            serviceRequestPageControllerG2.initData(chefBean, selectedMenuBean, selectedMenuAllergenBeans);
        }
    }

}
