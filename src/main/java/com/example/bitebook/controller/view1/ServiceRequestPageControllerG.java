package com.example.bitebook.controller.view1;

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
import javafx.scene.text.TextFlow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class ServiceRequestPageControllerG{
    private boolean ignoreAllergenWarning = false;


    SendServiceRequestController sendServiceRequestController =  new SendServiceRequestController();

    private ChefBean menuChefBean;
    MenuBean selectedMenuBean;
    List<AllergenBean> menuAllergenBeans;
    ReservationDetailsBean reservationDetailsBean =  new ReservationDetailsBean();
    private int totalPrice;

    @FXML
    private Button sendRequestButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private TextFlow nameTextFlow;

    @FXML
    private TextFlow numberOfCoursesTextFlow;

    @FXML
    private Label nameLabel;

    @FXML
    private Label numberOfCoursesLabel;

    @FXML
    private Label allergensLabel;

    @FXML
    private Label dietTypeLabel;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private DatePicker serviceDatePicker;

    @FXML
    private ComboBox<LocalTime> timeComboBox;

    @FXML
    private TextField addressTextField;

    @FXML
    private ComboBox<String> numberOfParticipantsComboBox;

    @FXML
    private ComboBox<String> ingredientsLevelComboBox;

    @FXML
    private AnchorPane allergenWarningAnchorPane;

    @FXML
    private Button proocedAnywayButton;

    @FXML
    private Button cancelRequestButton;

    @FXML
    private Button backButton;


    @FXML
    void clickedOnCancelRequest(ActionEvent event){
        FxmlLoader.setPage("ClientHomePage");
    }

    @FXML
    void clickedOnProceedAnyway(ActionEvent event){
        ignoreAllergenWarning = true;
        backButton.setDisable(false);
        sendRequestButton.setDisable(false);
        allergenWarningAnchorPane.setVisible(false);
        clickedOnSendRequest(event);
    }

    @FXML
    void clickedOnSendRequest(ActionEvent event){
        if(!isValidDate()){return;}
        LocalDate selectedDate = serviceDatePicker.getValue();
        if(!checkTimeIsSelected()){return;}
        LocalTime selectedTime = timeComboBox.getValue();
        if(!checkInsertedAddress()){return;}
        String selectedAddress = addressTextField.getText();
        if(!checkParticipantsNumberIsSelected()){return;}
        int numberOfParticipants = Integer.parseInt(numberOfParticipantsComboBox.getValue());
        if(!checkIngredientsLevelIsSelected()){return;}

        reservationDetailsBean.setDate(selectedDate);
        reservationDetailsBean.setTime(selectedTime);
        reservationDetailsBean.setAddress(selectedAddress);
        reservationDetailsBean.setParticipantNumber(numberOfParticipants);

        SendServiceRequestController sendServiceRequestController = new SendServiceRequestController();
        if(sendServiceRequestController.clientAllergiesIncompatibility(menuAllergenBeans) && !ignoreAllergenWarning){
            backButton.setDisable(true);
            sendRequestButton.setDisable(true);
            allergenWarningAnchorPane.setVisible(true);
            return;
        }

        PaymentPageControllerG paymentPageControllerG = FxmlLoader.setPageAndReturnController("PaymentPage");
        if(paymentPageControllerG != null){
            paymentPageControllerG.initData(selectedMenuBean,reservationDetailsBean, menuAllergenBeans,menuChefBean);
        }
    }

    @FXML
    void clickedOnBack(ActionEvent event){
        MenuDetailsPageControllerG menuDetailsPageControllerG = FxmlLoader.setPageAndReturnController("MenuDetailsPage");
        menuDetailsPageControllerG.initData(selectedMenuBean,menuChefBean);
    }




    public void initData(MenuBean selectedMenuBean,List<AllergenBean> menuAllergenBeans, ChefBean menuChefBean){
        this.menuChefBean = menuChefBean;
        this.selectedMenuBean = selectedMenuBean;
        this.menuAllergenBeans = menuAllergenBeans;
        nameLabel.setText(selectedMenuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(selectedMenuBean.getNumberOfCourses()));
        dietTypeLabel.setText(selectedMenuBean.getDietType().toString().toLowerCase());
        pricePerPersonLabel.setText(selectedMenuBean.getPricePerPerson() + " €");
        allergensLabel.setText(getAllergensAsString());


        fillTimeComboBox();
        fillNumberOfParticipantsComboBox();
        fillIngredientsLevelComboBox();
    }


    public void initialize(){
        ignoreAllergenWarning = false;
        allergenWarningAnchorPane.setVisible(false);
        numberOfParticipantsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateTotalPrice() );
        ingredientsLevelComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateTotalPrice());
    }

    public void updateTotalPrice(){

        String participantsStr = numberOfParticipantsComboBox.getValue();
        String levelStr = ingredientsLevelComboBox.getValue();


        if (participantsStr == null || levelStr == null || selectedMenuBean == null) {
            totalPriceLabel.setText("--");
            return;
        }

        try {
            int participants = Integer.parseInt(participantsStr);
            MenuLevel level = extractMenuLevel();

            reservationDetailsBean.setParticipantNumber(participants);
            reservationDetailsBean.setSelectedMenuLevel(level);


            if (participants > 0 && level != null) {
                calculateTotalPrice();
                totalPriceLabel.setText(String.valueOf(totalPrice));
            } else {
                totalPriceLabel.setText("--");
            }

        } catch (NumberFormatException e) {
            totalPriceLabel.setText("Error");
        }
    }



    private String getAllergensAsString(){
        String allergensString = "";
        int index = 0;
        for(AllergenBean allergenBean : menuAllergenBeans){
            if(index == 0){
                allergensString = allergensString.concat(allergenBean.getName());
            } else{
                allergensString = allergensString.concat(", ").concat(allergenBean.getName());
            }
            index++;
        }
        return allergensString;
    }



    private void fillNumberOfParticipantsComboBox(){
        for(int i = 1; i <= 10; i++){
            numberOfParticipantsComboBox.getItems().add(String.valueOf(i));
        }
    }

    private void fillIngredientsLevelComboBox(){
        selectedMenuBean = sendServiceRequestController.getMenuLevelsSurcharge(selectedMenuBean);
        for(int i = 1; i <= 3; i++){
            switch(i){
                case 1: ingredientsLevelComboBox.getItems().add("1: Base        Surcharge: +0 €"); break;
                case 2: ingredientsLevelComboBox.getItems().add("2: Premium     Surcharge: +" + selectedMenuBean.getPremiumLevelSurcharge() + " €"); break;
                case 3: ingredientsLevelComboBox.getItems().add("3: Luxe        Surcharge: +" + selectedMenuBean.getLuxeLevelSurcharge() + " €"); break;
            }
        }
    }

    private void fillTimeComboBox(){
        timeComboBox.getItems().clear();
        timeComboBox.getItems().addAll(generateTimeSlots());
    };

    public List<LocalTime> generateTimeSlots(){
        List<LocalTime> timeSlots = new ArrayList<>();


        LocalTime lunchStart = LocalTime.of(12, 0); // 12:00
        LocalTime lunchEnd = LocalTime.of(14, 0);   // 14:00

        LocalTime dinnerStart = LocalTime.of(18, 0); // 18:00
        LocalTime dinnerEnd = LocalTime.of(23, 0);   // 23:00

        LocalTime currentLunchTime = lunchStart;
        while (currentLunchTime.isBefore(lunchEnd) || currentLunchTime.equals(lunchEnd)) {
            timeSlots.add(currentLunchTime);
            currentLunchTime = currentLunchTime.plusMinutes(30); // Aggiunge 30 minuti
        }

        LocalTime currentDinnerTime = dinnerStart;
        while (currentDinnerTime.isBefore(dinnerEnd) || currentDinnerTime.equals(dinnerEnd)) {
            timeSlots.add(currentDinnerTime);
            currentDinnerTime = currentDinnerTime.plusMinutes(30); // Aggiunge 30 minuti
        }

        return timeSlots;
    }


    private boolean isValidDate(){
        LocalDate serviceDate = serviceDatePicker.getValue();
        if(serviceDate == null){
            errorLabel.setVisible(true);  errorLabel.setText("Please select a date first");
            return false;
        } else if(serviceDate.isBefore(LocalDate.now()) || serviceDate.isEqual(LocalDate.now())){
            errorLabel.setText("Select a date after today");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTimeIsSelected(){
        LocalTime selectedTime = timeComboBox.getValue();
        if(selectedTime == null){
            errorLabel.setVisible(true);  errorLabel.setText("Please select the time for your request");
            return false;
        }
        return true;
    }

    private boolean checkInsertedAddress(){
        if(addressTextField.getText().isEmpty() || addressTextField.getText().length() < 10){
            errorLabel.setText("Please enter a valid address");
            return false;
        }
        return true;
    }


    private boolean checkParticipantsNumberIsSelected(){
        if(numberOfParticipantsComboBox.getValue() == null){
            errorLabel.setText("Please select the number of participants");
            return false;
        }
        return true;
    }

    private boolean checkIngredientsLevelIsSelected(){
        MenuLevel selectedMenuLevel = extractMenuLevel();
        if(selectedMenuLevel != MenuLevel.BASE && selectedMenuLevel != MenuLevel.PREMIUM && selectedMenuLevel != MenuLevel.LUXE){return false;}
        reservationDetailsBean.setSelectedMenuLevel(selectedMenuLevel);
        return true;
    }

    private MenuLevel extractMenuLevel(){
        String menuLevelString = ingredientsLevelComboBox.getValue();
        MenuLevel menuLevel;
        if(menuLevelString == null || menuLevelString.isEmpty()){
            errorLabel.setVisible(true);  errorLabel.setText("Please select a valid menu level");
            return null;
        }

        if(menuLevelString.contains("Base")){
            menuLevel = MenuLevel.BASE;
        } else if(menuLevelString.contains("Premium")){
            menuLevel = MenuLevel.PREMIUM;
        } else if(menuLevelString.contains("Luxe")){
            menuLevel = MenuLevel.LUXE;
        }else{
            errorLabel.setText("Please select a valid menu level");
            return null;
        }
        return menuLevel;
    }

    private void calculateTotalPrice(){
        totalPrice = -1;
        totalPrice = sendServiceRequestController.calculateTotalPrice(reservationDetailsBean,selectedMenuBean);
    }

}
