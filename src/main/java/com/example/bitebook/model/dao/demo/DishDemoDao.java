package com.example.bitebook.model.dao.demo;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.dao.DishDao;
import com.example.bitebook.model.enums.CourseType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishDemoDao implements DishDao {

//    // 1. SIMULAZIONE TABELLA DI UNIONE "AllergensDish"
//    // Key: DishID (l'ID del piatto)
//    // Value: List<Allergen> (la lista delle allergie di quel piatto)
//    private static Map<Integer, List<Allergen>> dishAllergensMap = new HashMap<>();
//
//    // ---------------------------------------------------------
//    // POPOLAMENTO DATI INIZIALI (Coerente con MenuDemoDao)
//    // ---------------------------------------------------------
//    static {
//        System.out.println("[Demo] Pre-caricamento Allergie dei Piatti...");
//
//        // Creiamo oggetti Allergeni base per riutilizzarli
//        Allergen glutine = new Allergen(1, "Glutine");
//        Allergen lattosio = new Allergen(2, "Lattosio");
//        Allergen crostacei = new Allergen(3, "Crostacei");
//        Allergen uova = new Allergen(4, "Uova"); // Aggiungo per completezza
//
//        // --- PIATTO 1001: "Carbonara Sbagliata" (dal Menu 210) ---
//        // Contiene: Uova, Glutine (pasta), Lattosio (formaggio/panna)
//        List<Allergen> allergens1001 = new ArrayList<>();
//        allergens1001.add(glutine);
//        allergens1001.add(uova);
//        allergens1001.add(lattosio);
//
//        dishAllergensMap.put(1001, allergens1001);
//
//        // --- PIATTO 1002: "Saltimbocca 2.0" (dal Menu 210) ---
//        // Contiene: Glutine (infarinatura)
//        List<Allergen> allergens1002 = new ArrayList<>();
//        allergens1002.add(glutine);
//
//        dishAllergensMap.put(1002, allergens1002);
//
//        // --- PIATTO 2001: "Linguine allo scoglio" (dal Menu 300) ---
//        // Contiene: Glutine (pasta), Crostacei
//        List<Allergen> allergens2001 = new ArrayList<>();
//        allergens2001.add(glutine);
//        allergens2001.add(crostacei);
//
//        dishAllergensMap.put(2001, allergens2001);
//
//        // --- PIATTO 2002: "Babà al rum" (dal Menu 300) ---
//        // Contiene: Glutine, Lattosio, Uova
//        List<Allergen> allergens2002 = new ArrayList<>();
//        allergens2002.add(glutine);
//        allergens2002.add(lattosio);
//        allergens2002.add(uova);
//
//        dishAllergensMap.put(2002, allergens2002);
//    }


    // ---------------------------------------------------------
    // METODO: getDishAllergens
    // ---------------------------------------------------------

    // Da spostare in AllergenDao

//    @Override
//    public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException{
//        // Nel DB DAO facevi: CALL getDishAllergens(?) -> SELECT ... JOIN ... WHERE DishId = ?
//        // Qui facciamo un lookup diretto nella mappa usando l'ID del piatto.
//
//        List<Allergen> allergens = dishAllergensMap.get(dishId);
//
//        if (allergens != null) {
//            // Trovato! Restituiamo la lista
//            return allergens;
//        } else {
//            // Se il piatto non ha allergie (o l'ID non esiste nella mappa allergie),
//            // restituiamo un vettore vuoto invece di null, così non crasha l'interfaccia.
//            // È un comportamento sicuro.
//            return new ArrayList<>();
//        }
//    }

    // 1. SPOSTATO QUI: La mappa che collega MenuID -> Lista Piatti
    private static Map<Integer, List<Dish>> menuCoursesMap = new HashMap<>();

    // 2. SPOSTATO QUI: Popolamento dati finti dei PIATTI
    static {
        System.out.println("[DishDemoDao] Pre-caricamento Piatti finti...");

        // --- PIATTI PER MENU 210 (Menu Romano) ---
        Dish d1 = new Dish();
        d1.setId(1001);
        d1.setName("Carbonara Sbagliata");
        d1.setCourseType(CourseType.MAIN_COURSE);
        d1.setDescription("Carbonara con gamberi");

        Dish d2 = new Dish();
        d2.setId(1002);
        d2.setName("Saltimbocca 2.0");
        d2.setCourseType(CourseType.FIRST_COURSE);
        d2.setDescription("Vitello cotto a bassa temperatura");

        List<Dish> courses210 = new ArrayList<>();
        courses210.add(d1);
        courses210.add(d2);

        menuCoursesMap.put(210, courses210);

        // --- PIATTI PER MENU 300 (Menu Napoli) ---
        Dish d3 = new Dish();
        d3.setId(2001);
        d3.setName("Linguine allo scoglio");
        d3.setCourseType(CourseType.MAIN_COURSE);
        d3.setDescription("Pasta fresca con frutti di mare");

        Dish d4 = new Dish();
        d4.setId(2002);
        d4.setName("Babà al rum");
        d4.setCourseType(CourseType.DESSERT);
        d4.setDescription("Classico napoletano");

        List<Dish> courses300 = new ArrayList<>();
        courses300.add(d3);
        courses300.add(d4);

        menuCoursesMap.put(300, courses300);
    }

    // 3. IMPLEMENTAZIONE METODO (Spostata da MenuDemoDao)

    public List<Dish> getMenuCourses(int menuId) throws FailedSearchException {
        // Simulazione Query: SELECT * FROM Dish WHERE MenuId = ?
        List<Dish> courses = menuCoursesMap.get(menuId);

        if (courses != null) {
            return courses; // Ritorna la lista in memoria
        } else {
            return new ArrayList<>(); // Ritorna lista vuota se non ci sono piatti
        }
    }

    // Qui dovrai implementare anche getDishAllergens (puoi restituire lista vuota per ora)

    public List<com.example.bitebook.model.Allergen> getDishAllergens(int dishId) throws FailedSearchException {
        return new ArrayList<>(); // Stub per ora
    }


}
