package com.example.bitebook.model.dao.factory;

import com.example.bitebook.model.dao.*;

public interface DaoFactory {

    AllergenDao getAllergenDao();

    ChefDao getChefDao();

    DishDao getDishDao();

    MenuDao getMenuDao();

    ServiceRequestDao getServiceRequestDao();

    UserDao getUserDao();

}
