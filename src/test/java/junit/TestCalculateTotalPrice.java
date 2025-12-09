package junit;

import com.example.bitebook.controller.application.SendServiceRequestController;
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

public class TestCalculateTotalPrice{


    private final SendServiceRequestController controller = new SendServiceRequestController();


    @Test
    @DisplayName("Calcolo corretto: Livello BASE (Nessun supplemento)")
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
    @DisplayName("Calcolo corretto: Livello PREMIUM (+Supplemento)")
    void testCalculate_Premium() {
        // ARRANGE
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);
        menu.setPremiumLevelSurcharge(20); // +20€
        menu.setLuxeLevelSurcharge(21); // non può essere nullo

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(2);
        res.setSelectedMenuLevel(MenuLevel.PREMIUM);

        // ACT & ASSERT
        // (50 + 20) * 2 = 140
        assertEquals(140, controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Calcolo corretto: Livello LUXE (+Supplemento)")
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
    @DisplayName("Errore: Numero partecipanti negativo")
    void testCalculate_NegativeParticipants() {
        // ARRANGE
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(-5); // Input invalido!
        res.setSelectedMenuLevel(MenuLevel.BASE);

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }

    @Test
    @DisplayName("Errore: Livello Menu Null")
    void testCalculate_NullLevel(){
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(2);
        res.setSelectedMenuLevel(null); // Input invalido!

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }

    @Test
    @DisplayName("Limite: 0 Partecipanti -> Errore logico")
    void testCalculate_ZeroParticipants(){
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(0); // Non ha senso una prenotazione senza ospiti
        res.setSelectedMenuLevel(MenuLevel.BASE);

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }


    @Test
    @DisplayName("Limite: mancano i supplementi dei livelli del menu")
    void testCalculate_missingSurchargePrices(){
        MenuBean menu = new MenuBean();
        menu.setPricePerPerson(50);
        // mancano i supplementi

        ReservationDetailsBean res = new ReservationDetailsBean();
        res.setParticipantNumber(2);
        res.setSelectedMenuLevel(MenuLevel.BASE);

        assertThrows(IllegalArgumentException.class, () -> controller.calculateTotalPrice(res, menu));
    }



    @Test
    @DisplayName("Limite: Supplemento luxe minore del supplemento premium")
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
