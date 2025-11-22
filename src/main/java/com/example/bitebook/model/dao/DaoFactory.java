package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.ServiceRequest;
import com.example.bitebook.model.dao.demo.*;
import com.example.bitebook.model.dao.persistence.*;
import com.example.bitebook.util.AppConfig;
import com.example.bitebook.util.Connector;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DaoFactory{


    public static UserDao getUserDao() {

        // ---------------------------------------------------------
        // 1. PRIORITÀ ASSOLUTA: MODALITÀ DEMO
        // ---------------------------------------------------------
        if (AppConfig.getInstance().isDemoMode()) {
            System.out.println("[Factory] Richiesto UserDao in modalità DEMO");
            return new UserDemoDao();
        }

        // ---------------------------------------------------------
        // 2. TENTATIVO PERSISTENZA: DATABASE
        // ---------------------------------------------------------
        try {
            // Proviamo a ottenere la connessione.
            // Se il DB è spento o irraggiungibile, questo metodo lancerà l'eccezione
            Connection conn = Connector.getInstance().getConnection();

            // Se siamo qui, la connessione c'è!
            // (Opzionale: puoi controllare if (conn != null && !conn.isClosed()))

            System.out.println("[Factory] Connessione DB riuscita. Restituisco UserDbDao.");
            return new UserDbDao();

        } catch (FailedDatabaseConnectionException e) {
            // ---------------------------------------------------------
            // 3. FALLBACK: FILE SYSTEM
            // ---------------------------------------------------------
            // Se la connessione fallisce (entriamo nel catch),
            // attiviamo il piano B: File System.

            System.out.println("[Factory] Database non raggiungibile (" + e.getMessage() + ").");
            System.out.println("[Factory] Fallback attivo: Restituisco UserFsDao.");
            return new UserFsDao();
        }
    }



    public static ServiceRequestDao getServiceRequestDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new ServiceRequestDemoDao();
        } else{
            return new ServiceRequestDbDao();
        }
    };


    public static AllergenDao getAllergenDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new AllergenDemoDao();
        } else{
            return new AllergenDbDao();
        }
    };


    public static ChefDao getChefDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new ChefDemoDao();
        } else{
            return new ChefDbDao();
        }
    };


    public static MenuDao getMenuDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new MenuDemoDao();
        } else{
            return new MenuDbDao();
        }
    };


    public static DishDao getDishDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new DishDemoDao();
        } else{
            return new DishDbDao();
        }
    }





    // In questa classe vanno anche tutte le altre getClasseDao
    // Questo mi garantisce flessibilità nel momento in cui vado a usare le Dao per la demo, cioè, dovrei fare una cosa del tipo:
//    public static UserDao getUserDao() {
//        if (AppConfig.getInstance().isDemoMode()) {
//            // return new UserDemoDao();
//            return null; // Da implementare
//        } else {
//            // return new UserDbDao();
//            return null; // Da implementare
//        }
//    }
//
//    /**
//     * Ottiene il DAO per Menu.
//     */
//    public static MenuDao getMenuDao() {
//        if (AppConfig.getInstance().isDemoMode()) {
//            // return new MenuDemoDao();
//            return null; // Da implementare
//        } else {
//            // return new MenuDbDao();
//            return null; // Da implementare
//        }
//    }






}
