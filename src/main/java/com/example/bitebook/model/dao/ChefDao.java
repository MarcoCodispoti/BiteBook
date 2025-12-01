package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.Chef;

import java.sql.SQLException;
import java.util.List;

public interface ChefDao{

    public List<Chef> findCityChefs(String cityName) throws NoChefInCityException, FailedDatabaseConnectionException;

    public List<Chef> getChefsInCity(String cityName) throws SQLException;

    // public List<SpecializationType> convertSpecializationString(String specializationString);

    public Chef getChefFromMenu(int menuId) throws Exception;

}
