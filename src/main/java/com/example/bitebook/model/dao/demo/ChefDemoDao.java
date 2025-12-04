package com.example.bitebook.model.dao.demo;

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

    private static final Map<Integer, Chef> chefsMap = new HashMap<>();

    private static final Map<Integer, Integer> menuToChefMap = new HashMap<>();

    static{
        Chef c1 = new Chef(1, "Alessandro", "Borghese", CookingStyle.MODERN, "Roma");
        List<SpecializationType> specs1 = new ArrayList<>();
        specs1.add(SpecializationType.SEAFOOD);
        specs1.add(SpecializationType.VEGAN);
        c1.setSpecializations(specs1);
        chefsMap.put(1, c1);


        Chef c2 = new Chef(2, "Antonino", "Cannavacciuolo", CookingStyle.CLASSIC, "Napoli");
        List<SpecializationType> specs2 = new ArrayList<>();
        specs2.add(SpecializationType.ITALIAN);
        specs2.add(SpecializationType.SEAFOOD);
        c2.setSpecializations(specs2);
        chefsMap.put(2, c2);


        Chef c3 = new Chef(3, "Carlo", "Cracco", CookingStyle.FUSION, "Milano");
        List<SpecializationType> specs3 = new ArrayList<>();
        specs3.add(SpecializationType.JAPANESE);
        c3.setSpecializations(specs3);
        chefsMap.put(3, c3);


        menuToChefMap.put(210, 1);
        menuToChefMap.put(300, 2);
    }


    @Override
    public void findCityChefs(String cityName) throws NoChefInCityException {
        boolean found = false;

        for (Chef c : chefsMap.values()) {
            if (c.getCity() != null && c.getCity().equalsIgnoreCase(cityName)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoChefInCityException(cityName);
        }
    }


    @Override
    public List<Chef> getChefsInCity(String cityName) {
        List<Chef> cityChefs = new ArrayList<>();
        for (Chef c : chefsMap.values()) {
            if (c.getCity() != null && c.getCity().equalsIgnoreCase(cityName)) {
                cityChefs.add(c);
            }
        }
        return cityChefs;
    }


    @Override
    public Chef getChefFromMenu(int menuId) {
        Integer chefId = menuToChefMap.get(menuId);

        if (chefId != null) {
            return chefsMap.get(chefId);
        }
        return null;
    }



}
