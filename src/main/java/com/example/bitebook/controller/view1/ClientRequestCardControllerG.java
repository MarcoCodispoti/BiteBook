package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class ClientRequestCardControllerG{
    private ClientRequestsPageControllerG parentController;
    private Parent cardUi;

    private ServiceRequestBean serviceRequestBean;

    @FXML
    private Label dateLabel;

    @FXML
    private Label numberOfParticipantsLabel;

    @FXML
    private Label requestIdLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label chefLabel;

    @FXML
    private Label menuLabel;

    @FXML
    private Label menuLevelLabel;

    public void initData(ServiceRequestBean serviceRequestBean){
        this.serviceRequestBean = serviceRequestBean;
        requestIdLabel.setText(String.valueOf(serviceRequestBean.getId()));
        chefLabel.setText(serviceRequestBean.getChefBean().getName() + " " + serviceRequestBean.getChefBean().getSurname());
        menuLabel.setText(serviceRequestBean.getMenuBean().getName());
        menuLevelLabel.setText(String.valueOf(serviceRequestBean.getReservationDetails().getSelectedMenuLevel()));
        numberOfParticipantsLabel.setText(String.valueOf(serviceRequestBean.getReservationDetails().getParticipantNumber()));
        totalPriceLabel.setText(serviceRequestBean.getTotalPrice() + " €");
        dateLabel.setText(String.valueOf(serviceRequestBean.getReservationDetails().getDate()));
        timeLabel.setText(String.valueOf(serviceRequestBean.getReservationDetails().getTime()));
        addressLabel.setText(serviceRequestBean.getReservationDetails().getAddress());
        statusLabel.setText(String.valueOf(serviceRequestBean.getStatus()));
    }



    public void setParentController(ClientRequestsPageControllerG parentController){this.parentController = parentController;}

    // public void setCardUi(Parent cardUi){this.cardUi = cardUi;}
}
