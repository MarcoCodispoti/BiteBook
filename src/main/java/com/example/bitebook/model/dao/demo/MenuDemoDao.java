package com.example.bitebook.model.dao.demo;

import com.example.bitebook.model.Menu;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.enums.DietType;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MenuDemoDao implements MenuDao{

    
    private static final Map<Integer, Menu> menusMap = new HashMap<>();
    private static final Map<Integer, List<Menu>> chefMenusMap = new HashMap<>();


    static {
        // --- MENU 1 (ID 210) ---
        Menu m1 = new Menu();
        m1.setId(210);
        m1.setName("Menu Romano Moderno");
        m1.setDietType(DietType.NONE);
        m1.setPricePerPerson(50);
        m1.setPremiumLevelSurcharge(10);
        m1.setLuxeLevelSurcharge(20);
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
    }



    @Override
    public List<Menu> getChefMenus(int chefId) {
        List<Menu> menus = chefMenusMap.get(chefId);
        return menus != null ? new ArrayList<>(menus) : new ArrayList<>();
    }



    @Override
    public Menu populateMenuLevelsSurcharge(int menuId) {
        Menu fullMenu = menusMap.get(menuId);

        if (fullMenu != null){
            Menu surchargeInfo = new Menu();
            surchargeInfo.setPremiumLevelSurcharge(fullMenu.getPremiumLevelSurcharge());
            surchargeInfo.setLuxeLevelSurcharge(fullMenu.getLuxeLevelSurcharge());
            return surchargeInfo;
        }

        return null;
    }


}
