package com.example.bitebook.model;

import com.example.bitebook.model.enums.CourseType;

import java.util.List;

public class Dish{

    private int id;
    private String name;
    private CourseType courseType;
    private String description;
    private List<Allergen> allergens;


    public Dish(){
        // Default constructor
    }


    public int  getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public CourseType getCourseType(){return courseType;}
    public void setCourseType(CourseType courseType){this.courseType = courseType;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public List<Allergen> getAllergens() {return allergens;}
    public void setAllergens(List<Allergen> allergens) {this.allergens = allergens;}

}
