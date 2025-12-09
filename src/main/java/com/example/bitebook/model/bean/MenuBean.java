package com.example.bitebook.model.bean;

import com.example.bitebook.model.Menu;
import com.example.bitebook.model.enums.DietType;

public class MenuBean{

    private int id;
    private String name;
    private int numberOfCourses;
    private DietType dietType;
    private int pricePerPerson;
    private int premiumLevelSurcharge;
    private int luxeLevelSurcharge;

    public MenuBean(){}

    public MenuBean(Menu menu){
        this.id = menu.getId();
        this.name = menu.getName();
        this.numberOfCourses = menu.getNumberOfCourses();
        this.dietType = menu.getDietType();
        this.pricePerPerson = menu.getPricePerPerson();
    }

    public int getId() {return id;}
    public void setId(int id){this.id = id;}
    public String getName() {return name;}
    public void setName(String name){this.name = name;}
    public int getNumberOfCourses() {return numberOfCourses;}
    public DietType getDietType() {return dietType;}
    public int getPricePerPerson() {return pricePerPerson;}
    public void setPricePerPerson(int pricePerPerson) {this.pricePerPerson = pricePerPerson;}
    public int getPremiumLevelSurcharge() {return premiumLevelSurcharge;}
    public int getLuxeLevelSurcharge() {return luxeLevelSurcharge;}
    public void setPremiumLevelSurcharge(int premiumLevelSurcharge) {this.premiumLevelSurcharge = premiumLevelSurcharge;}
    public void setLuxeLevelSurcharge(int luxeLevelSurcharge) {this.luxeLevelSurcharge = luxeLevelSurcharge;}

    public boolean validate(){
        return id > 0 && name != null && !name.isEmpty() && numberOfCourses > 0 && dietType != null && pricePerPerson > 0
                && premiumLevelSurcharge > 0 && luxeLevelSurcharge > 0 && luxeLevelSurcharge > premiumLevelSurcharge;
    }
}
