package com.example.bitebook.model.dao;

import com.example.bitebook.model.Dish;
import com.example.bitebook.model.Menu;

import java.sql.SQLException;
import java.util.Vector;

public interface MenuDao {
    public Vector<Menu> getChefMenus(int chefId) throws Exception;

    public Vector<Dish> getMenuCourses(int menuId);

    public Menu getMenuLevelsSurcharge(int menuId) throws Exception;
}
