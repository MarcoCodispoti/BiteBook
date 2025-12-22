package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ServiceRequestController;
import com.example.bitebook.model.bean.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentPageControllerG2 {


    private static final Logger logger = Logger.getLogger(PaymentPageControllerG2.class.getName());


    @FXML
    private AnchorPane paymentAnchorPane;
    @FXML
    private AnchorPane confirmationAnchorPane;
    @FXML
    private Label menuNameLabel;
    @FXML
    private Label numberOfCoursesLabel;
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
    private Label participantsNumberLabel;
    @FXML
    private Label menuLevelLabel;
    @FXML
    private Label messageLabel;


    private final ServiceRequestController serviceRequestController = new ServiceRequestController();
    private ChefBean chefBean;
    private MenuBean selectedMenuBean;
    private ReservationDetailsBean reservationDetailsBean;
    private List<AllergenBean> selectedMenuAllergenBeans;


    @FXML
    void handlePay(){
        messageLabel.setVisible(false);

        try {
            ServiceRequestBean serviceRequestBean = serviceRequestController.createServiceRequest(selectedMenuBean, reservationDetailsBean);

            if (serviceRequestBean != null) {
                serviceRequestBean.validate();
                serviceRequestController.sendServiceRequest(serviceRequestBean);

                confirmationAnchorPane.setVisible(true);
                paymentAnchorPane.setDisable(true);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending the request" , e );
            messageLabel.setText("Error occurred while processing request");
            messageLabel.setVisible(true);
        }
    }



    @FXML
    void handleBackToHomepage(){
        FxmlLoader2.setPage("ClientHomePage2");
    }



    @FXML
    void handleBack(){
        ServiceRequestPageControllerG2 serviceRequestPageControllerG2 = FxmlLoader2.setPageAndReturnController("ServiceRequestPage2");
        if(serviceRequestPageControllerG2 != null){
            serviceRequestPageControllerG2.initData(chefBean, selectedMenuBean, selectedMenuAllergenBeans);
        }
    }



    @FXML
    public void initialize() {
        confirmationAnchorPane.setVisible(false);
        messageLabel.setText("");
        messageLabel.setVisible(false);
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

        double total = serviceRequestController.calculateTotalPrice(reservationDetailsBean, selectedMenuBean);
        totalPriceLabel.setText(total + " €");
    }


}
