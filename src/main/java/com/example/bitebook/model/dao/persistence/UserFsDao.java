package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.enums.SpecializationType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserFsDao implements UserDao{

    String CHEF_SPECIALIZATIONS_FILE_PATH = "/com/example/bitebook/ChefsSpecializations.csv";
    private final String USERS_FILE_PATH = "/com/example/bitebook/Users.csv";
    private final String CSV_DELIMITER = ",";


    @Override
    public Role getCredentialsRole(String email, String password) throws WrongCredentialsException, FailedSearchException {
        try (InputStream is = getClass().getResourceAsStream(USERS_FILE_PATH)) {
            if (is == null) throw new FailedSearchException("File Users.csv non trovato");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                br.readLine(); // Salta header
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(CSV_DELIMITER);
                    if (fields.length < 6) continue;
                    String fileEmail = fields[3].trim();
                    String filePassword = fields[4].trim();
                    if (fileEmail.equals(email) && filePassword.equals(password)) {
                        try {
                            return Role.fromString(fields[5].trim());
                        } catch (IllegalArgumentException e) {
                            throw new FailedSearchException("Ruolo non valido nel CSV per utente: " + email);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new FailedSearchException("Errore lettura file utenti", e);
        }
        throw new WrongCredentialsException("Error finding user info with inserted credentials");
    }




    @Override
    public Client getClientInfo(String email, String password) throws FailedSearchException {
        try (InputStream is = getClass().getResourceAsStream(USERS_FILE_PATH)) {
            if (is == null) throw new FailedSearchException("File Users.csv non trovato");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                br.readLine(); // Header
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(CSV_DELIMITER);
                    if (fields.length < 6) continue;
                    if (isMatchingUser(fields, email, password, Role.CLIENT)) {
                        Client client = new Client(
                                Integer.parseInt(fields[0].trim()),
                                fields[1].trim(),
                                fields[2].trim()
                        );
                        client.setEmail(email);
                        client.setPassword(password);
                        return client;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new FailedSearchException("Errore recupero dati Cliente da File System", e);
        }
        return null;
    }



    @Override
    public Chef getChefInfo(String email, String password) throws FailedSearchException {
        try (InputStream is = getClass().getResourceAsStream(USERS_FILE_PATH)) {
            if (is == null) throw new FailedSearchException("File Users.csv non trovato");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(CSV_DELIMITER);
                    if (fields.length < 8) continue;
                    if (isMatchingUser(fields, email, password, Role.CHEF)) {
                        Chef chef = new Chef(
                                Integer.parseInt(fields[0].trim()),
                                fields[1].trim(),
                                fields[2].trim(),
                                CookingStyle.fromString(fields[7].trim()),
                                fields[6].trim()
                        );
                        chef.setEmail(email);
                        chef.setPassword(password);
                        chef.setSpecializations(getChefSpecializations(chef.getId()));
                        return chef;
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new FailedSearchException("Errore recupero dati Chef da File System", e);
        }
        return null;
    }


    private boolean isMatchingUser(String[] fields, String email, String pass, Role role) {
        return fields[3].trim().equals(email) &&
                fields[4].trim().equals(pass) &&
                fields[5].trim().equals(role.toString());
    }


    private List<SpecializationType> getChefSpecializations(int chefId) throws IOException {
        List<SpecializationType> specs = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(CHEF_SPECIALIZATIONS_FILE_PATH)) {
            if (is == null) return specs; // Lista vuota se file manca

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(CSV_DELIMITER);
                    if (fields.length < 2) continue;

                    int fileChefId = Integer.parseInt(fields[0].trim());
                    if (fileChefId == chefId) {
                        try {
                            specs.add(SpecializationType.fromString(fields[1].trim()));
                        } catch (IllegalArgumentException ignored) {
                            // Ignore
                        }
                    }
                }
            }
        }
        return specs;
    }

}
