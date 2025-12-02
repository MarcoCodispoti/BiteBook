package com.example.bitebook.model.dao.demo;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.dao.ChefDao;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.SpecializationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChefDemoDao implements ChefDao{
    // 1. SIMULAZIONE TABELLA "Chef"
    // Key: ChefID, Value: Oggetto Chef completo
    private static Map<Integer, Chef> chefsMap = new HashMap<>();

    // 2. SIMULAZIONE RELAZIONE "Menu -> Chef" (Foreign Key)
    // Key: MenuID, Value: ChefID (chi ha creato quel menu)
    // Serve per il metodo getChefFromMenu
    private static Map<Integer, Integer> menuToChefMap = new HashMap<>();

    // ---------------------------------------------------------
    // POPOLAMENTO DATI INIZIALI (Eseguito all'avvio)
    // ---------------------------------------------------------
    static {
        System.out.println("[Demo] Pre-caricamento Chef finti...");

        // --- CHEF 1: Alessandro Borghese (Roma) ---
        Chef c1 = new Chef();
        c1.setId(1);
        c1.setName("Alessandro");
        c1.setSurname("Borghese");
        c1.setCity("Roma");
        c1.setStyle(CookingStyle.MODERN);

        List<SpecializationType> specs1 = new ArrayList<>();
        specs1.add(SpecializationType.SEAFOOD);
        specs1.add(SpecializationType.VEGAN);
        c1.setSpecializations(specs1);

        chefsMap.put(1, c1);

        // --- CHEF 2: Antonino Cannavacciuolo (Napoli) ---
        Chef c2 = new Chef();
        c2.setId(2);
        c2.setName("Antonino");
        c2.setSurname("Cannavacciuolo");
        c2.setCity("Napoli");
        c2.setStyle(CookingStyle.CLASSIC);

        List<SpecializationType> specs2 = new ArrayList<>();
        specs2.add(SpecializationType.ITALIAN);
        specs2.add(SpecializationType.SEAFOOD);
        c2.setSpecializations(specs2);

        chefsMap.put(2, c2);

        // --- CHEF 3: Carlo Cracco (Milano) ---
        Chef c3 = new Chef();
        c3.setId(3);
        c3.setName("Carlo");
        c3.setSurname("Cracco");
        c3.setCity("Milano");
        c3.setStyle(CookingStyle.FUSION); // Per esempio

        List<SpecializationType> specs3 = new ArrayList<>();
        specs3.add(SpecializationType.JAPANESE);
        c3.setSpecializations(specs3);

        chefsMap.put(3, c3);

        // --- POPOLAMENTO ASSOCIAZIONI MENU (MenuID -> ChefID) ---
        // Simuliamo che il Menu 210 appartenga allo Chef 1 (Borghese)
        menuToChefMap.put(210, 1);
        // Simuliamo che il Menu 300 appartenga allo Chef 2 (Cannavacciuolo)
        menuToChefMap.put(300, 2);
    }

    // ---------------------------------------------------------
    // METODO 1: findCityChefs (Restituisce solo ID parziali)
    // ---------------------------------------------------------
    @Override
    public void findCityChefs(String cityName) throws NoChefInCityException {
        List<Chef> chefsFound = new ArrayList<>();

        // Iteriamo sui valori della mappa (senza Stream)
        for (Chef c : chefsMap.values()) {
            // Confronto Case-Insensitive per la città
            if (c.getCity() != null && c.getCity().equalsIgnoreCase(cityName)) {

                // Nel DB DAO creavi un oggetto nuovo con solo l'ID. Facciamo uguale.
                Chef proxy = new Chef();
                proxy.setId(c.getId());
                chefsFound.add(proxy);
            }
        }

        if (chefsFound.isEmpty()) {
            throw new NoChefInCityException("No chef found in " + cityName);
        }

        // return chefsFound;
    }

    // ---------------------------------------------------------
    // METODO 2: getChefsInCity (Restituisce oggetti completi)
    // ---------------------------------------------------------
    @Override
    public List<Chef> getChefsInCity(String cityName) throws FailedSearchException {
        List<Chef> cityChefs = new ArrayList<>();
        System.out.println("[Demo] Cerco chef a: " + cityName);

        // Iteriamo sui valori della mappa
        for (Chef c : chefsMap.values()) {
            if (c.getCity() != null && c.getCity().equalsIgnoreCase(cityName)) {
                // Qui aggiungiamo l'oggetto COMPLETO (già popolato nel blocco static)
                cityChefs.add(c);

                System.out.println("Lo chef trovato è: " + c.getId() + " " + c.getName() + " "
                        + c.getSurname() + " " + c.getStyle());
            }
        }

        System.out.println("Ho trovato e sto restituendo: " + cityChefs.size() + " chefs.");
        return cityChefs;
    }

    // ---------------------------------------------------------
    // METODO 3: getChefFromMenu
    // ---------------------------------------------------------
    @Override
    public Chef getChefFromMenu(int menuId) throws FailedSearchException {
        // 1. Cerchiamo l'ID dello chef associato a quel menu
        Integer chefId = menuToChefMap.get(menuId);

        if (chefId != null) {
            // 2. Se abbiamo l'ID dello chef, recuperiamo l'oggetto Chef dalla mappa principale
            Chef foundChef = chefsMap.get(chefId);

            if (foundChef != null) {
                System.out.println("ChefID trovato per menu " + menuId + ": " + foundChef.getId());
                return foundChef;
            }
        }

        // Se non troviamo l'associazione o lo chef, simuliamo l'errore SQL o restituiamo null
        // Nel tuo DbDao lanciavi SQLException se rs.next() era false.
        // throw new Exception("Chef not found for menu ID: " + menuId);
        return null;  // da gestire dopo
    }

}
