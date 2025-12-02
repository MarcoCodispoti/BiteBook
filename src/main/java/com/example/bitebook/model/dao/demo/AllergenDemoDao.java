package com.example.bitebook.model.dao.demo;

import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.AllergenDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllergenDemoDao implements AllergenDao{

    private static Map<Integer, Allergen> allAllergensMap = new HashMap<>();

    private static Map<Integer, List<Allergen>> clientAllergiesMap = new HashMap<>();

    @Override
    public List<Allergen> getClientAllergies(Client client) throws Exception {
        int clientId = client.getId();
        System.out.println("[Demo] Recupero allergie per client ID: " + clientId);

        // Cerchiamo nella mappa delle associazioni
        List<Allergen> foundAllergies = clientAllergiesMap.get(clientId);

        // Se non ha allergie (è null), restituiamo un vettore vuoto per evitare errori
        if (foundAllergies == null) {
            return new ArrayList<>();
        }

        // Restituiamo una copia per sicurezza (opzionale) o il vettore diretto
        return foundAllergies;
    }



    @Override
    public void removeClientAllergy(int clientId, int allergenId) throws FailedRemoveException{
        System.out.println("[Demo] Rimozione allergia " + allergenId + " per client " + clientId);

        // 1. Otteniamo la lista delle allergie del cliente
        List<Allergen> userAllergies = clientAllergiesMap.get(clientId);

        if (userAllergies != null) {
            Allergen toRemove = null;

            // 2. Ciclo classico per trovare l'allergene da rimuovere
            for (Allergen a : userAllergies) {
                if (a.getId() == allergenId) {
                    toRemove = a;
                    break; // Trovato, usciamo dal ciclo
                }
            }

            // 3. Se l'abbiamo trovato, lo rimuoviamo dal vettore
            if (toRemove != null) {
                userAllergies.remove(toRemove);
                System.out.println("[Demo] Allergia rimossa con successo.");
            }
        }
    }



    @Override
    public List<Allergen> getAllergens() throws FailedSearchException {
        System.out.println("[Demo] Recupero tutte le allergie di sistema");

        List<Allergen> allAllergensList = new ArrayList<>();

        // Ciclo classico sui valori della Mappa globale
        for (Allergen a : allAllergensMap.values()) {
            allAllergensList.add(a);
        }
         return allAllergensList;

        // Dummy -> Si usa sempre il databae per i dati che voglio solo andare a leggere
        // e.c. -> Devo andare ad inserire manualmente tutti i dati in nella mia Mappa

        // return null;
    }

    @Override
    public void insertAllergy(Allergen allergen, int clientId) throws FailedInsertException {
        System.out.println("[Demo] Aggiunta allergia " + allergen.getName() + " a client " + clientId);

        // 1. Controlla se il cliente ha già una lista di allergie
        List<Allergen> userAllergies = clientAllergiesMap.get(clientId);

        // 2. Se non esiste la lista, creala
        if (userAllergies == null) {
            userAllergies = new ArrayList<>();
            clientAllergiesMap.put(clientId, userAllergies);
        }

        // 3. Controlla se l'allergia è già presente per evitare duplicati (Simula Primary Key composta)
        boolean exists = false;
        for (Allergen a : userAllergies) {
            if (a.getId() == allergen.getId()) {
                exists = true;
                break;
            }
        }

        // 4. Se non c'è, aggiungila
        if (!exists) {
            userAllergies.add(allergen);
            System.out.println("[Demo] Allergia inserita.");
        } else {
            System.out.println("[Demo] Allergia già presente per questo utente.");
        }
    }



    // 1. SIMULAZIONE TABELLA DI UNIONE "AllergensDish"
    // Key: DishID (l'ID del piatto)
    // Value: List<Allergen> (la lista delle allergie di quel piatto)
    private static Map<Integer, List<Allergen>> dishAllergensMap = new HashMap<>();

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
        List<Allergen> allergens1001 = new ArrayList<>();
        allergens1001.add(glutine);
        allergens1001.add(uova);
        allergens1001.add(lattosio);

        dishAllergensMap.put(1001, allergens1001);

        // --- PIATTO 1002: "Saltimbocca 2.0" (dal Menu 210) ---
        // Contiene: Glutine (infarinatura)
        List<Allergen> allergens1002 = new ArrayList<>();
        allergens1002.add(glutine);

        dishAllergensMap.put(1002, allergens1002);

        // --- PIATTO 2001: "Linguine allo scoglio" (dal Menu 300) ---
        // Contiene: Glutine (pasta), Crostacei
        List<Allergen> allergens2001 = new ArrayList<>();
        allergens2001.add(glutine);
        allergens2001.add(crostacei);

        dishAllergensMap.put(2001, allergens2001);

        // --- PIATTO 2002: "Babà al rum" (dal Menu 300) ---
        // Contiene: Glutine, Lattosio, Uova
        List<Allergen> allergens2002 = new ArrayList<>();
        allergens2002.add(glutine);
        allergens2002.add(lattosio);
        allergens2002.add(uova);

        dishAllergensMap.put(2002, allergens2002);
    }



    @Override
    public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException{

        List<Allergen> allergens = dishAllergensMap.get(dishId);

        if (allergens != null) {
            // Trovato! Restituiamo la lista
            return allergens;
        } else {
            // Se il piatto non ha allergie (o l'ID non esiste nella mappa allergie),
            // restituiamo un vettore vuoto invece di null, così non crasha l'interfaccia.
            // È un comportamento sicuro.
            return new ArrayList<>();
        }
    }

}
