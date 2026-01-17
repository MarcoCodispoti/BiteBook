package com.example.bitebook.model.dao.factory;

import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.dao.demo.*;

public class DemoDaoFactory implements AbstractDaoFactory {

    @Override
    public AllergenDao getAllergenDao() {
        return new AllergenDemoDao();
    }

    @Override
    public ChefDao getChefDao() {
        return new ChefDemoDao();
    }

    @Override
    public DishDao getDishDao() {
        return new DishDemoDao();
    }

    @Override
    public MenuDao getMenuDao() {
        return new MenuDemoDao();
    }

    @Override
    public ServiceRequestDao getServiceRequestDao() {
        return new ServiceRequestDemoDao();
    }

    @Override
    public UserDao getUserDao() {
        return new UserDemoDao();
    }

}
