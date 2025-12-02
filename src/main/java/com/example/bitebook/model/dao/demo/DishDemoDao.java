package com.example.bitebook.model.dao.demo;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.dao.DishDao;
import com.example.bitebook.model.enums.CourseType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishDemoDao implements DishDao {
    private static Map<Integer, List<Dish>> menuCoursesMap = new HashMap<>();

    static {
        Dish d1 = new Dish();
        d1.setId(1001);
        d1.setName("Carbonara Sbagliata");
        d1.setCourseType(CourseType.MAIN_COURSE);
        d1.setDescription("Carbonara con gamberi");

        Dish d2 = new Dish();
        d2.setId(1002);
        d2.setName("Saltimbocca 2.0");
        d2.setCourseType(CourseType.FIRST_COURSE);
        d2.setDescription("Vitello cotto a bassa temperatura");

        List<Dish> courses210 = new ArrayList<>();
        courses210.add(d1);
        courses210.add(d2);

        menuCoursesMap.put(210, courses210);
        Dish d3 = new Dish();
        d3.setId(2001);
        d3.setName("Linguine allo scoglio");
        d3.setCourseType(CourseType.MAIN_COURSE);
        d3.setDescription("Pasta fresca con frutti di mare");

        Dish d4 = new Dish();
        d4.setId(2002);
        d4.setName("Babà al rum");
        d4.setCourseType(CourseType.DESSERT);
        d4.setDescription("Classico napoletano");

        List<Dish> courses300 = new ArrayList<>();
        courses300.add(d3);
        courses300.add(d4);

        menuCoursesMap.put(300, courses300);
    }

    public List<Dish> getMenuCourses(int menuId) throws FailedSearchException {
        List<Dish> courses = menuCoursesMap.get(menuId);

        if (courses != null) {
            return courses; // Ritorna la lista in memoria
        } else {
            return new ArrayList<>(); // Ritorna lista vuota se non ci sono piatti
        }
    }

    public List<com.example.bitebook.model.Allergen> getDishAllergens(int dishId) throws FailedSearchException {
        return new ArrayList<>(); // Stub per ora
    }


}
