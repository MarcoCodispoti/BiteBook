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
        initializeConnection();
    }


    private void initializeConnection() throws FailedDatabaseConnectionException {
        try(InputStream input = Connector.class.getResourceAsStream("/com/example/bitebook/db.properties")){
            if(input == null){
                throw new FailedDatabaseConnectionException("db.properties files not found");
            }
            Properties properties = new Properties();
            properties.load(input);

            String connectionURL = properties.getProperty("CONNECTION_URL");
            String username = properties.getProperty("LOGIN_USERNAME");
            String password = properties.getProperty("LOGIN_PASSWORD");

            conn = DriverManager.getConnection(connectionURL,username,password);
        } catch(Exception e) {
            throw new FailedDatabaseConnectionException("Error while connecting to database", e);
        }
    }

    public static Connector getInstance() throws FailedDatabaseConnectionException{
        if (instance == null){
            instance = new Connector();
        }
        return instance;
    }



    public Connection getConnection() throws FailedDatabaseConnectionException{
        try {
            if (conn == null || conn.isClosed()) {
                initializeConnection();
            }
            return conn;
        } catch (SQLException e) {
            throw new FailedDatabaseConnectionException("Unable to establish connection to database", e);
        }
    }


}
