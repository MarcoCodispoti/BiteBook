package com.example.bitebook.model.dao.factory;

import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.dao.persistence.AllergenFSDao;
import com.example.bitebook.model.dao.persistence.UserFsDao;

public class FsDaoFactory implements DaoFactory {
    @Override
    public AllergenDao getAllergenDao() {
        return new AllergenFSDao();
    }

    @Override
    public ChefDao getChefDao() {
        return null; // not implemented yet
    }

    @Override
    public DishDao getDishDao() {
        return null; // not implemented yet
    }

    @Override
    public MenuDao getMenuDao(){
        return null; // not implemented yet
    }

    @Override
    public ServiceRequestDao getServiceRequestDao() {
        return null; // not implemented yet
    }

    @Override
    public UserDao getUserDao() {
        return new UserFsDao();
    }

}
