package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.ServiceRequestBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class ChefRequestCardControllerG{


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
    private Label statusLabel;
    @FXML
    private Label menuLabel;
    @FXML
    private Label menuLevelLabel;


    private ChefRequestsPageControllerG parentController;
    private Parent cardUi;
    private ServiceRequestBean cardServiceRequestBean;



    @FXML
    private void handleClick(){
        if(parentController!=null){
            parentController.setSelectedRequest(cardServiceRequestBean,cardUi);
        }
    }



    public void initData(ServiceRequestBean serviceRequestBean){
        this.cardServiceRequestBean = serviceRequestBean;
        requestIdLabel.setText(String.valueOf(serviceRequestBean.getId()));
        clientLabel.setText(serviceRequestBean.getClientBean().getName() + " " + serviceRequestBean.getClientBean().getSurname());
        menuLabel.setText(serviceRequestBean.getMenuBean().getName());
        menuLevelLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getSelectedMenuLevel()));
        numberOfParticipantsLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getParticipantNumber()));
        totalPriceLabel.setText(serviceRequestBean.getTotalPrice() + " €");
        dateLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getDate()));
        timeLabel.setText(String.valueOf(serviceRequestBean.getReservationDetailsBean().getTime()));
        addressLabel.setText(serviceRequestBean.getReservationDetailsBean().getAddress());
        statusLabel.setText(String.valueOf(serviceRequestBean.getStatus()));
    }



    public void setParentController(ChefRequestsPageControllerG parentController){
        this.parentController=parentController;
    }



    public void setCardUi(Parent cardUi){this.cardUi=cardUi;}


}
