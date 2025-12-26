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

public class AllergenFSDao implements AllergenDao{


    private static final String CLIENT_ALLERGIES_FILE_PATH = "/com/example/bitebook/FileSystem/ClientsAllergies.csv";
    private static final String CSV_DELIMITER = ",";

    public List<Allergen> getClientAllergies(Client client) throws FailedSearchException {
        List<Allergen> clientAllergens = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(CLIENT_ALLERGIES_FILE_PATH)) {
            // 1. Uscita anticipata se il file non esiste
            if (is == null) {
                return clientAllergens;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                // 2. Controllo header
                if (br.readLine() == null) {
                    throw new FailedSearchException("Client allergies database file is empty or corrupted");
                }

                String line;
                while ((line = br.readLine()) != null) {
                    // 3. Clausola di guardia: Salta le righe vuote
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] fields = line.split(CSV_DELIMITER);

                    // 4. Clausola di guardia: Salta le righe malformate
                    if (fields.length < 3) {
                        continue;
                    }

                    try {
                        int fileClientId = Integer.parseInt(fields[0].trim());

                        // 5. Clausola di guardia: Salta se non è il cliente che cerchiamo
                        if (fileClientId != client.getId()) {
                            continue;
                        }

                        // Se siamo arrivati qui, è la riga giusta
                        int allergenId = Integer.parseInt(fields[1].trim());
                        String allergenName = fields[2].trim();
                        clientAllergens.add(new Allergen(allergenId, allergenName));

                    } catch (NumberFormatException e) {
                        throw new FailedSearchException("Error parsing numbers in CSV file", e);
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            // Gestione unificata delle eccezioni simili se l'azione da intraprendere è la stessa
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


}
