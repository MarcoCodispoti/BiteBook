package com.example.bitebook.model.dao.factory;

import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.dao.persistence.AllergenFsDao;
import com.example.bitebook.model.dao.persistence.UserFsDao;

public class FsDaoFactory implements DaoFactory {
    @Override
    public AllergenDao getAllergenDao() {
        return new AllergenFsDao();
    }

    @Override
    public ChefDao getChefDao() {
        return null; // ChefFsDao not implemented yet
    }

    @Override
    public DishDao getDishDao() {
        return null; // FishFsDao not implemented yet
    }

    @Override
    public MenuDao getMenuDao(){
        return null; // ChefFsDao not implemented yet
    }

    @Override
    public ServiceRequestDao getServiceRequestDao() {
        return null; // ChefFsDao not implemented yet
    }

    @Override
    public UserDao getUserDao() {
        return new UserFsDao();
    }

}
