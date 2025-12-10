package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.SendServiceRequestController;
import com.example.bitebook.model.bean.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentPageControllerG {

    private static final Logger logger = Logger.getLogger(PaymentPageControllerG.class.getName());

    private final SendServiceRequestController sendServiceRequestController = new SendServiceRequestController();
    private MenuBean requestMenuBean;
    private ReservationDetailsBean requestReservationDetailsBean;
    private List<AllergenBean> reservationAllergenBeans;
    private ChefBean reservationChefBean;

    @FXML private Label errorLabel;

    @FXML private Label nameLabel;

    @FXML private Label numberOfCoursesLabel;

    @FXML private Label dietTypeLabel;

    @FXML private Label pricePerPersonLabel;

    @FXML private Label totalPriceLabel;

    @FXML private Label dateLabel;

    @FXML private Label timeLabel;

    @FXML private Label addressLabel;

    @FXML private Label numberOfParticipantsLabel;

    @FXML private Label ingredientsLevelLabel;


    @FXML
    void clickedOnSendRequest(){
        errorLabel.setText("");
        try {
            ServiceRequestBean serviceRequestBean = sendServiceRequestController.fillServiceRequest(
                    requestMenuBean,
                    requestReservationDetailsBean
            );
            if (serviceRequestBean != null) {
                serviceRequestBean.validate();
            }
            sendServiceRequestController.sendServiceRequest(serviceRequestBean);
            FxmlLoader.setPage("ConfirmationPage");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending request!", e);
            errorLabel.setText("Error completing the request: ");
        }
    }


    @FXML
    void clickedOnBack() {
        ServiceRequestPageControllerG controller = FxmlLoader.setPageAndReturnController("ServiceRequestPage");
        if (controller != null) {
            controller.initData(requestMenuBean, reservationAllergenBeans, reservationChefBean);
        }
    }

    public void initData(MenuBean menuBean, ReservationDetailsBean reservationDetailsBean, List<AllergenBean> menuAllergenBeans, ChefBean menuChefBean) {
        this.requestMenuBean = menuBean;
        this.requestReservationDetailsBean = reservationDetailsBean;
        this.reservationAllergenBeans = menuAllergenBeans;
        this.reservationChefBean = menuChefBean;

        nameLabel.setText(menuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(menuBean.getNumberOfCourses()));

        if (menuBean.getDietType() != null) {
            dietTypeLabel.setText(menuBean.getDietType().toString().toLowerCase());
        }

        pricePerPersonLabel.setText(menuBean.getPricePerPerson() + " €");

        if (reservationDetailsBean.getDate() != null) {
            dateLabel.setText(reservationDetailsBean.getDate().toString());
        }

        if (reservationDetailsBean.getTime() != null) {
            timeLabel.setText(reservationDetailsBean.getTime().toString());
        }

        addressLabel.setText(reservationDetailsBean.getAddress());
        numberOfParticipantsLabel.setText(String.valueOf(reservationDetailsBean.getParticipantNumber()));

        if (reservationDetailsBean.getSelectedMenuLevel() != null) {
            ingredientsLevelLabel.setText(reservationDetailsBean.getSelectedMenuLevel().toString().toLowerCase());
        }

        double totalPrice = sendServiceRequestController.calculateTotalPrice(reservationDetailsBean, menuBean);
        totalPriceLabel.setText(String.valueOf(totalPrice));
    }





}
