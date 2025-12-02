package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Dish;

import java.util.List;

public interface DishDao{

    // public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException;

    public List<Dish> getMenuCourses(int menuId) throws FailedSearchException;
}
