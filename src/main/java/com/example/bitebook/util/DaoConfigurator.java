package com.example.bitebook.util;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.dao.factory.DaoFactory;
import com.example.bitebook.model.dao.factory.DbDaoFactory;
import com.example.bitebook.model.dao.factory.DemoDaoFactory;
import com.example.bitebook.model.dao.factory.FsDaoFactory;


public class DaoConfigurator{


    private final DaoFactory daoFactory;


    private DaoConfigurator(){
        if (AppConfig.getInstance().isDemoMode()) {
            this.daoFactory = new DemoDaoFactory();
        } else {
            DaoFactory factoryTemp;
            try {
                Connector.getInstance().getConnection();
                factoryTemp = new DbDaoFactory();
            } catch (FailedDatabaseConnectionException _) {
                factoryTemp = new FsDaoFactory();
            }
            this.daoFactory = factoryTemp;
        }
    }



    private static class SingletonHolder{
        private static final DaoConfigurator INSTANCE = new DaoConfigurator();
    }



    public DaoFactory getFactory() {
        return this.daoFactory;
    }



    public static DaoConfigurator getInstance(){
        return SingletonHolder.INSTANCE;
    }


}
