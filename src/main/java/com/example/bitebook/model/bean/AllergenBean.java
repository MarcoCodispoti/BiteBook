package com.example.bitebook.model.bean;

import com.example.bitebook.model.Allergen;

public class AllergenBean{
    private int id;
    private String name;

    public AllergenBean() {}

    public AllergenBean(Allergen allergen) {
        this.id = allergen.getId();
        this.name = allergen.getName();
    }

    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}
}
