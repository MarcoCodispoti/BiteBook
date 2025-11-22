package com.example.bitebook.model;

import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.SpecializationType;

import java.util.Vector;

public class Chef extends User {

    public Chef(){};

    public Chef(int id, String name, String surname, CookingStyle cookingStyle, String city) {
        super(id,name,surname);
        this.style = cookingStyle;
        this.city = city;
    }

    private CookingStyle style;
    private Vector<SpecializationType> specializations;
    private Vector<Menu> offeredMenus;
    private String city;


    public CookingStyle getStyle(){return style;}
    public Vector<SpecializationType> getSpecializations(){return specializations;}
    public Vector<Menu> getOfferedMenus(){return offeredMenus;}
    public String getCity(){return city;}

    public void setStyle(CookingStyle style){this.style = style;}
    public void setSpecializations(Vector<SpecializationType> specializations){
        this.specializations = specializations;
    }
    public void setOfferedMenus(Vector<Menu> offeredMenus){
        this.offeredMenus = offeredMenus;
    }
    public void setCity(String city){this.city = city;}
}
