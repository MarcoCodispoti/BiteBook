package com.example.bitebook.model.bean;

import com.example.bitebook.model.Allergen;

public class AllergenBean{

    private int id;
    private String name;


    public AllergenBean(){}

    public AllergenBean(int id, String name){
        this.id = id;
        this.name = name;
    }

    public AllergenBean(Allergen allergen){
        this.id = allergen.getId();
        this.name = allergen.getName();
    }

    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public boolean validate(){
        return  this.id > 0 && this.name != null && !this.name.isEmpty();
    }



    public String toString(){return this.name;}

}
