package com.example.bitebook.controller.view2;

import com.example.bitebook.model.bean.AllergenBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

import java.util.Vector;

public class ServiceRequestPageControllerG2{


    @FXML
    private Button sendRequestButton;

    @FXML
    private Label numberOfCoursesLabel;

    @FXML
    private Slider participantsNumberSlider;

    @FXML
    private RadioButton premiumLevelRadioButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private RadioButton baseLevelRadioButton;

    @FXML
    private AnchorPane allergyWarningAnchorPane;

    @FXML
    private Button proceedButton;

    @FXML
    private Label participantsNumberLabel;

    @FXML
    private Label luxeLevelLabel;

    @FXML
    private Label premiumLevelLabel;

    @FXML
    private RadioButton luxeLevelRadioButton;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Label menuNameLabel;

    @FXML
    private Label menuAllergensLabel;

    @FXML
    private Button backButton;

    @FXML
    private Label dietTypeLabel;

    @FXML
    private Label errorLabel;

    @FXML
    void clickedOnSendRequest(ActionEvent event) {

    }

    @FXML
    void clickedOnBack(ActionEvent event) {

    }


    @FXML
    void clickedOnCancel(ActionEvent event) {

    }

    @FXML
    void clickedOnProceed(ActionEvent event) {

    }

    @FXML
    void clickedOnBase(ActionEvent event) {

    }

    @FXML
    void clickedOnPremium(ActionEvent event) {

    }

    @FXML
    void clickedOnLuxe(ActionEvent event) {

    }





    private Vector<AllergenBean> selectedMenuAllergenBeans = new Vector<>();


}
