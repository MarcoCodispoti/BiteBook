package com.example.bitebook.util;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector{
    private static Connector instance = null;
    private Connection conn = null;


    private Connector() throws FailedDatabaseConnectionException {
        System.out.println("Vado a inizializzare la nuova connessione");
        try {
            initializeConnection();
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedDatabaseConnectionException(e.getMessage());
        }
    }


//    private Connector() throws FailedDatabaseConnectionException {
//        System.out.println("Vado a inizializzare la nuova connessione");
//        initializeConnection();
//    }

    private void initializeConnection() throws FailedDatabaseConnectionException {
        try{
            InputStream input = Connector.class.getResourceAsStream("/com/example/bitebook/db.properties");
            if(input == null){
                System.out.println("Non sono riuscito a prendere il file db.properties");
                throw new FailedDatabaseConnectionException("db.properties files not found");
            }
            System.out.println("Ho preso il file db.properties");

            Properties properties = new Properties();
            System.out.println("Vado a caricare le properties");
            properties.load(input);
            System.out.println("Ho caricato le properties");

            String connectionURL = properties.getProperty("CONNECTION_URL");
            String username = properties.getProperty("LOGIN_USERNAME");
            String password = properties.getProperty("LOGIN_PASSWORD");

            System.out.println("Vado instaurare la connessione con le proprieta carica");
            conn = DriverManager.getConnection(connectionURL,username,password);
            System.out.println("Connessione inizializzata");
        } catch (Exception e) {
            // e.printStackTrace(); e.getMessage();
            throw new FailedDatabaseConnectionException("Error while connecting to database");
        }
    }

    public static Connector getInstance() throws FailedDatabaseConnectionException {
        try {
            if (instance == null) {
                System.out.println("L'istanza della connessione non c'è, la vado a creare nuova");
                instance = new Connector();
                System.out.println("Ho creato la nuova istanza della connessione");
            }
        }  catch (FailedDatabaseConnectionException e) {
            throw new FailedDatabaseConnectionException(e.getMessage());
        }
        return instance;
    }


//    public static Connector getInstance() throws FailedDatabaseConnectionException {
//        if (instance == null) {
//            System.out.println("L'istanza della connessione non c'è, la vado a creare nuova");
//            instance = new Connector();
//            System.out.println("Ho creato la nuova istanza della connessione");
//        }
//
//        return instance;
//    }


    public Connection getConnection() throws FailedDatabaseConnectionException{
        try {
            if (conn == null || conn.isClosed()) {
                initializeConnection();
            }
            // System.out.println("Ho restituito la connessione");
            return conn;
        } catch (SQLException e) {
            throw new FailedDatabaseConnectionException("Unable to establish connection to database");
        }

    }


}
