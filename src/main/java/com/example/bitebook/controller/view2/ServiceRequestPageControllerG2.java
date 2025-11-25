package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
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

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    private ComboBox<LocalTime> timeComboBox;

    @FXML
    private TextField addressTextField;


    @FXML
    void clickedOnSendRequest(ActionEvent event) {

    }

    @FXML
    void clickedOnBack(ActionEvent event) {

    }


    @FXML
    void clickedOnCancel(ActionEvent event) {
        FxmlLoader2.setPage("ClientHomePage2");
    }

    @FXML
    void clickedOnProceed(ActionEvent event) {
        ignoreAllergenWarning = true;
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

    SendServiceRequestController sendServiceRequestControllerG =  new SendServiceRequestController();

    private ChefBean chefBean;
    private MenuBean selectedMenuBean;
    private Vector<AllergenBean> selectedMenuAllergenBeans;
    private ReservationDetailsBean reservationDetailsBean = new ReservationDetailsBean();
    private boolean ignoreAllergenWarning = false;




    public void initData(ChefBean chefBean, MenuBean selectedMenuBean, Vector<AllergenBean> selectedMenuAllergenBeans){
        this.chefBean = chefBean;
        this.selectedMenuBean = selectedMenuBean;
        this.selectedMenuAllergenBeans = selectedMenuAllergenBeans;

        fillMenuDetails();
        fillTimeComboBox();


        ToggleGroup levelToggleGroup = new ToggleGroup();
        baseLevelRadioButton.setToggleGroup(levelToggleGroup);
        premiumLevelRadioButton.setToggleGroup(levelToggleGroup);
        luxeLevelRadioButton.setToggleGroup(levelToggleGroup);
        baseLevelRadioButton.setSelected(true);
        selectedMenuBean = sendServiceRequestControllerG.getMenuLevelsSurcharge(selectedMenuBean);
        premiumLevelLabel.setText("+ "+selectedMenuBean.getPremiumLevelSurcharge()+" €");
        luxeLevelLabel.setText("+ "+selectedMenuBean.getLuxeLevelSurcharge()+" €");



        participantsNumberLabel.setText("1");
        reservationDetailsBean.setParticipantNumber(1);
        reservationDetailsBean.setSelectedMenuLevel(MenuLevel.BASE);

        participantsNumberSlider.setMin(1);
        participantsNumberSlider.setMax(10);
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
        totalPriceLabel.setText(String.valueOf(sendServiceRequestControllerG.calculateTotalPrice(reservationDetailsBean,selectedMenuBean)) + " €");
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

        // 2. Genera slot per la CENA
        LocalTime currentDinnerTime = dinnerStart;
        while (currentDinnerTime.isBefore(dinnerEnd) || currentDinnerTime.equals(dinnerEnd)) {
            timeSlots.add(currentDinnerTime);
            currentDinnerTime = currentDinnerTime.plus(30, ChronoUnit.MINUTES); // Aggiunge 30 minuti
        }

        return timeSlots;
    }



}
