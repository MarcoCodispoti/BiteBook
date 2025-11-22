package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.WrongCredentialsExcpetion;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.bean.LoginBean;
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.dao.persistence.UserDbDao;
import com.example.bitebook.model.dao.persistence.UserFsDao;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.singleton.LoggedUser;
import com.example.bitebook.util.AppConfig;
import com.example.bitebook.util.Connector;

import javax.security.auth.login.FailedLoginException;
import java.sql.Connection;

import static com.example.bitebook.model.enums.Role.CHEF;
import static com.example.bitebook.model.enums.Role.CLIENT;

public class LoginController{

    public Role role = null;

    public void authenticate(LoginBean loginBean) throws FailedLoginException, WrongCredentialsExcpetion {

//
//        boolean databaseIsConnected = false;
//        Client client = null;
//        Chef chef = null;
//        Connection conn;
//
//        try {
//            conn = Connector.getInstance().getConnection();
//        }  catch (FailedDatabaseConnectionException e) {
//            conn = null;
//        }
//
//        if(conn != null){
//            databaseIsConnected = true;
//        }
//
//        if(databaseIsConnected) {
//            try {
//                System.out.println("Vado a istaurare la connessione");
////            conn = Connector.getInstance().getConnection();
//                System.out.println("Ho preso la connessione e vado a prendere il ruolo dal DAO");
//                UserDbDao userDao = new UserDbDao();
//                System.out.println("Vado a prendere il ruolo dal DAO");
//                role = userDao.getCredentialsRole(loginBean.getEmail(), loginBean.getPassword());
//                System.out.println("Ho preso il ruolo dal DAO");
//
//            } catch (FailedDatabaseConnectionException e) {
//                // Da gestire
//            } catch (FailedLoginException e) {
//                System.out.println("Impossibile ottenere Ruolo dal database");
//                throw new WrongCredentialsExcpetion(e.getMessage());
//            }
//        } else{
//            System.out.println("Impossibile ottenere Ruolo dal database, uso il Dao del File System");
//            UserFsDao userDao = new UserFsDao();
//            System.out.println("Ho creato il dao del FS");
//            role = userDao.getCredentialsRole(loginBean.getEmail(), loginBean.getPassword());
//            System.out.println("Ho preso il ruolo dal DAO FS, ruolo: " + role);
//
//        }
//
//
//
//        System.out.println("Ruolo dell'utente: " + role );
//
//
//        if(role == CLIENT){
//            client = new Client();
//
//            if(databaseIsConnected){
//                UserDbDao userDao = new UserDbDao();    // da implementare la funzione dao DB
//                client = userDao.getClientInfo(loginBean.getEmail(),loginBean.getPassword());
//                AllergenDao allergenDao = DaoFactory.getAllergenDao();
//                try{
//                    client.setAllergies(allergenDao.getClientAllergies(client));
//                } catch (Exception e){
//                    System.out.println(e.getMessage() + "errore nel reperire le allergie");
//                }
//            } else{
//                System.out.println("Database non connesso, cerco l'utente nel file system");
//                UserFsDao userDao = new UserFsDao();       // da Implementare la funzione dao FS
//                System.out.println("Vado a prendere le informazioni del client nel FS");
//                client = userDao.getClientInfo(loginBean.getEmail(),loginBean.getPassword());
////                AllergenDao allergenDao = DaoFactory.getAllergenDao();
////                try {
////                    client.setAllergies(allergenDao.getClientAllergies(client));
////                } catch (Exception e){
////                    System.out.println(e.getMessage() + "errore nel reperire le allergie");
////                }
//                System.out.println("Ho restituito un client dal FS");
//            }
//
//        } else if(role == CHEF){
//            chef = new Chef();
//
//            System.out.println("Searching a chef in the database");
//
//            if(databaseIsConnected){
//                UserDbDao userDao = new UserDbDao();
//                chef = userDao.getChefInfo(loginBean.getEmail(),loginBean.getPassword());
//            } else{
//                UserFsDao userDao = new UserFsDao();       // da implementare la funzione Dao FS
//                chef = userDao.getChefInfo(loginBean.getEmail(),loginBean.getPassword());
//            }
//        }
//
//        if( (role == CLIENT && client == null) || (role == CHEF && chef == null) || role == null){
//            System.out.println("Impossibile ottenere L'informazioni dell'utente " + role + " dal database");
//            throw new FailedLoginException();
//        }
//
//        if(role == CHEF) {
//            System.out.println("Chef details: " + chef.getId() + " " + chef.getEmail() + " " + chef.getPassword() + " " + chef.getName() + " " + chef.getSurname() + " " + chef.getStyle() + " " + chef.getCity());
//            System.out.println("Specializzazioni: " + chef.getSpecializations());
//        }else if(role == CLIENT) {
//            System.out.println("Chef details: " + client.getId() + " " + client.getEmail() + " " + client.getPassword() + " " + client.getName() + " " + client.getSurname());
//            System.out.println("Allergie: " + client.GetAllergiesAsString());
//        }
//        LoggedUser.logout();
//
//        if(role == CLIENT){
//            LoggedUser.getInstance().setClient(client);
//            LoggedUser.getInstance().setRole(role);
//        } else if(role == CHEF){
//            LoggedUser.getInstance().setChef(chef);
//            LoggedUser.getInstance().setRole(role);
//        } else {
//            throw new FailedLoginException();
//        }
//    }

        Role role = null;
        Client client = null;
        Chef chef = null;

        try {
            // 1. CHIEDIAMO IL DAO ALLA FACTORY
            // Qui avviene la magia: la Factory controlla da sola se usare Demo, DB o FileSystem.
            // Il controller non deve più preoccuparsi di "databaseIsConnected".
            UserDao userDao = DaoFactory.getUserDao();

            // 2. RECUPERIAMO IL RUOLO
            System.out.println("Richiesta ruolo per: " + loginBean.getEmail());
            role = userDao.getCredentialsRole(loginBean.getEmail(), loginBean.getPassword());
            System.out.println("Ruolo ottenuto: " + role);

            // 3. CARICHIAMO IL PROFILO IN BASE AL RUOLO
            if (role == CLIENT) {
                // Recupera info base (funziona sia con DB che FS che Demo)
                client = userDao.getClientInfo(loginBean.getEmail(), loginBean.getPassword());

                if (client != null) {
                    // Recupera le allergie
                    // Anche qui, chiediamo alla factory il DAO corretto per le allergie
                    AllergenDao allergenDao = DaoFactory.getAllergenDao();
                    try {
                        // Nota: passiamo l'ID (o l'oggetto client intero se il tuo metodo lo richiede)
                        client.setAllergies(allergenDao.getClientAllergies(client));
                    } catch (Exception e) {
                        System.out.println("Warning: Impossibile caricare allergie (Database offline o errore demo).");
                    }
                }

            } else if (role == CHEF) {
                // Recupera info chef
                chef = userDao.getChefInfo(loginBean.getEmail(), loginBean.getPassword());
            }

            // 4. VALIDAZIONE FINALE
            // Se abbiamo trovato il ruolo ma non siamo riusciti a caricare il profilo (es. errore DB parziale)
            if ((role == CLIENT && client == null) || (role == CHEF && chef == null)) {
                throw new FailedLoginException("Ruolo trovato ma profilo utente mancante/nullo.");
            }

            // 5. SALVATAGGIO IN SESSIONE (SINGLETON)
            LoggedUser.logout(); // Pulisce sessioni precedenti

            if (role == CLIENT) {
                LoggedUser.getInstance().setClient(client);
                System.out.println("Login effettuato: CLIENTE " + client.getName());
            } else {
                LoggedUser.getInstance().setChef(chef);
                System.out.println("Login effettuato: CHEF " + chef.getName());
            }

            // Imposta il ruolo nel Singleton
            LoggedUser.getInstance().setRole(role);

        } catch (FailedLoginException e) {
            // Rilanciamo come WrongCredentials per la GUI (Username/pass errati)
            System.out.println("Login fallito: " + e.getMessage());
            throw new WrongCredentialsExcpetion("Email o Password errati");

        } catch (Exception e) {
            // Errore generico di sistema (es. Database giù e FS non disponibile)
            e.printStackTrace();
            throw new FailedLoginException("Errore di sistema durante il login");
        }
    }


    public void logout(){
        LoggedUser.logout();
    }
}
