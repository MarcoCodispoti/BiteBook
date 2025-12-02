package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.dao.demo.*;
import com.example.bitebook.model.dao.persistence.*;
import com.example.bitebook.util.AppConfig;
import com.example.bitebook.util.Connector;

public abstract class DaoFactory{


    public static UserDao getUserDao() {

        if (AppConfig.getInstance().isDemoMode()) {
            System.out.println("[Factory] Richiesto UserDao in modalità DEMO");
            return new UserDemoDao();
        }
        try {
            Connector.getInstance().getConnection();
            return new UserDbDao();
        } catch (FailedDatabaseConnectionException e) {
            return new UserFsDao();
        }
    }



    public static ServiceRequestDao getServiceRequestDao(){
        if(AppConfig.getInstance().isDemoMode()){
            return new ServiceRequestDemoDao();
        } else{
            return new ServiceRequestDbDao();
        }
    }


//    public static AllergenDao getAllergenDao(){
//        if(AppConfig.getInstance().isDemoMode()){
//            return new AllergenDemoDao();
//        } else{
//            return new AllergenDbDao();
//        }
//    };

    public static AllergenDao getAllergenDao() {
        if (AppConfig.getInstance().isDemoMode()) {
            return new AllergenDemoDao();
        }
        try {
            Connector.getInstance().getConnection();
            return new AllergenDbDao();

        } catch (FailedDatabaseConnectionException e){
            // Dummy fallback on demoDao -> Allow only login without app crash
            System.out.println("[Factory] DB AllergenDao irraggiungibile. Fallback -> Demo/Memory");
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