package com.example.bitebook.util;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.dao.factory.AbstractDaoFactory;
import com.example.bitebook.model.dao.factory.DbDaoFactory;
import com.example.bitebook.model.dao.factory.DemoDaoFactory;
import com.example.bitebook.model.dao.factory.FsDaoFactory;


public class DaoConfigurator{


    private final AbstractDaoFactory daoFactory;


    private DaoConfigurator(){
        if (AppConfig.getInstance().isDemoMode()) {
            this.daoFactory = new DemoDaoFactory();
        } else {
            AbstractDaoFactory factoryTemp;
            try {
                Connector.getInstance().getConnection();
                factoryTemp = new DbDaoFactory();
            } catch (FailedDatabaseConnectionException _) {
                factoryTemp = new FsDaoFactory();
            }
            this.daoFactory = factoryTemp;
        }
    }


    public AbstractDaoFactory getFactory() {
        return this.daoFactory;
    }



    private static class SingletonHolder{
        private static final DaoConfigurator INSTANCE = new DaoConfigurator();
    }



    public static DaoConfigurator getInstance(){
        return SingletonHolder.INSTANCE;
    }


}
