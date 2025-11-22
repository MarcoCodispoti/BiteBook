package com.example.bitebook.model.dao;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.enums.SpecializationType;

import java.sql.SQLException;
import java.util.Vector;

public interface ChefDao{

    public Vector<Chef> findCityChefs(String cityName);

    public Vector<Chef> getChefsInCity(String cityName) throws SQLException;

    // public Vector<SpecializationType> convertSpecializationString(String specializationString);

    public Chef getChefFromMenu(int menuId) throws Exception;

}
