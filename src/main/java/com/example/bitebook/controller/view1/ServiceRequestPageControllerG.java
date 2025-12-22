package com.example.bitebook.controller.view1;

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
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServiceRequestPageControllerG{


    private static final Logger logger = Logger.getLogger(ServiceRequestPageControllerG.class.getName());


    @FXML
    private Button sendRequestButton;
    @FXML
    private Button backButton;
    @FXML
    private Label totalPriceLabel;
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
    private Label messageLabel;
    @FXML
    private DatePicker serviceDatePicker;
    @FXML
    private ComboBox<LocalTime> timeComboBox;
    @FXML
    private TextField addressTextField;
    @FXML
    private ComboBox<String> numberOfParticipantsComboBox;
    @FXML
    private ComboBox<MenuLevel> ingredientsLevelComboBox;
    @FXML
    private AnchorPane allergenWarningAnchorPane;


    private final ServiceRequestController serviceRequestController = new ServiceRequestController();
    private boolean ignoreAllergenWarning = false;
    private ChefBean menuChefBean;
    private MenuBean selectedMenuBean;
    private List<AllergenBean> menuAllergenBeans;
    private final ReservationDetailsBean reservationDetailsBean = new ReservationDetailsBean();



    @FXML
    void handleSendRequest() {
        messageLabel.setVisible(false);

        if (!validateAndCollectFormData()) {
            return;
        }

        boolean hasIncompatibility;
        try {
            hasIncompatibility = serviceRequestController.hasAllergyConflict(menuAllergenBeans);
        }catch(IllegalStateException e){
            logger.log(Level.SEVERE, "System Error: Unable to check incompatibilities", e);
            displayError("Error: Unable to check incompatibility");
            return;
        }

        if (hasIncompatibility && !ignoreAllergenWarning) {
            showAllergenWarning();
            return;
        }

        proceedToPayment();
    }



    @FXML
    void handleProceedAnyway() {
        ignoreAllergenWarning = true;
        hideAllergenWarning();
        handleSendRequest();
    }



    @FXML
    void handleCancelRequest() {
        FxmlLoader.setPage("ClientHomePage");
    }



    @FXML
    void handleBack() {
        MenuDetailsPageControllerG controller = FxmlLoader.setPageAndReturnController("MenuDetailsPage");
        if (controller != null) {
            controller.initData(selectedMenuBean, menuChefBean);
        }
    }



    @FXML
    public void initialize() {
        resetUIState();

        ingredientsLevelComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MenuLevel level) {
                if (level == null) return null;
                return formatMenuLevelLabel(level);
            }

            @Override
            public MenuLevel fromString(String string) {
                return null;
            }
        });

        numberOfParticipantsComboBox.valueProperty().addListener(_ -> updateTotalPrice());
        ingredientsLevelComboBox.valueProperty().addListener(_ -> updateTotalPrice());
    }



    public void initData(MenuBean selectedMenuBean, List<AllergenBean> menuAllergenBeans, ChefBean menuChefBean) {
        this.selectedMenuBean = selectedMenuBean;
        this.menuAllergenBeans = menuAllergenBeans;
        this.menuChefBean = menuChefBean;

        nameLabel.setText(selectedMenuBean.getName());
        numberOfCoursesLabel.setText(String.valueOf(selectedMenuBean.getNumberOfCourses()));

        if (selectedMenuBean.getDietType() != null) {
            dietTypeLabel.setText(selectedMenuBean.getDietType().toString().toLowerCase());
        }

        pricePerPersonLabel.setText(selectedMenuBean.getPricePerPerson() + " €");
        allergensLabel.setText(formatAllergensList());

        fillTimeComboBox();
        fillNumberOfParticipantsComboBox();
        fillIngredientsLevelComboBox();
    }



    private void resetUIState() {
        ignoreAllergenWarning = false;
        allergenWarningAnchorPane.setVisible(false);
        messageLabel.setVisible(false);
        messageLabel.setText("");
    }



    private boolean validateAndCollectFormData() {

        LocalDate selectedDate = serviceDatePicker.getValue();
        if (selectedDate == null || !selectedDate.isAfter(LocalDate.now())) {
            displayError("Please select a valid date after today.");
            return false;
        }

        LocalTime selectedTime = timeComboBox.getValue();
        if (selectedTime == null) {
            displayError("Please select a time.");
            return false;
        }

        String address = addressTextField.getText();
        if (address == null || address.trim().length() < 6) {
            displayError("Please enter a valid address.");
            return false;
        }

        if (numberOfParticipantsComboBox.getValue() == null) {
            displayError("Please select the number of participants.");
            return false;
        }
        int participants = Integer.parseInt(numberOfParticipantsComboBox.getValue());

        MenuLevel level = ingredientsLevelComboBox.getValue();
        if (level == null) {
            displayError("Please select a menu level.");
            return false;
        }

        reservationDetailsBean.setDate(selectedDate);
        reservationDetailsBean.setTime(selectedTime);
        reservationDetailsBean.setAddress(address.trim());
        reservationDetailsBean.setParticipantNumber(participants);
        reservationDetailsBean.setSelectedMenuLevel(level);

        return true;
    }



    private void proceedToPayment() {
        PaymentPageControllerG controller = FxmlLoader.setPageAndReturnController("PaymentPage");
        if (controller != null) {
            controller.initData(selectedMenuBean, reservationDetailsBean, menuAllergenBeans, menuChefBean);
        }
    }



    private void updateTotalPrice() {
        String participantsStr = numberOfParticipantsComboBox.getValue();
        MenuLevel level = ingredientsLevelComboBox.getValue();

        if (participantsStr != null && level != null && selectedMenuBean != null) {
            try {
                int participants = Integer.parseInt(participantsStr);

                ReservationDetailsBean tempBean = new ReservationDetailsBean();
                tempBean.setParticipantNumber(participants);
                tempBean.setSelectedMenuLevel(level);

                int price = serviceRequestController.calculateTotalPrice(tempBean, selectedMenuBean);
                totalPriceLabel.setText(price + " €");
            } catch (NumberFormatException _) {
                totalPriceLabel.setText("--");
            }
        } else {
            totalPriceLabel.setText("--");
        }
    }



    private String formatAllergensList() {
        if (menuAllergenBeans == null || menuAllergenBeans.isEmpty()) return "None";
        return menuAllergenBeans.stream()
                .map(AllergenBean::getName)
                .collect(Collectors.joining(", "));
    }



    private void fillNumberOfParticipantsComboBox() {
        numberOfParticipantsComboBox.getItems().clear();
        for (int i = 1; i <= 10; i++) {
            numberOfParticipantsComboBox.getItems().add(String.valueOf(i));
        }
    }



    private void fillIngredientsLevelComboBox() {
        try {
            selectedMenuBean = serviceRequestController.populateMenuSurcharges(selectedMenuBean);

            ingredientsLevelComboBox.getItems().clear();
            ingredientsLevelComboBox.getItems().addAll(MenuLevel.BASE, MenuLevel.PREMIUM, MenuLevel.LUXE);
            ingredientsLevelComboBox.getSelectionModel().select(MenuLevel.BASE);

        } catch (FailedSearchException e){
            logger.log(Level.SEVERE, "Error while loading pricing levels", e);
            displayError("Unable to load menu pricing levels.");
        }
    }



    private String formatMenuLevelLabel(MenuLevel level) {
        if (selectedMenuBean == null) return level.toString();

        int surcharge = switch (level) {
            case BASE -> 0;
            case PREMIUM -> selectedMenuBean.getPremiumLevelSurcharge();
            case LUXE -> selectedMenuBean.getLuxeLevelSurcharge();
        };
        return level + " (Surcharge: +" + surcharge + " €)";
    }



    private void fillTimeComboBox() {
        timeComboBox.getItems().clear();
        timeComboBox.getItems().addAll(generateTimeSlots());
    }



    private List<LocalTime> generateTimeSlots() {
        List<LocalTime> slots = new ArrayList<>();
        addTimeSlots(slots, LocalTime.of(12, 0), LocalTime.of(14, 0));
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



    private void showAllergenWarning() {
        backButton.setDisable(true);
        sendRequestButton.setDisable(true);
        allergenWarningAnchorPane.setVisible(true);
    }



    private void hideAllergenWarning() {
        backButton.setDisable(false);
        sendRequestButton.setDisable(false);
        allergenWarningAnchorPane.setVisible(false);
    }



    private void displayError(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }


}
