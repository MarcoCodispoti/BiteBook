package com.example.bitebook.util;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.dao.Factory.AbstractDaoFactory;
import com.example.bitebook.model.dao.Factory.DbDaoFactory;
import com.example.bitebook.model.dao.Factory.DemoDaoFactory;
import com.example.bitebook.model.dao.Factory.FsDaoFactory;


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
            } catch (FailedDatabaseConnectionException e) {
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
