package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.Menu;

import java.util.List;

public interface MenuDao {
    public List<Menu> getChefMenus(int chefId) throws FailedSearchException;

    public List<Dish> getMenuCourses(int menuId);

    public Menu getMenuLevelsSurcharge(int menuId) throws Exception;
}
