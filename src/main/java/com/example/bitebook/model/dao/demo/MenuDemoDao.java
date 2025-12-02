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
    // 1. SIMULAZIONE TABELLA "Menu"
    // Key: MenuID, Value: Oggetto Menu completo
    private static Map<Integer, Menu> menusMap = new HashMap<>();

    // 2. SIMULAZIONE "Menu dello Chef" (Relazione Chef -> Menus)
    // Key: ChefID, Value: Lista dei Menu di quello chef
    private static Map<Integer, List<Menu>> chefMenusMap = new HashMap<>();

    // 3. SIMULAZIONE "Portate del Menu" (Relazione Menu -> Dishes)
    // Key: MenuID, Value: Lista dei Piatti di quel menu
    private static Map<Integer, List<Dish>> menuCoursesMap = new HashMap<>();


    // ---------------------------------------------------------
    // POPOLAMENTO DATI INIZIALI (Eseguito all'avvio)
    // ---------------------------------------------------------
    static {
        System.out.println("[Demo] Pre-caricamento Menu e Piatti finti...");

        // --- MENU 1 (ID 210) - Collegato a Chef 1 (Borghese) ---
        Menu m1 = new Menu();
        m1.setId(210);
        m1.setName("Menu Romano Moderno");
        m1.setDietType(DietType.NONE);
        m1.setPricePerPerson(50);
        m1.setPremiumLevelSurcharge(10); // Supplementi
        m1.setLuxeLevelSurcharge(20);

        // Salviamo nella mappa generale
        menusMap.put(210, m1);

        // Salviamo nella mappa dello Chef 1
        List<Menu> chef1Menus = new ArrayList<>();
        chef1Menus.add(m1);
        chefMenusMap.put(1, chef1Menus);

        // --- PIATTI PER MENU 210 ---
        Dish d1 = new Dish();
        d1.setId(1001); d1.setName("Carbonara Sbagliata"); d1.setCourseType(CourseType.MAIN_COURSE); d1.setDescription("Carbonara con gamberi");

        Dish d2 = new Dish();
        d2.setId(1002); d2.setName("Saltimbocca 2.0"); d2.setCourseType(CourseType.FIRST_COURSE); d2.setDescription("Vitello cotto a bassa temperatura");

        List<Dish> courses210 = new ArrayList<>();
        courses210.add(d1);
        courses210.add(d2);
        m1.setNumberOfCourses(courses210.size()); // Impostiamo il numero di portate

        menuCoursesMap.put(210, courses210);


        // --- MENU 2 (ID 300) - Collegato a Chef 2 (Cannavacciuolo) ---
        Menu m2 = new Menu();
        m2.setId(300);
        m2.setName("Menu Napoli Classico");
        m2.setDietType(DietType.NONE);
        m2.setPricePerPerson(70);
        m2.setPremiumLevelSurcharge(15);
        m2.setLuxeLevelSurcharge(30);

        // Salviamo nella mappa generale
        menusMap.put(300, m2);

        // Salviamo nella mappa dello Chef 2
        List<Menu> chef2Menus = new ArrayList<>();
        chef2Menus.add(m2);
        chefMenusMap.put(2, chef2Menus);

        // --- PIATTI PER MENU 300 ---
        Dish d3 = new Dish();
        d3.setId(2001); d3.setName("Linguine allo scoglio"); d3.setCourseType(CourseType.MAIN_COURSE); d3.setDescription("Pasta fresca con frutti di mare");

        Dish d4 = new Dish();
        d4.setId(2002); d4.setName("Babà al rum"); d4.setCourseType(CourseType.DESSERT); d4.setDescription("Classico napoletano");

        List<Dish> courses300 = new ArrayList<>();
        courses300.add(d3);
        courses300.add(d4);
        m2.setNumberOfCourses(courses300.size());

        menuCoursesMap.put(300, courses300);
    }


    // ---------------------------------------------------------
    // METODO 1: getChefMenus
    // ---------------------------------------------------------
    @Override
    public List<Menu> getChefMenus(int chefId) throws FailedSearchException{
        // Nel DB facevi: SELECT ... WHERE ChefId = ?
        // Qui recuperiamo direttamente la lista associata allo ChefID dalla mappa.

        List<Menu> menus = chefMenusMap.get(chefId);

        if (menus != null) {
            System.out.println("[Demo] Trovati " + menus.size() + " menu per chef " + chefId);

            // Nota: In DemoDao possiamo restituire direttamente i menu salvati.
            // Non serve ricreare oggetti 'new Menu()' come nel DB DAO perché qui
            // abbiamo già gli oggetti in memoria.
            return menus;
        } else {
            System.out.println("[Demo] Nessun menu trovato per chef " + chefId);
            return new ArrayList<>(); // Restituisce lista vuota invece di null
        }
    }


    // ---------------------------------------------------------
    // METODO 2: getMenuCourses
    // ---------------------------------------------------------
    @Override
    public List<Dish> getMenuCourses(int menuId) {
        // Nel DB facevi una JOIN complessa. Qui è un lookup immediato per Key.

        List<Dish> courses = menuCoursesMap.get(menuId);

        if (courses != null) {
            System.out.println("[Demo] Trovate " + courses.size() + " portate per menu " + menuId);

            // Stampiamo per coerenza col tuo vecchio codice
            for(Dish d : courses) {
                System.out.println("Ho trovato il piatto:" + d.getName() + " con Id: " + d.getId());
            }

            return courses;
        } else {
            System.out.println("[Demo] Nessuna portata trovata per menu " + menuId);
            return null; // O new ArrayList<>()
        }
    }


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
