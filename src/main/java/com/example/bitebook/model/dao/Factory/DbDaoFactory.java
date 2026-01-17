package com.example.bitebook.model.dao.Factory;

import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.dao.persistence.*;

public class DbDaoFactory implements AbstractDaoFactory {

    @Override
    public AllergenDao getAllergenDao() {
        return new AllergenDbDao();
    }

    @Override
    public ChefDao getChefDao() {
        return new ChefDbDao();
    }

    @Override
    public DishDao getDishDao() {
        return new DishDbDao();
    }

    @Override
    public MenuDao getMenuDao() {
        return new MenuDbDao();
    }

    @Override
    public ServiceRequestDao getServiceRequestDao() {
        return new ServiceRequestDbDao();
    }

    @Override
    public UserDao getUserDao() {
        return new UserDbDao();
    }

}
