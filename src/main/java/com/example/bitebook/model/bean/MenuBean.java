package com.example.bitebook.model.bean;

import com.example.bitebook.model.Menu;
import com.example.bitebook.model.enums.DietType;

public class MenuBean{

    public MenuBean(){};

    public MenuBean(Menu menu){
        this.id = menu.getId();
        this.name = menu.getName();
        this.numberOfCourses = menu.getNumberOfCourses();
        this.dietType = menu.getDietType();
        this.pricePerPerson = menu.getPricePerPerson();
    }

    private int id;
    private String name;
    private int numberOfCourses;
    private DietType dietType;
    private int pricePerPerson;

    // private MenuLevel menuLevel; -> Spostato nel reservation details
    private int premiumLevelSurcharge;
    private int luxeLevelSurcharge;

    public int getId() {return id;}
    public void setId(int id) {}
    public String getName() {return name;}
    public void setName(String name){this.name = name;}
    public int getNumberOfCourses() {return numberOfCourses;}
    public void setNumberOfCourses(int numberOfCourses) {}
    public DietType getDietType() {return dietType;}
    public void setDietType(DietType dietType) {}
    public int getPricePerPerson() {return pricePerPerson;}
    public void setPricePerPerson(int pricePerPerson){this.pricePerPerson=pricePerPerson;}

    // Spostati in ReservationDetailsBean
//    public MenuLevel getMenuLevel() {return menuLevel;}
//    public void setMenuLevel(MenuLevel menuLevel) {this.menuLevel=menuLevel;}

    public int getPremiumLevelSurcharge() {return premiumLevelSurcharge;}
    public int getLuxeLevelSurcharge() {return luxeLevelSurcharge;}
    public void setPremiumLevelSurcharge(int premiumLevelSurcharge) {this.premiumLevelSurcharge = premiumLevelSurcharge;}
    public void setLuxeLevelSurcharge(int luxeLevelSurcharge) {this.luxeLevelSurcharge = luxeLevelSurcharge;}

}
