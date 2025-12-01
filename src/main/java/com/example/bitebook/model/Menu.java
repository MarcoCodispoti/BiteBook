package com.example.bitebook.model;

import com.example.bitebook.model.enums.DietType;

import java.util.List;

public class Menu {
    private int id;
    private String name;
    private int numberOfCourses;
    private DietType dietType;
    private int pricePerPerson;
    private List<Dish> courses;
    // private MenuLevel menuLevel;  -> Spostato in ReservationDetails
    private int premiumLevelSurcharge;
    private int luxeLevelSurcharge;

    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public int getNumberOfCourses(){return numberOfCourses;}
    public void setNumberOfCourses(int num){this.numberOfCourses = num;}
    public DietType getDietType(){return dietType;}
    public void setDietType(DietType dietType){this.dietType = dietType;}
    public int getPricePerPerson(){return pricePerPerson;}
    public void setPricePerPerson(int pricePerPerson){this.pricePerPerson = pricePerPerson;}
    public List<Dish> getCourses(){return courses;}
    public void setCourses(List<Dish> courses){this.courses = courses;}

    // rimossi -> Spostati in Menu Level
//    public MenuLevel getMenuLevel(){return menuLevel;}
//    public void setMenuLevel(MenuLevel menuLevel){this.menuLevel = menuLevel;}

    public int getPremiumLevelSurcharge(){return premiumLevelSurcharge;}
    public void setPremiumLevelSurcharge(int premiumLevelSurcharge){this.premiumLevelSurcharge = premiumLevelSurcharge;}
    public int getLuxeLevelSurcharge(){return luxeLevelSurcharge;}
    public void setLuxeLevelSurcharge(int luxeLevelSurcharge){this.luxeLevelSurcharge = luxeLevelSurcharge;}
}
