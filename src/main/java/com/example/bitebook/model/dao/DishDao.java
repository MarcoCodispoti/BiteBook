package com.example.bitebook.model.dao;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Menu;

import java.sql.SQLException;
import java.util.Vector;

public interface DishDao{

    public Vector<Allergen> getDishAllergens(int dishId) throws Exception;


}
