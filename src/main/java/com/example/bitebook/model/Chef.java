package com.example.bitebook.model;

import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.SpecializationType;

import java.util.List;

public class Chef extends User {

    private CookingStyle style;
    private List<SpecializationType> specializations;
    private List<Menu> offeredMenus;
    private String city;


    public Chef(){}

    public Chef(int id, String name, String surname, CookingStyle cookingStyle, String city) {
        super(id,name,surname);
        this.style = cookingStyle;
        this.city = city;
    }


    public CookingStyle getStyle(){return style;}
    public void setStyle(CookingStyle style){this.style = style;}

    public List<SpecializationType> getSpecializations(){return specializations;}
    public void setSpecializations(List<SpecializationType> specializations){
        this.specializations = specializations;
    }

    public List<Menu> getOfferedMenus(){return offeredMenus;}
    public void setOfferedMenus(List<Menu> offeredMenus){this.offeredMenus = offeredMenus;}

    public String getCity(){return city;}
    public void setCity(String city){this.city = city;}

}
