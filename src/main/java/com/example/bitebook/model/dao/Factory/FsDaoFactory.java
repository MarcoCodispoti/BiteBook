package com.example.bitebook.model.dao.Factory;

import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.dao.demo.ChefDemoDao;
import com.example.bitebook.model.dao.demo.DishDemoDao;
import com.example.bitebook.model.dao.demo.MenuDemoDao;
import com.example.bitebook.model.dao.demo.ServiceRequestDemoDao;
import com.example.bitebook.model.dao.persistence.AllergenFSDao;
import com.example.bitebook.model.dao.persistence.UserFsDao;

public class FsDaoFactory implements AbstractDaoFactory {
    @Override
    public AllergenDao getAllergenDao() {
        return new AllergenFSDao();
    }

    @Override
    public ChefDao getChefDao() {
        return new ChefDemoDao(); // dummy fallback on demo dao
    }

    @Override
    public DishDao getDishDao() {
        return new DishDemoDao(); // dummy fallback on demo dao
    }

    @Override
    public MenuDao getMenuDao() {
        return new MenuDemoDao();  // dummy fallback on demo dao
    }

    @Override
    public ServiceRequestDao getServiceRequestDao() {
        return new ServiceRequestDemoDao();  // dummy fallback on demo dao
    }

    @Override
    public UserDao getUserDao() {
        return new UserFsDao();
    }

}
