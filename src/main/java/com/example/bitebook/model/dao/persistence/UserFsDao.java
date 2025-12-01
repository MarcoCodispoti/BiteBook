package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.enums.SpecializationType;

import javax.security.auth.login.FailedLoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserFsDao implements UserDao{

    private final String USERS_FILE_PATH = "/com/example/bitebook/Users.csv";
    private final String CLIENT_ALLERGIES_FILE_PATH = "/com/example/bitebook/ClientsAllergies.csv";
    private final String CHEF_SPECIALIZATIONS_FILE_PATH = "/com/example/bitebook/ChefsSpecializations.csv";

    private final String CSV_DELIMITER = ",";

    @Override
    public Role getCredentialsRole(String email, String password) throws FailedLoginException {

        try (
                InputStream is = getClass().getResourceAsStream(USERS_FILE_PATH);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))
        ){
            if (is == null) {
                System.err.println("ERRORE CRITICO: Risorsa file non trovata nel Classpath: " + USERS_FILE_PATH);
                throw new FailedLoginException("Errore di sistema: file utenti non accessibile.");
            }
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CSV_DELIMITER);
                if (fields.length < 6) continue;
                String dbEmail = fields[3].trim();
                String dbPassword = fields[4].trim();

                if (dbEmail.equals(email) && dbPassword.equals(password)) {
                    try {
                        System.out.println("Utente " + email + " autenticato tramite FS.");
                        return Role.fromString(fields[5].trim());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Errore di mappatura Role: Valore '" + fields[5].trim() + "' non valido nel CSV.");
                        throw new FailedLoginException("Dati utente corrotti nel file di sistema.");
                    }
                }
            }
            // Se il ciclo finisce, le credenziali non sono state trovate nel file
            System.out.println("Tentativo di accesso fallito per: " + email);
            throw new FailedLoginException("Credenziali non valide.");

        } catch (IOException e) {
            System.err.println("ERRORE I/O: Fallimento della lettura del file utenti. " + e.getMessage());
            throw new FailedLoginException("Errore di sistema nella lettura dei dati utente.");
        }
    }

    public Client getClientInfo(String email, String password){
        Client client = null;
        try{
            InputStream is = getClass().getResourceAsStream(USERS_FILE_PATH);
            System.out.println("Ho preso il file");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("Ho caricato il file");
            if(is == null) {
                System.err.println("ERRORE CRITICO: Risorsa file non trovata nel Classpath: " + USERS_FILE_PATH);
                return client = null;
            }
            System.out.println("Ho aperto il file: " + is);
            br.readLine();
            System.out.println("Ho letto la prima riga");
            String line;
            System.out.println("Vado a scorrere tutte le righe del file per trovare una corrispondenza");
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CSV_DELIMITER);
                String fileEmail = fields[3].trim();
                String filePassword = fields[4].trim();
                String fileRole = fields[5].trim();
                if(fileEmail.equals(email) && filePassword.equals(password) && fileRole.equals(Role.CLIENT.toString())){
                    System.out.println("Ho trovato una corrispondenza con l'utente nel file");
                    try {
                        int id = Integer.parseInt(fields[0].trim());
                        String name = fields[1].trim();
                        String surname = fields[2].trim();
                        client = new Client(id, name, surname);
                        client.setEmail(email);
                        client.setPassword(password);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Errore durante la gestione delle stringhe");
                        // throw new Exception
                        return client;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERRORE I/O: Fallimento della lettura del file utenti. " + e.getMessage());
            return client = null;
        }
        System.out.println("I dati del client sono: " + client.getId() + " " + client.getEmail() + " " + client.getPassword() + " " + client.getName() + " " + client.getSurname());
        System.out.println("Fino a qui funziona bene"); // ora devo prendere le allergie
        try {
            client.setAllergies(getClientAllergies(client.getId()));
        } catch (IOException e){
            System.out.println("Errore nell'ottenere le allergie: Accesso negato per sicurezza");
            return client =  null;
        }
        return client;
    }






    public Chef getChefInfo(String email, String password){
        Chef chef = null;

        try{
            InputStream is = getClass().getResourceAsStream(USERS_FILE_PATH);
            System.out.println("Ho preso il file");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("Ho caricato il file");
            if(is == null) {
                System.err.println("ERRORE CRITICO: Risorsa file non trovata nel Classpath: " + USERS_FILE_PATH);
                return chef = null;
            }
            System.out.println("Ho aperto il file: " + is);
            br.readLine();
            System.out.println("Ho letto la prima riga");
            String line;
            System.out.println("Vado a scorrere tutte le righe del file per trovare una corrispondenza");
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CSV_DELIMITER);
                String fileEmail = fields[3].trim();
                String filePassword = fields[4].trim();
                String fileRole = fields[5].trim();

                if(fileEmail.equals(email) && filePassword.equals(password) && fileRole.equals(Role.CHEF.toString()) && !(fields[5].trim()).equals("NULL") && !(fields[6].trim()).equals("NULL")){
                    System.out.println("Ho trovato una corrispondenza con lo chef nel file");
                    try{
                        int id = Integer.parseInt(fields[0].trim());
                        String name = fields[1].trim();
                        String surname = fields[2].trim();
                        String city = fields[6].trim();
                        CookingStyle style = CookingStyle.fromString(fields[7].trim());
                        chef = new Chef(id, name, surname,style,city);
                        chef.setEmail(email);
                        chef.setPassword(password);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Errore durante la gestione delle stringhe");
                        // throw new Exception
                        return chef;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERRORE I/O: Fallimento della lettura del file utenti. " + e.getMessage());
            return chef = null;
        }
        System.out.println("I dati dello client sono: " + chef.getId() + " " + chef.getEmail() + " " + chef.getPassword() + " " + chef.getName() + " " + chef.getSurname());
        System.out.println("Fino a qui funziona bene"); // ora devo prendere le allergie

        try {
            chef.setSpecializations(getChefSpecializations(chef.getId()));
        } catch (IOException e){
            System.out.println("Errore nell'ottenere le specializzazioni");
        }
        // Dovrei aggiungere anche i menu ma è un casino e non servirebbe nell'app
        chef.setOfferedMenus(null);
        return chef;
    };





    private List<Allergen> getClientAllergies(int id) throws IOException{

        List<Allergen> allergies;
        try{
            InputStream is = getClass().getResourceAsStream(CLIENT_ALLERGIES_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("Ho caricato il file");
            if(is == null) {
                System.err.println("ERRORE CRITICO: Risorsa file non trovata nel Classpath: " + USERS_FILE_PATH);
                throw new IOException("Fallimento della lettura del file utenti.");
            }
            System.out.println("Ho aperto il file: " + is);
            br.readLine();
            System.out.println("Ho letto la prima riga e la salto");
            String line;
            System.out.println("Vado a scorrere tutte le righe del file per trovare una corrispondenza");
            allergies = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CSV_DELIMITER);
                int fileUserId = Integer.parseInt(fields[0].trim());
                int fileAllergenId = Integer.parseInt(fields[1].trim());
                String fileAllergenName = fields[2].trim();
                if(fileUserId == id){
                    Allergen allergy = new Allergen(fileAllergenId,fileAllergenName);
                    allergies.add(allergy);
                }
            }
        } catch (IOException e) {
            System.err.println("ERRORE I/O: Fallimento della lettura del file utenti. " + e.getMessage());
            throw new IOException();
        }
        return allergies;
    }

    private List<SpecializationType> getChefSpecializations(int id) throws IOException{
        List<SpecializationType> specializations;
        try{
            InputStream is = getClass().getResourceAsStream(CHEF_SPECIALIZATIONS_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("Ho caricato il file");
            if(is == null) {
                System.err.println("ERRORE CRITICO: Risorsa file non trovata nel Classpath: " + CHEF_SPECIALIZATIONS_FILE_PATH);
                throw new IOException("Fallimento della lettura del file utenti.");
            }
            System.out.println("Ho aperto il file: " + is);
            br.readLine();
            System.out.println("Ho letto la prima riga e la salto");
            String line;
            System.out.println("Vado a scorrere tutte le righe del file per trovare una corrispondenza");
            specializations = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(CSV_DELIMITER);
                int fileChefId = Integer.parseInt(fields[0].trim());
                if(fileChefId == id){
                    SpecializationType fileSpecialization = SpecializationType.fromString(fields[1].trim());
                    specializations.add(fileSpecialization);
                }
            }
        } catch (IOException e) {
            System.err.println("ERRORE I/O: Fallimento della lettura del file utenti. " + e.getMessage());
            throw new IOException();
        }
        return specializations;
    }






}
