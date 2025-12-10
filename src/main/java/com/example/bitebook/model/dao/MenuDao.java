package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Menu;

import java.util.List;

public interface MenuDao {
    List<Menu> getChefMenus(int chefId) throws FailedSearchException;

    Menu getMenuLevelsSurcharge(int menuId) throws FailedSearchException;
}
