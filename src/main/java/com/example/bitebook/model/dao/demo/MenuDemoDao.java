package com.example.bitebook.model.dao.demo;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.Menu;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.enums.CourseType;
import com.example.bitebook.model.enums.DietType;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MenuDemoDao implements MenuDao{

    // Manteniamo solo le mappe dei MENU
    private static Map<Integer, Menu> menusMap = new HashMap<>();
    private static Map<Integer, List<Menu>> chefMenusMap = new HashMap<>();

    // RIMOSSO: private static Map<Integer, List<Dish>> menuCoursesMap...

    static {
        System.out.println("[MenuDemoDao] Pre-caricamento Menu finti...");

        // --- MENU 1 (ID 210) ---
        Menu m1 = new Menu();
        m1.setId(210);
        m1.setName("Menu Romano Moderno");
        m1.setDietType(DietType.NONE);
        m1.setPricePerPerson(50);
        m1.setPremiumLevelSurcharge(10);
        m1.setLuxeLevelSurcharge(20);

        // Piccolo trucco: Poiché i piatti sono gestiti altrove, qui hardcodiamo il numero
        // per coerenza, oppure lo lasciamo a 0 (verrà riempito se serve).
        m1.setNumberOfCourses(2);

        menusMap.put(210, m1);

        List<Menu> chef1Menus = new ArrayList<>();
        chef1Menus.add(m1);
        chefMenusMap.put(1, chef1Menus);

        // --- MENU 2 (ID 300) ---
        Menu m2 = new Menu();
        m2.setId(300);
        m2.setName("Menu Napoli Classico");
        m2.setDietType(DietType.NONE);
        m2.setPricePerPerson(70);
        m2.setPremiumLevelSurcharge(15);
        m2.setLuxeLevelSurcharge(30);
        m2.setNumberOfCourses(2);

        menusMap.put(300, m2);

        List<Menu> chef2Menus = new ArrayList<>();
        chef2Menus.add(m2);
        chefMenusMap.put(2, chef2Menus);

        // RIMOSSO: Tutta la creazione dei Dish
    }

    @Override
    public List<Menu> getChefMenus(int chefId) throws FailedSearchException {
        List<Menu> menus = chefMenusMap.get(chefId);
        if (menus != null) {
            return menus;
        } else {
            return new ArrayList<>();
        }
    }


    // ---------------------------------------------------------
    // METODO 2: getMenuCourses
    // ---------------------------------------------------------

    // Spostato in DishDao
//    @Override
//    public List<Dish> getMenuCourses(int menuId) throws FailedSearchException {
//        // Nel DB facevi una JOIN complessa. Qui è un lookup immediato per Key.
//
//        List<Dish> courses = menuCoursesMap.get(menuId);
//
//        if (courses != null) {
//            System.out.println("[Demo] Trovate " + courses.size() + " portate per menu " + menuId);
//
//            // Stampiamo per coerenza col tuo vecchio codice
//            for(Dish d : courses) {
//                System.out.println("Ho trovato il piatto:" + d.getName() + " con Id: " + d.getId());
//            }
//
//            return courses;
//        } else {
//            System.out.println("[Demo] Nessuna portata trovata per menu " + menuId);
//            return null; // O new ArrayList<>()
//        }
//    }


    // ---------------------------------------------------------
    // METODO 3: getMenuLevelsSurcharge
    // ---------------------------------------------------------
    @Override
    public Menu getMenuLevelsSurcharge(int menuId) throws SQLException {
        // Recuperiamo l'oggetto Menu intero
        Menu fullMenu = menusMap.get(menuId);

        if (fullMenu != null) {
            // Nel DB DAO creavi un oggetto parziale. Qui possiamo restituire
            // o il menu completo (che contiene già i surcharge) o crearne uno piccolo
            // per imitare esattamente il DB.
            // Per semplicità e sicurezza, restituiamo un oggetto con i soli dati richiesti
            // oppure direttamente fullMenu (funzionerebbe uguale).

            Menu surchargeInfo = new Menu();
            surchargeInfo.setPremiumLevelSurcharge(fullMenu.getPremiumLevelSurcharge());
            surchargeInfo.setLuxeLevelSurcharge(fullMenu.getLuxeLevelSurcharge());

            return surchargeInfo;
        } else {
            // Nel tuo DB DAO lanciavi SQLException se non trovava nulla
            throw new SQLException("[Demo] Menu ID " + menuId + " non trovato.");
        }
    }


}
