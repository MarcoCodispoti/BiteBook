package com.example.bitebook.model.dao.Factory;

import com.example.bitebook.model.dao.*;

public interface AbstractDaoFactory {

    AllergenDao getAllergenDao();

    ChefDao getChefDao();

    DishDao getDishDao();

    MenuDao getMenuDao();

    ServiceRequestDao getServiceRequestDao();

    UserDao getUserDao();

}
