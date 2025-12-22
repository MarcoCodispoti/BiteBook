package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ServiceRequestController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.MenuBean;
import com.example.bitebook.model.bean.ReservationDetailsBean;
import com.example.bitebook.model.enums.MenuLevel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServiceRequestPageControllerG2{


    private static final Logger logger = Logger.getLogger(ServiceRequestPageControllerG2.class.getName());


    private final ServiceRequestController serviceRequestController = new ServiceRequestController();
    private final ReservationDetailsBean reservationDetailsBean = new ReservationDetailsBean();
    private ChefBean selectedChefBean;
    private MenuBean selectedMenuBean;
    private List<AllergenBean> selectedMenuAllergenBeans;
    private boolean ignoreAllergenWarning = false;


    @FXML private AnchorPane serviceRequestAnchorPane;
    @FXML private AnchorPane allergyWarningAnchorPane;
    @FXML private Label menuNameLabel;
    @FXML private Label numberOfCoursesLabel;
    @FXML private Label dietTypeLabel;
    @FXML private Label pricePerPersonLabel;
    @FXML private Label menuAllergensLabel;
    @FXML private Label premiumLevelLabel;
    @FXML private Label luxeLevelLabel;
    @FXML private Label totalPriceLabel;
    @FXML private Label messageLabel;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LocalTime> timeComboBox;
    @FXML private TextField addressTextField;
    @FXML private Slider participantsNumberSlider;
    @FXML private Label participantsNumberLabel;
    @FXML private RadioButton baseLevelRadioButton;
    @FXML private RadioButton premiumLevelRadioButton;
    @FXML private RadioButton luxeLevelRadioButton;
    @FXML private ToggleGroup levelToggleGroup;


    @FXML
    void handleSendRequest() {
        messageLabel.setVisible(false);

        if (!validateAndBindData()) {
            return;
        }

        boolean hasIncompatibility;
        try{
            hasIncompatibility = serviceRequestController.hasAllergyConflict(selectedMenuAllergenBeans);
        } catch (IllegalStateException e){
            logger.log(Level.SEVERE, "Safety Warning: Error while confronting ", e);
            displayMessage("System Error: Unable to check allergies incompatibility");
            return;
        }

        if (hasIncompatibility && !ignoreAllergenWarning) {
            toggleWarningPane(true);
            return;
        }
        PaymentPageControllerG2 controller = FxmlLoader2.setPageAndReturnController("PaymentPage2");
        if (controller != null) {
            controller.initData(reservationDetailsBean, selectedMenuBean, selectedMenuAllergenBeans, selectedChefBean);
        }
    }



    @FXML
    void handleProceed() {
        ignoreAllergenWarning = true;
        toggleWarningPane(false);
        handleSendRequest();
    }



    @FXML
    void handleBack() {
        SelectMenuPageControllerG2 controller = FxmlLoader2.setPageAndReturnController("SelectMenuPage2");
        if (controller != null) {
            controller.initData(selectedChefBean);
        }
    }



    @FXML
    void handleCancel() {
        FxmlLoader2.setPage("ClientHomePage2");
    }



    @FXML
    public void initialize(){
        setupMenuLevelToggles();
        setupParticipantsSlider();
        allergyWarningAnchorPane.setVisible(false);
        messageLabel.setVisible(false);
    }



    public void initData(ChefBean chefBean, MenuBean selectedMenuBean, List<AllergenBean> selectedMenuAllergenBeans) {
        this.selectedChefBean = chefBean;
        this.selectedMenuBean = selectedMenuBean;
        this.selectedMenuAllergenBeans = selectedMenuAllergenBeans;

        loadMenuSurcharges();

        fillMenuDetails();
        fillTimeComboBox();

        reservationDetailsBean.setParticipantNumber(1);
        reservationDetailsBean.setSelectedMenuLevel(MenuLevel.BASE);
        levelToggleGroup.selectToggle(baseLevelRadioButton);

        updateTotalPrice();
    }



    private void setupMenuLevelToggles() {
        levelToggleGroup = new ToggleGroup();
        baseLevelRadioButton.setToggleGroup(levelToggleGroup);
        premiumLevelRadioButton.setToggleGroup(levelToggleGroup);
        luxeLevelRadioButton.setToggleGroup(levelToggleGroup);

        baseLevelRadioButton.setUserData(MenuLevel.BASE);
        premiumLevelRadioButton.setUserData(MenuLevel.PREMIUM);
        luxeLevelRadioButton.setUserData(MenuLevel.LUXE);

        levelToggleGroup.selectedToggleProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                MenuLevel selectedLevel = (MenuLevel) newVal.getUserData();
                reservationDetailsBean.setSelectedMenuLevel(selectedLevel);
                updateTotalPrice();
            }
        });
    }



    private void setupParticipantsSlider() {
        participantsNumberSlider.setMin(1);
        participantsNumberSlider.setMax(10);
        participantsNumberSlider.setValue(1);
        participantsNumberLabel.setText("1");

        participantsNumberSlider.valueProperty().addListener((_, _, newVal) -> {
            int participants = newVal.intValue();
            participantsNumberLabel.setText(String.valueOf(participants));
            reservationDetailsBean.setParticipantNumber(participants);
            updateTotalPrice();
        });
    }



    private void loadMenuSurcharges() {
        try {
            this.selectedMenuBean = serviceRequestController.populateMenuSurcharges(selectedMenuBean);
            premiumLevelLabel.setText("+ " + selectedMenuBean.getPremiumLevelSurcharge() + " €");
            luxeLevelLabel.setText("+ " + selectedMenuBean.getLuxeLevelSurcharge() + " €");
        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Error while loading menu level surcharges" , e);
            displayMessage("Warning: Unable to load levels surcharges");
        }
    }



    private void fillMenuDetails() {
        menuNameLabel.setText(selectedMenuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(selectedMenuBean.getNumberOfCourses()));
        if (selectedMenuBean.getDietType() != null) {
            dietTypeLabel.setText(selectedMenuBean.getDietType().toString().toLowerCase());
        }
        pricePerPersonLabel.setText(selectedMenuBean.getPricePerPerson() + " €");

        menuAllergensLabel.setText(formatAllergensList(selectedMenuAllergenBeans));
    }



    private boolean validateAndBindData() {
        LocalDate date = datePicker.getValue();
        if (date == null || !date.isAfter(LocalDate.now())) {
            displayMessage("Please select a valid date after today.");
            return false;
        }

        LocalTime time = timeComboBox.getValue();
        if (time == null) {
            displayMessage("Please select a time.");
            return false;
        }

        String address = addressTextField.getText();
        if (address == null || address.trim().length() < 5) {
            displayMessage("Please enter a valid address.");
            return false;
        }
        reservationDetailsBean.setDate(date);
        reservationDetailsBean.setTime(time);
        reservationDetailsBean.setAddress(address.trim());
        return true;
    }



    private void updateTotalPrice() {
        if (selectedMenuBean != null) {
            int price = serviceRequestController.calculateTotalPrice(reservationDetailsBean, selectedMenuBean);
            totalPriceLabel.setText(price + " €");
        }
    }



    private String formatAllergensList(List<AllergenBean> allergens) {
        if (allergens == null || allergens.isEmpty()) return "None";
        return allergens.stream()
                .map(AllergenBean::getName)
                .distinct()
                .collect(Collectors.joining(", "));
    }



    private void fillTimeComboBox() {
        timeComboBox.getItems().clear();
        timeComboBox.getItems().addAll(generateTimeSlots());
    }



    private List<LocalTime> generateTimeSlots() {
        List<LocalTime> slots = new ArrayList<>();
        // Lunch 12:00 - 14:00
        addTimeSlots(slots, LocalTime.of(12, 0), LocalTime.of(14, 0));
        // Dinner 18:00 - 23:00
        addTimeSlots(slots, LocalTime.of(18, 0), LocalTime.of(23, 0));
        return slots;
    }



    private void addTimeSlots(List<LocalTime> slots, LocalTime start, LocalTime end) {
        LocalTime current = start;
        while (!current.isAfter(end)) {
            slots.add(current);
            current = current.plusMinutes(30);
        }
    }



    private void toggleWarningPane(boolean show) {
        allergyWarningAnchorPane.setVisible(show);
        allergyWarningAnchorPane.setDisable(!show);
        serviceRequestAnchorPane.setDisable(show);
    }



    private void displayMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
