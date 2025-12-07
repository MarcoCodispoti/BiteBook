package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ApprovedRequestCardControllerG{
    private ChefHomePageControllerG parentController;
    private ServiceRequestBean approvedRequestRequestBean;

    @FXML
    private Label dateLabel;

    @FXML
    private Label numberOfParticipantsLabel;

    @FXML
    private Label requestIdLabel;

    @FXML
    private Label clientLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label menuLabel;

    @FXML
    private Label menuLevelLabel;


    public void initData(ServiceRequestBean serviceRequestBean){
        this.approvedRequestRequestBean = serviceRequestBean;
        requestIdLabel.setText(String.valueOf(serviceRequestBean.getId()));
        menuLabel.setText(serviceRequestBean.getMenuBean().getName());
        menuLevelLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getSelectedMenuLevel()));
        numberOfParticipantsLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getParticipantNumber()));
        totalPriceLabel.setText(serviceRequestBean.getTotalPrice() + " €");
        dateLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getDate()));
        timeLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getTime()));
        addressLabel.setText(serviceRequestBean.getReservationDetailsBean().getAddress());
        clientLabel.setText(serviceRequestBean.getClientBean().getName() + " " + serviceRequestBean.getClientBean().getSurname());
    }

    public void setParentController(ChefHomePageControllerG parentController){this.parentController = parentController;}


}
