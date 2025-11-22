package com.example.bitebook.model.dao.demo;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.AllergenDao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AllergenDemoDao implements AllergenDao{

    private static Map<Integer, Allergen> allAllergensMap = new HashMap<>();

    private static Map<Integer, Vector<Allergen>> clientAllergiesMap = new HashMap<>();

    @Override
    public Vector<Allergen> getClientAllergies(Client client) throws Exception {
        int clientId = client.getId();
        System.out.println("[Demo] Recupero allergie per client ID: " + clientId);

        // Cerchiamo nella mappa delle associazioni
        Vector<Allergen> foundAllergies = clientAllergiesMap.get(clientId);

        // Se non ha allergie (è null), restituiamo un vettore vuoto per evitare errori
        if (foundAllergies == null) {
            return new Vector<>();
        }

        // Restituiamo una copia per sicurezza (opzionale) o il vettore diretto
        return foundAllergies;
    }



    @Override
    public void removeClientAllergy(int clientId, int allergenId) throws Exception {
        System.out.println("[Demo] Rimozione allergia " + allergenId + " per client " + clientId);

        // 1. Otteniamo la lista delle allergie del cliente
        Vector<Allergen> userAllergies = clientAllergiesMap.get(clientId);

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
    public Vector<Allergen> getAllergens() throws Exception {
//        System.out.println("[Demo] Recupero tutte le allergie di sistema");
//
//        Vector<Allergen> allAllergensVector = new Vector<>();
//
//        // Ciclo classico sui valori della Mappa globale
//        for (Allergen a : allAllergensMap.values()) {
//            allAllergensVector.add(a);
//        }
//         return allAllergensVector;

        // Dummy -> Si usa sempre il databae per i dati che voglio solo andare a leggere
        // e.c. -> Devo andare ad inserire manualmente tutti i dati in nella mia Mappa

        return null;
    }

    @Override
    public void insertAllergy(Allergen allergen, int clientId) throws SQLException {
        System.out.println("[Demo] Aggiunta allergia " + allergen.getName() + " a client " + clientId);

        // 1. Controlla se il cliente ha già una lista di allergie
        Vector<Allergen> userAllergies = clientAllergiesMap.get(clientId);

        // 2. Se non esiste la lista, creala
        if (userAllergies == null) {
            userAllergies = new Vector<>();
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
}
