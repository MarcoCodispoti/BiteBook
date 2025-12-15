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
        Allergen glutine = new Allergen(1, "Glutine");
        Allergen lattosio = new Allergen(2, "Lattosio");
        Allergen crostacei = new Allergen(3, "Crostacei");
        Allergen uova = new Allergen(4, "Uova");
        Allergen arachidi = new Allergen(5, "Arachidi");

        allAllergensMap.put(1, glutine);
        allAllergensMap.put(2, lattosio);
        allAllergensMap.put(3, crostacei);
        allAllergensMap.put(4, uova);
        allAllergensMap.put(5, arachidi);

        List<Allergen> a1001 = new ArrayList<>();
        a1001.add(glutine); a1001.add(uova); a1001.add(lattosio);
        dishAllergensMap.put(1001, a1001);


        List<Allergen> a1002 = new ArrayList<>();
        a1002.add(glutine);
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
