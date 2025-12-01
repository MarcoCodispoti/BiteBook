package com.example.bitebook.model.dao;

import com.example.bitebook.model.Allergen;

import java.util.List;

public interface DishDao{

    public List<Allergen> getDishAllergens(int dishId) throws Exception;


}
