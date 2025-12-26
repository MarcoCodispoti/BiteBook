package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.AllergenDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S1075")
public class AllergenFSDao implements AllergenDao{


    private static final String CLIENT_ALLERGIES_FILE_PATH = "/com/example/bitebook/FileSystem/ClientsAllergies.csv";
    private static final String CSV_DELIMITER = ",";



    public List<Allergen> getClientAllergies(Client client) throws FailedSearchException {
        List<Allergen> clientAllergens = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(CLIENT_ALLERGIES_FILE_PATH)) {
            if (is == null) {
                return clientAllergens;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String header = br.readLine(); // Assegni il valore a una variabile
                if (header == null) {
                    throw new FailedSearchException("Client allergies database file is empty or corrupted");
                }

                String line;
                while ((line = br.readLine()) != null) {
                    Allergen allergen = processLine(line, client.getId());

                    if (allergen != null) {
                        clientAllergens.add(allergen);
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new FailedSearchException("Error accessing or reading the CSV file", e);
        }

        return clientAllergens;
    }



    // dummy method
    @Override
    public void removeClientAllergy(int clientId, int allergenId) throws FailedRemoveException {
        // dummy
        throw new FailedRemoveException("Dummy method involved", new Exception());
    }



    // dummy method
    @Override
    public List<Allergen> getAllergens() throws FailedSearchException {
        return List.of();
    }



    // dummy method
    @Override
    public void insertAllergy(Allergen allergen, int clientId) throws FailedInsertException {
        throw new FailedInsertException("Dummy method involved", new Exception());
    }



    // dummy method
    @Override
    public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException {
        return List.of();
    }



    private Allergen processLine(String line, int targetClientId) throws FailedSearchException {
        if (line.trim().isEmpty()) {
            return null;
        }

        String[] fields = line.split(CSV_DELIMITER);

        if (fields.length < 3) {
            return null;
        }

        try {
            int fileClientId = Integer.parseInt(fields[0].trim());

            if (fileClientId == targetClientId) {
                int allergenId = Integer.parseInt(fields[1].trim());
                String allergenName = fields[2].trim();
                return new Allergen(allergenId, allergenName);
            }
            return null;

        } catch (NumberFormatException e) {
            throw new FailedSearchException("Error recovering client allergies info in the CSV file", e);
        }
    }


}
