package com.example.bitebook.model.bean;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.SpecializationType;

import java.util.Vector;

public class ChefBean extends UserBean {
    // private int id;
//    private String name;
//    private String surname;
    private CookingStyle cookingStyle;
    private Vector<SpecializationType> specializationTypes;
    private String city;

    public ChefBean(){};

    public ChefBean(Chef chef){
        super();
        this.id = chef.getId();
        this.name = chef.getName();
        this.surname = chef.getSurname();
        this.cookingStyle = chef.getStyle();
        this.specializationTypes = chef.getSpecializations();
    }

//    public int getId(){return id;}
//    public void setId(int id){this.id=id;}
//    public void setName(String name){this.name=name;}
//    public String getName(){return name;}
//    public void setSurname(String surname){this.surname=surname;}
//    public String getSurname(){return surname;}

    public CookingStyle getCookingStyle(){return cookingStyle;}
    public void setCookingStyle(CookingStyle cookingStyle){this.cookingStyle=cookingStyle;}
    public Vector<SpecializationType> getSpecializationTypes(){return this.specializationTypes;}
    public void setSpecializationTypes(Vector<SpecializationType> specializationTypes){this.specializationTypes = specializationTypes;}
    public String getCity(){return city;}
    public void setCity(String inputCity){this.city=inputCity;}


    public boolean validateCity(String inputCity){
        if (city == null || city.trim().isEmpty()) {
            return false;
        }
        // Rimuove spazi iniziali e finali
        city = city.trim();
        // Controlla che contenga solo lettere e spazi (es. "New York")
        if (!city.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]+$")) {
            return false;
        }
        // Controlla lunghezza minima
        if (city.length() < 2){
            return false;
        }
        // Controlla che inizi con lettera maiuscola
        return Character.isUpperCase(city.charAt(0));
    }
}
