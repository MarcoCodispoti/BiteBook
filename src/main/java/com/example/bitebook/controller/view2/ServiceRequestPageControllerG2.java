package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.SendServiceRequestController;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.MenuBean;
import com.example.bitebook.model.bean.ReservationDetailsBean;
import com.example.bitebook.model.enums.MenuLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

public class ServiceRequestPageControllerG2{

    SendServiceRequestController sendServiceRequestController =  new SendServiceRequestController();
    private ChefBean selectedChefBean;
    private MenuBean selectedMenuBean;
    private Vector<AllergenBean> selectedMenuAllergenBeans;
    private ReservationDetailsBean reservationDetailsBean = new ReservationDetailsBean();
    private boolean ignoreAllergenWarning = false;


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
    private ComboBox<LocalTime> timeComboBox;

    @FXML
    private TextField addressTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private AnchorPane serviceRequestAnchorPane;


    @FXML
    void clickedOnSendRequest(ActionEvent event) {
        if(!(isSelectedDate())){return;}
        LocalDate selectedDate = datePicker.getValue();
        if(!(isSelectedTime())){return;}
        LocalTime selectedTime = timeComboBox.getValue();
        if(!(isInsertedAddress())){return;}
        String selectedAddress = addressTextField.getText();

        reservationDetailsBean.setDate(selectedDate);
        reservationDetailsBean.setTime(selectedTime);
        reservationDetailsBean.setAddress(selectedAddress);

        for(AllergenBean allergyBean : selectedMenuAllergenBeans){
            System.out.println("Allergia trovata:" + allergyBean.getName());
        }


        if(sendServiceRequestController.clientAllergiesIncompatibility(selectedMenuAllergenBeans) && !ignoreAllergenWarning){
//            sendRequestButton.setDisable(true);
//            backButton.setDisable(true);
            serviceRequestAnchorPane.setDisable(true);
            allergyWarningAnchorPane.setVisible(true);
            allergyWarningAnchorPane.setDisable(false);
            return;
        }

        PaymentPageControllerG2  paymentPageControllerG2 = FxmlLoader2.setPageAndReturnController("PaymentPage2");
        if(paymentPageControllerG2 != null){
            paymentPageControllerG2.initData(reservationDetailsBean,selectedMenuBean, selectedMenuAllergenBeans, selectedChefBean);
        }

    }

    @FXML
    void clickedOnBack(ActionEvent event) {
        SelectMenuPageControllerG2 selectMenuPageControllerG2 = FxmlLoader2.setPageAndReturnController("SelectMenuPage2");
        if(selectMenuPageControllerG2 != null){
            selectMenuPageControllerG2.initData(selectedChefBean);
        }
    }


    @FXML
    void clickedOnCancel(ActionEvent event) {
        FxmlLoader2.setPage("ClientHomePage2");
    }

    @FXML
    void clickedOnProceed(ActionEvent event) {
//        sendRequestButton.setDisable(false);
//        backButton.setDisable(false);
        ignoreAllergenWarning = true;
        serviceRequestAnchorPane.setDisable(false);
        allergyWarningAnchorPane.setVisible(false);
        allergyWarningAnchorPane.setDisable(true);
        clickedOnSendRequest(event);
    }

    @FXML
    void clickedOnBase(ActionEvent event) {
        reservationDetailsBean.setSelectedMenuLevel(MenuLevel.BASE);
        updateTotalPrice();
    }

    @FXML
    void clickedOnPremium(ActionEvent event) {
        reservationDetailsBean.setSelectedMenuLevel(MenuLevel.PREMIUM);
        updateTotalPrice();
    }

    @FXML
    void clickedOnLuxe(ActionEvent event) {
        reservationDetailsBean.setSelectedMenuLevel(MenuLevel.LUXE);
        updateTotalPrice();
    }




    public void initData(ChefBean chefBean, MenuBean selectedMenuBean, Vector<AllergenBean> selectedMenuAllergenBeans){
        this.selectedChefBean = chefBean;
        this.selectedMenuBean = selectedMenuBean;
        this.selectedMenuAllergenBeans = selectedMenuAllergenBeans;

        fillMenuDetails();
        fillTimeComboBox();


        ToggleGroup levelToggleGroup = new ToggleGroup();
        baseLevelRadioButton.setToggleGroup(levelToggleGroup);
        premiumLevelRadioButton.setToggleGroup(levelToggleGroup);
        luxeLevelRadioButton.setToggleGroup(levelToggleGroup);
        baseLevelRadioButton.setSelected(true);
        selectedMenuBean = sendServiceRequestController.getMenuLevelsSurcharge(selectedMenuBean);
        premiumLevelLabel.setText("+ "+selectedMenuBean.getPremiumLevelSurcharge()+" €");
        luxeLevelLabel.setText("+ "+selectedMenuBean.getLuxeLevelSurcharge()+" €");

        participantsNumberLabel.setText("1");
        reservationDetailsBean.setParticipantNumber(1);
        reservationDetailsBean.setSelectedMenuLevel(MenuLevel.BASE);

        participantsNumberSlider.setMin(1);
        participantsNumberSlider.setMax(10);
        participantsNumberSlider.setBlockIncrement(1);
        participantsNumberSlider.setMajorTickUnit(1);
        participantsNumberSlider.setMinorTickCount(0);
        participantsNumberSlider.setSnapToTicks(true);
        participantsNumberSlider.setValue(1);
        participantsNumberLabel.setText("1");

        participantsNumberSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            participantsNumberLabel.setText(String.valueOf(newValue.intValue()));
            reservationDetailsBean.setParticipantNumber(newValue.intValue());
            // Chiama la funzione di aggiornamento del prezzo
        updateTotalPrice();
        });

        updateTotalPrice();
    }




    private void fillMenuDetails(){
        menuNameLabel.setText(selectedMenuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(selectedMenuBean.getNumberOfCourses()));
        dietTypeLabel.setText(String.valueOf(selectedMenuBean.getDietType()).toLowerCase());
        pricePerPersonLabel.setText(String.valueOf(selectedMenuBean.getPricePerPerson()) + " €");
        menuAllergensLabel.setText(getAllergensAsString(selectedMenuAllergenBeans));
    }


    public String getAllergensAsString(Vector<AllergenBean> allergenBeans){
        Vector<String> writteAllergenNames = new Vector<>();
        Vector<AllergenBean> menuAllergenBeans = new Vector<>();
        int index = 0;
        String allergensAsString = "";
        for(AllergenBean allergenBean:allergenBeans){
            if(index == 0){
                if(!isAlredyPresent(writteAllergenNames,allergenBean.getName())){
                    allergensAsString = allergensAsString.concat(allergenBean.getName());
                    menuAllergenBeans.add(allergenBean);

                    index++;
                    writteAllergenNames.add(allergenBean.getName());
                }
            } else {
                if(!isAlredyPresent(writteAllergenNames,allergenBean.getName())){
                    allergensAsString = allergensAsString.concat(", ").concat(allergenBean.getName());
                    menuAllergenBeans.add(allergenBean);

                    index++;
                    writteAllergenNames.add(allergenBean.getName());
                }
            }
        }
        this.selectedMenuAllergenBeans = menuAllergenBeans;
        System.out.println("menuAllergenBeans.size(): " + menuAllergenBeans.size());
        return allergensAsString;
    }

    public boolean isAlredyPresent(Vector<String> writtenAllergenNames, String actualName){
        for(String writtenAllergenName:writtenAllergenNames){
            if(writtenAllergenName.equals(actualName)){
                return true;
            }
        }
        return false;
    }


    public void updateTotalPrice(){
        totalPriceLabel.setText(String.valueOf(sendServiceRequestController.calculateTotalPrice(reservationDetailsBean,selectedMenuBean)) + " €");
    }


    private void fillTimeComboBox(){
        timeComboBox.getItems().clear();
        timeComboBox.getItems().addAll(generateTimeSlots());
    };

    public Vector<LocalTime> generateTimeSlots(){
        Vector<LocalTime> timeSlots = new Vector<>();

        // --- Intervallo Pranzo ---
        LocalTime lunchStart = LocalTime.of(12, 0); // 12:00
        LocalTime lunchEnd = LocalTime.of(14, 0);   // 14:00

        // --- Intervallo Cena ---
        LocalTime dinnerStart = LocalTime.of(18, 0); // 18:00
        LocalTime dinnerEnd = LocalTime.of(23, 0);   // 23:00

        // 1. Genera slot per il PRANZO
        LocalTime currentLunchTime = lunchStart;
        while (currentLunchTime.isBefore(lunchEnd) || currentLunchTime.equals(lunchEnd)) {
            timeSlots.add(currentLunchTime);
            currentLunchTime = currentLunchTime.plus(30, ChronoUnit.MINUTES); // Aggiunge 30 minuti
        }
        LocalTime currentDinnerTime = dinnerStart;
        while (currentDinnerTime.isBefore(dinnerEnd) || currentDinnerTime.equals(dinnerEnd)) {
            timeSlots.add(currentDinnerTime);
            currentDinnerTime = currentDinnerTime.plus(30, ChronoUnit.MINUTES); // Aggiunge 30 minuti
        }

        return timeSlots;
    }

    public boolean isSelectedDate(){
        LocalDate requestDate = datePicker.getValue();
        if (requestDate == null) {
            errorLabel.setText("Please Select a Date");
            return false;
        } else if (requestDate.isBefore(LocalDate.now()) || requestDate.equals(LocalDate.now())){
            errorLabel.setText("Select a date after today");
            return false;
        } else{
            return true;
        }
    }

    public boolean isSelectedTime(){
        LocalTime selectedTime = timeComboBox.getValue();
        if(selectedTime == null){
            errorLabel.setText("Please Select a Time"); return false;
        }
        return true;
    }

    public boolean isInsertedAddress(){
        String address = addressTextField.getText();
        if(address == null || address.isEmpty()){
            errorLabel.setText("Please Enter Address");
            return false;
        }
        if(address.length() < 10 ){
            errorLabel.setText("Please insert a valid address");
            return false;
        }
        return true;
    }

}
