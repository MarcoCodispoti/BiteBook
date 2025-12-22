package junit;

import com.example.bitebook.controller.application.ServiceRequestController;
import com.example.bitebook.model.bean.MenuBean;
import com.example.bitebook.model.bean.ReservationDetailsBean;
import com.example.bitebook.model.enums.MenuLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/*
    this class tests the calculateTotalPrice method in the SendServiceRequestController
 */

class CalculateTotalPriceTest {


    private final ServiceRequestController controller = new ServiceRequestController();


    @Test
    @DisplayName("Correct Calculation: BASE Level (No Surcharge)")
    void testCalculate_Base() {
        // ARRANGE
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50); // 50€
        menu.setPremiumLevelSurcharge(50);
        menu.setLuxeLevelSurcharge(51);

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(3);
        res.setSelectedMenuLevel(MenuLevel.BASE);

        // ACT & ASSERT
        // 50 * 3 = 150
        assertEquals(150, controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Correct Calculation: PREMIUM Level (+Surcharge)")
    void testCalculate_Premium() {
        // ARRANGE
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);
        menu.setPremiumLevelSurcharge(20); // +20€
        menu.setLuxeLevelSurcharge(21); // cannot be null

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(2);
        res.setSelectedMenuLevel(MenuLevel.PREMIUM);

        // ACT & ASSERT
        // (50 + 20) * 2 = 140
        assertEquals(140, controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Correct Calculation: LUXE Level (+Surcharge)")
    void testCalculate_Luxe() {
        // ARRANGE
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);
        menu.setPremiumLevelSurcharge(27);
        menu.setLuxeLevelSurcharge(50); // +50€

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(2);
        res.setSelectedMenuLevel(MenuLevel.LUXE);

        // ACT & ASSERT
        // (50 + 50) * 2 = 200
        assertEquals(200, controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Error: Negative Participant Number")
    void testCalculate_NegativeParticipants() {
        // ARRANGE
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(-5); // Invalid input!
        res.setSelectedMenuLevel(MenuLevel.BASE);

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Error: Null Menu Level")
    void testCalculate_NullLevel(){
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(2);
        res.setSelectedMenuLevel(null); // Invalid input!

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }

    @Test
    @DisplayName("Boundary: 0 Participants -> Logical Error")
    void testCalculate_ZeroParticipants(){
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(0); // A reservation without guests makes no sense
        res.setSelectedMenuLevel(MenuLevel.BASE);

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Boundary: Missing Menu Level Surcharges")
    void testCalculate_missingSurchargePrices(){
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);
        // missing surcharges

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(2);
        res.setSelectedMenuLevel(MenuLevel.BASE);

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Boundary: Luxe Surcharge Lower Than Premium Surcharge")
    void testCalculate_wrongSurchargePrices() {
        // ARRANGE
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);
        menu.setPremiumLevelSurcharge(20);
        menu.setLuxeLevelSurcharge(15);     // luxeLevelSurcharge must be higher than premiumLevelSurcharge

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(0);
        res.setSelectedMenuLevel(MenuLevel.BASE);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }


}
