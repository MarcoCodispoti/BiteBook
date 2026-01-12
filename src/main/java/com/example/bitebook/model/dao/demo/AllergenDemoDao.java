package com.example.bitebook.model.dao.demo;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.AllergenDao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllergenDemoDao implements AllergenDao{


    private static final Map<Integer, Allergen> allAllergensMap = new HashMap<>();
    private static final Map<Integer, List<Allergen>> clientAllergiesMap = new HashMap<>();
    private static final Map<Integer, List<Allergen>> dishAllergensMap = new HashMap<>();


    static {
        Allergen gluten = new Allergen(1, "Gluten");
        Allergen lactose = new Allergen(2, "Lactose");
        Allergen crustaceans = new Allergen(3, "Crustaceans");
        Allergen egg = new Allergen(4, "Egg");
        Allergen peanuts = new Allergen(5, "peanuts");

        allAllergensMap.put(1, gluten);
        allAllergensMap.put(2, lactose);
        allAllergensMap.put(3, crustaceans);
        allAllergensMap.put(4, egg);
        allAllergensMap.put(5, peanuts);

        List<Allergen> a1001 = new ArrayList<>();
        a1001.add(gluten); a1001.add(egg); a1001.add(lactose);
        dishAllergensMap.put(1001, a1001);


        List<Allergen> a1002 = new ArrayList<>();
        a1002.add(gluten);
        dishAllergensMap.put(1002, a1002);

    }



    @Override
    public List<Allergen> getClientAllergies(Client client){
        List<Allergen> found = clientAllergiesMap.get(client.getId());
        return found != null ? new ArrayList<>(found) : new ArrayList<>();
    }



    @Override
    public void removeClientAllergy(int clientId, int allergenId){
        List<Allergen> userAllergies = clientAllergiesMap.get(clientId);
        if (userAllergies != null) {
            userAllergies.removeIf(a -> a.getId() == allergenId);
        }
    }



    @Override
    public List<Allergen> getAllergens(){
        return new ArrayList<>(allAllergensMap.values());
    }



    @Override
    public void insertAllergy(Allergen allergen, int clientId){
        List<Allergen> userAllergies = clientAllergiesMap.computeIfAbsent(clientId, _ -> new ArrayList<>());

        boolean exists = userAllergies.stream().anyMatch(a -> a.getId() == allergen.getId());
        if (!exists) {
            userAllergies.add(allergen);
        }
    }



    @Override
    public List<Allergen> getDishAllergens(int dishId){
        List<Allergen> allergens = dishAllergensMap.get(dishId);
        return allergens != null ? new ArrayList<>(allergens) : new ArrayList<>();
    }


}
