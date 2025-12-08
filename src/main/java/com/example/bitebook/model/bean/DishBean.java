package com.example.bitebook.model.bean;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.enums.CourseType;

import java.util.ArrayList;
import java.util.List;

public class DishBean{
    private final int id;
    private final String name;
    private final CourseType courseType;
    private final String description;
    private final List<AllergenBean> allergens;


    public DishBean(Dish dish){
        this.id = dish.getId();
        this.name = dish.getName();
        this.courseType = dish.getCourseType();
        this.description = dish.getDescription();

        this.allergens = new ArrayList<>();
        if (dish.getAllergens() != null) {
            for (Allergen allergen : dish.getAllergens()){
                this.allergens.add(new AllergenBean(allergen));
            }
        }

    }

    public int  getId() {return id;}
    public String getName(){return name;}
    public CourseType getCourseType(){return courseType;}
    public String getDescription(){return description;}
    public List<AllergenBean> getAllergens(){return this.allergens;}

    public boolean validate(){
        return id>0 && name!=null && !name.isEmpty() && courseType!=null && description != null && description.length()>10;
    }
    
}
