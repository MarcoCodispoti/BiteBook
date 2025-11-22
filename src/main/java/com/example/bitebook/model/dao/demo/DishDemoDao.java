package com.example.bitebook.model.dao.demo;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.dao.DishDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DishDemoDao implements DishDao {

    // 1. SIMULAZIONE TABELLA DI UNIONE "AllergensDish"
    // Key: DishID (l'ID del piatto)
    // Value: Vector<Allergen> (la lista delle allergie di quel piatto)
    private static Map<Integer, Vector<Allergen>> dishAllergensMap = new HashMap<>();

    // ---------------------------------------------------------
    // POPOLAMENTO DATI INIZIALI (Coerente con MenuDemoDao)
    // ---------------------------------------------------------
    static {
        System.out.println("[Demo] Pre-caricamento Allergie dei Piatti...");

        // Creiamo oggetti Allergeni base per riutilizzarli
        Allergen glutine = new Allergen(1, "Glutine");
        Allergen lattosio = new Allergen(2, "Lattosio");
        Allergen crostacei = new Allergen(3, "Crostacei");
        Allergen uova = new Allergen(4, "Uova"); // Aggiungo per completezza

        // --- PIATTO 1001: "Carbonara Sbagliata" (dal Menu 210) ---
        // Contiene: Uova, Glutine (pasta), Lattosio (formaggio/panna)
        Vector<Allergen> allergens1001 = new Vector<>();
        allergens1001.add(glutine);
        allergens1001.add(uova);
        allergens1001.add(lattosio);

        dishAllergensMap.put(1001, allergens1001);

        // --- PIATTO 1002: "Saltimbocca 2.0" (dal Menu 210) ---
        // Contiene: Glutine (infarinatura)
        Vector<Allergen> allergens1002 = new Vector<>();
        allergens1002.add(glutine);

        dishAllergensMap.put(1002, allergens1002);

        // --- PIATTO 2001: "Linguine allo scoglio" (dal Menu 300) ---
        // Contiene: Glutine (pasta), Crostacei
        Vector<Allergen> allergens2001 = new Vector<>();
        allergens2001.add(glutine);
        allergens2001.add(crostacei);

        dishAllergensMap.put(2001, allergens2001);

        // --- PIATTO 2002: "Babà al rum" (dal Menu 300) ---
        // Contiene: Glutine, Lattosio, Uova
        Vector<Allergen> allergens2002 = new Vector<>();
        allergens2002.add(glutine);
        allergens2002.add(lattosio);
        allergens2002.add(uova);

        dishAllergensMap.put(2002, allergens2002);
    }


    // ---------------------------------------------------------
    // METODO: getDishAllergens
    // ---------------------------------------------------------
    @Override
    public Vector<Allergen> getDishAllergens(int dishId){
        // Nel DB DAO facevi: CALL getDishAllergens(?) -> SELECT ... JOIN ... WHERE DishId = ?
        // Qui facciamo un lookup diretto nella mappa usando l'ID del piatto.

        Vector<Allergen> allergens = dishAllergensMap.get(dishId);

        if (allergens != null) {
            // Trovato! Restituiamo la lista
            return allergens;
        } else {
            // Se il piatto non ha allergie (o l'ID non esiste nella mappa allergie),
            // restituiamo un vettore vuoto invece di null, così non crasha l'interfaccia.
            // È un comportamento sicuro.
            return new Vector<>();
        }
    }

}
