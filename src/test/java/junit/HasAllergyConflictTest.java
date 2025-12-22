package junit;


/*
    this class tests the clientAllergiesIncompatibility method in the SendServiceRequestController
 */


import com.example.bitebook.controller.application.ServiceRequestController;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.session.LoggedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HasAllergyConflictTest {


    private final ServiceRequestController controller = new ServiceRequestController();


    @AfterEach
    void tearDown() {
        LoggedUser.clear();
    }


    @Test
    @DisplayName("SAFETY: Incompatibility Detection (Allergic User vs Dangerous Menu)")
    void testIncompatibility_Detected(){
        Allergen clientAllergen = new Allergen(1, "Arachidi");

        Client loggedClient = new Client();
        List<Allergen> clientAllergies = new ArrayList<>();
        clientAllergies.add(clientAllergen);
        loggedClient.setAllergies(clientAllergies);
        LoggedUser.getInstance().setClient(loggedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(2, "Latte"));
        menuAllergens.add(new AllergenBean(1, "Arachidi")); // CONFLICT!

        boolean isDangerous = controller.hasAllergyConflict(menuAllergens);

        assertTrue(isDangerous, "The system MUST detect that ID 1 is present in both lists");
    }


    @Test
    @DisplayName("SAFETY: No Incompatibility (Safe Menu)")
    void testIncompatibility_SafeMenu(){
        Allergen clientAllergen = new Allergen(5, "Glutine");
        Client loggedClient = new Client();
        List<Allergen> clientAllergies = new ArrayList<>();
        clientAllergies.add(clientAllergen);
        loggedClient.setAllergies(clientAllergies);
        LoggedUser.getInstance().setClient(loggedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(2, "Latte"));

        boolean isDangerous = controller.hasAllergyConflict(menuAllergens);

        assertFalse(isDangerous, "There are no matching IDs, must return false");
    }


    @Test
    @DisplayName("EDGE CASE: Menu without allergens -> Returns False")
    void testIncompatibility_EmptyMenu(){
        Client loggedClient = new Client();
        List<Allergen> clientAllergies = new ArrayList<>();
        clientAllergies.add(new Allergen(1, "Arachidi"));
        loggedClient.setAllergies(clientAllergies);
        LoggedUser.getInstance().setClient(loggedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>(); // Empty list

        boolean isDangerous = controller.hasAllergyConflict(menuAllergens);

        assertFalse(isDangerous, "If the menu has no allergens, it is safe");
    }


    @Test
    @DisplayName("EDGE CASE: Menu List is NULL -> Exception 'Error while obtaining menu allergens'")
    void testIncompatibility_NullMenuInput(){
        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                controller.hasAllergyConflict(null)
        );

        assertEquals("Error: Error while obtaining menu allergens", e.getMessage());
    }


    @Test
    @DisplayName("EDGE CASE: User not logged in -> Exception 'Unable to get client info'")
    void testIncompatibility_NoUserLogged(){
        LoggedUser.clear();

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(1, "Arachidi"));

        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                controller.hasAllergyConflict(menuAllergens)
        );

        assertEquals("Unable to get client info", e.getMessage());
    }


    @Test
    @DisplayName("DATA CORRUPTION: User logged in but allergies list is NULL -> Exception 'User data error'")
    void testIncompatibility_UserAllergiesListIsNull(){
        Client corruptedClient = new Client();
        corruptedClient.setAllergies(null); // Corrupted data

        LoggedUser.getInstance().setClient(corruptedClient);

        List<AllergenBean> menuAllergens = new ArrayList<>();
        menuAllergens.add(new AllergenBean(1, "Arachidi"));

        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                controller.hasAllergyConflict(menuAllergens)
        );

        assertEquals("User data error: Unable to get client allergies info", e.getMessage());
    }


}
