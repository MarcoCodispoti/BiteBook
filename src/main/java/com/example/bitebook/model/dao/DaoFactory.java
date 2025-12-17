package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.dao.demo.*;
import com.example.bitebook.model.dao.persistence.*;
import com.example.bitebook.util.AppConfig;
import com.example.bitebook.util.Connector;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoFactory{


    private static final Logger logger = Logger.getLogger(DaoFactory.class.getName());


    private DaoFactory(){
        // Default constructor
    }



    public static UserDao getUserDao() {

        if (AppConfig.getInstance().isDemoMode()){
            return new UserDemoDao();
        }

        try {
            Connector.getInstance().getConnection();
            return new UserDbDao();
        } catch (FailedDatabaseConnectionException _){
            logger.log(Level.INFO, "Database connection error: Login via File System");
            return new UserFsDao();
        }

    }



    public static ServiceRequestDao getServiceRequestDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new ServiceRequestDemoDao();
        }
        try{
            Connector.getInstance().getConnection();
            return new ServiceRequestDbDao();
        }  catch (FailedDatabaseConnectionException _){
            // Dummy fallback on demoDao -> Allow only login without app crash
            return new ServiceRequestDemoDao();
        }
    }



    public static AllergenDao getAllergenDao() {
        if (AppConfig.getInstance().isDemoMode()) {
            return new AllergenDemoDao();
        }
        try {
            Connector.getInstance().getConnection();
            return new AllergenDbDao();

        } catch (FailedDatabaseConnectionException _){
            // Dummy fallback on demoDao -> Allow only login without app crash
            return new AllergenDemoDao();
        }
    }



    public static ChefDao getChefDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new ChefDemoDao();
        } else{
            return new ChefDbDao();
        }
    }



    public static MenuDao getMenuDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new MenuDemoDao();
        } else{
            return new MenuDbDao();
        }
    }



    public static DishDao getDishDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new DishDemoDao();
        } else{
            return new DishDbDao();
        }
    }


}