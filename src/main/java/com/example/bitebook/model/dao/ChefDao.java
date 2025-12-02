package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.Chef;

import java.sql.SQLException;
import java.util.List;

public interface ChefDao{

    void findCityChefs(String cityName) throws FailedSearchException, NoChefInCityException;

    List<Chef> getChefsInCity(String cityName) throws FailedSearchException;

    Chef getChefFromMenu(int menuId) throws FailedSearchException;

}
