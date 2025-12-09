package junit;


/*
    this class tests the clientAllergiesIncompatibility method in the SendServiceRequestController
 */


import com.example.bitebook.controller.application.SendServiceRequestController;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.singleton.LoggedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestAllergenIncompatibility{

//    private final SendServiceRequestController controller = new SendServiceRequestController();
//
//    @AfterEach
//    void tearDown() {
//        LoggedUser.clear();
//    }
//
//    @Test
//    @DisplayName("SAFETY: Rilevamento Incompatibilità (Utente allergico vs Menu pericoloso)")
//    void testIncompatibility_Detected(){
//        Allergen clientAllergen = new Allergen(1, "Arachidi");
//
//        Client loggedClient = new Client();
//        List<Allergen> clientAllergies = new ArrayList<>();
//        clientAllergies.add(clientAllergen);
//        loggedClient.setAllergies(clientAllergies);
//        LoggedUser.getInstance().setClient(loggedClient);
//        List<AllergenBean> menuAllergens = new ArrayList<>();
//        menuAllergens.add(new AllergenBean(2, "Latte"));
//        menuAllergens.add(new AllergenBean(1, "Arachidi")); // <--- ID 1: CONFLITTO!
//
//        boolean isDangerous = controller.clientAllergiesIncompatibility(menuAllergens);
//
//        assertTrue(isDangerous, "Il sistema DEVE rilevare che l'ID 1 è presente in entrambe le liste");
//    }
//
//    @Test
//    @DisplayName("SAFETY: Nessuna Incompatibilità (Menu sicuro)")
//    void testIncompatibility_SafeMenu(){
//        Allergen clientAllergen = new Allergen(5, "Glutine");
//        Client loggedClient = new Client();
//        List<Allergen> clientAllergies = new ArrayList<>();
//        clientAllergies.add(clientAllergen);
//        loggedClient.setAllergies(clientAllergies);
//
//        LoggedUser.getInstance().setClient(loggedClient);
//        List<AllergenBean> menuAllergens = new ArrayList<>();
//        menuAllergens.add(new AllergenBean(2, "Latte"));
//
//        boolean isDangerous = controller.clientAllergiesIncompatibility(menuAllergens);
//
//        assertFalse(isDangerous, "Non ci sono ID corrispondenti, deve restituire false");
//    }
//
//    @Test
//    @DisplayName("EDGE CASE: Utente non loggato (LoggedUser vuoto) -> Deve lanciare Eccezione")
//    void testIncompatibility_NoUserLogged(){
//        LoggedUser.clear();
//        List<AllergenBean> menuAllergens = new ArrayList<>();
//        menuAllergens.add(new AllergenBean(1, "Arachidi"));
//
//        // assertThrows(IllegalStateException.class, () -> controller.clientAllergiesIncompatibility(menuAllergens));
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> controller.clientAllergiesIncompatibility(menuAllergens));
//
//        // 3. VERIFICA MESSAGGIO
//        assertEquals("Unable to get client info", e.getMessage(),
//                "Failed test,wrong error received");
//    }
//
//
//    @Test
//    @DisplayName("EDGE CASE: Menu senza allergeni")
//    void testIncompatibility_EmptyMenu(){
//        Client loggedClient = new Client();
//        List<Allergen> clientAllergies = new ArrayList<>();
//        clientAllergies.add(new Allergen(1, "Arachidi"));
//        loggedClient.setAllergies(clientAllergies);
//        LoggedUser.getInstance().setClient(loggedClient);
//        List<AllergenBean> menuAllergens = new ArrayList<>();
//        boolean isDangerous = controller.clientAllergiesIncompatibility(menuAllergens);
//
//        assertFalse(isDangerous, "Se il menu non ha allergeni, è sicuro");
//    }
//
//
//
//    @Test
//    @DisplayName("EDGE CASE: Lista Menu è NULL -> Ritorna false (Fail Safe)")
//    void testIncompatibility_NullMenuInput(){
//
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> controller.clientAllergiesIncompatibility(null));
//
//        assertEquals("Error: Error while obtaining menu allergens", e.getMessage());
//
//    }
//
//
//
//    @Test
//    @DisplayName("DATA CORRUPTION: Utente loggato ma lista allergie NULL -> Eccezione")
//    void testIncompatibility_UserAllergiesListIsNull(){
//        Client corruptedClient = new Client();
//        corruptedClient.setAllergies(null);
//
//        LoggedUser.getInstance().setClient(corruptedClient);
//
//        List<AllergenBean> menuAllergens = new ArrayList<>();
//        menuAllergens.add(new AllergenBean(1, "Arachidi"));
//
//        //assertThrows(IllegalStateException.class, () -> controller.clientAllergiesIncompatibility(menuAllergens));
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> controller.clientAllergiesIncompatibility(menuAllergens));
//
//        // 3. VERIFICA MESSAGGIO
//        assertEquals("User data error: Unable to get client allergies info", e.getMessage(),
//                "Failed test,wrong error received");
//    }






    private final SendServiceRequestController controller = new SendServiceRequestController();

    @AfterEach
    void tearDown() {
        LoggedUser.clear();
    }


    @Test
    @DisplayName("SAFETY: Rilevamento Incompatibilità (Utente allergico vs Menu pericoloso)")
    void testIncompatibility_Detected(){
        Allergen clientAllergen = new Allergen(1, "Arachidi");

        Client loggedClient = new Client();
        List<Allergen> clientAllergies = new ArrayList<>();
        clientAllergies.add(clientAllergen);
        loggedClient.setAllergies(clientAllergies);
        LoggedUser.getInstance().setClient(loggedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(2, "Latte"));
        menuAllergens.add(new AllergenBean(1, "Arachidi")); // CONFLITTO!

        boolean isDangerous = controller.clientAllergiesIncompatibility(menuAllergens);

        assertTrue(isDangerous, "Il sistema DEVE rilevare che l'ID 1 è presente in entrambe le liste");
    }


    @Test
    @DisplayName("SAFETY: Nessuna Incompatibilità (Menu sicuro)")
    void testIncompatibility_SafeMenu(){
        Allergen clientAllergen = new Allergen(5, "Glutine");
        Client loggedClient = new Client();
        List<Allergen> clientAllergies = new ArrayList<>();
        clientAllergies.add(clientAllergen);
        loggedClient.setAllergies(clientAllergies);
        LoggedUser.getInstance().setClient(loggedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(2, "Latte"));

        boolean isDangerous = controller.clientAllergiesIncompatibility(menuAllergens);

        assertFalse(isDangerous, "Non ci sono ID corrispondenti, deve restituire false");
    }


    @Test
    @DisplayName("EDGE CASE: Menu senza allergeni -> Ritorna False")
    void testIncompatibility_EmptyMenu(){
        Client loggedClient = new Client();
        List<Allergen> clientAllergies = new ArrayList<>();
        clientAllergies.add(new Allergen(1, "Arachidi"));
        loggedClient.setAllergies(clientAllergies);
        LoggedUser.getInstance().setClient(loggedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>(); // Lista vuota

        boolean isDangerous = controller.clientAllergiesIncompatibility(menuAllergens);

        assertFalse(isDangerous, "Se il menu non ha allergeni, è sicuro");
    }


    @Test
    @DisplayName("EDGE CASE: Lista Menu è NULL -> Eccezione 'Error while obtaining menu allergens'")
    void testIncompatibility_NullMenuInput(){
        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                controller.clientAllergiesIncompatibility(null)
        );

        assertEquals("Error: Error while obtaining menu allergens", e.getMessage());
    }

    @Test
    @DisplayName("EDGE CASE: Utente non loggato -> Eccezione 'Unable to get client info'")
    void testIncompatibility_NoUserLogged(){
        LoggedUser.clear();

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(1, "Arachidi"));

        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                controller.clientAllergiesIncompatibility(menuAllergens)
        );

        assertEquals("Unable to get client info", e.getMessage());
    }

    @Test
    @DisplayName("DATA CORRUPTION: Utente loggato ma lista allergie NULL -> Eccezione 'User data error'")
    void testIncompatibility_UserAllergiesListIsNull(){
        Client corruptedClient = new Client();
        corruptedClient.setAllergies(null); // Dati corrotti

        LoggedUser.getInstance().setClient(corruptedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(1, "Arachidi"));

        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                controller.clientAllergiesIncompatibility(menuAllergens)
        );

        assertEquals("User data error: Unable to get client allergies info", e.getMessage());
    }


}
