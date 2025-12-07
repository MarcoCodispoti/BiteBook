package com.example.bitebook.model.bean;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.SpecializationType;

import java.util.List;

public class ChefBean extends UserBean{

    private CookingStyle cookingStyle;
    private List<SpecializationType> specializationTypes;
    private String city;

    
    public ChefBean(){}

    public ChefBean(Chef chef){
        super();
        this.id = chef.getId();
        this.name = chef.getName();
        this.surname = chef.getSurname();
        this.cookingStyle = chef.getStyle();
        this.specializationTypes = chef.getSpecializations();
        this.city = chef.getCity();
    }


    public CookingStyle getCookingStyle(){return cookingStyle;}
    public List<SpecializationType> getSpecializationTypes(){return this.specializationTypes;}
    public String getCity(){return city;}
    public void setCity(String inputCity){this.city=inputCity;}


    public boolean validateCity(){
        if (city == null || city.trim().isEmpty()) {
            return false;
        }
        city = city.trim();
        if (!city.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]+$")){
            return false;
        }
        if (city.length() < 2){
            return false;
        }
        return Character.isUpperCase(city.charAt(0));
    }

    @Override
    public boolean validate(){
        if(!super.validate()){
            return false;
        }
        return cookingStyle != null && specializationTypes != null && validateCity();
    }

}
