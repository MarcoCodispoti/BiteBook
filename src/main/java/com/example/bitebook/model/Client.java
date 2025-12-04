package com.example.bitebook.model;

import java.util.List;

public class Client extends User {

    private List<Allergen> allergies;


    public Client(){}

    public Client(int id, String name, String surname){
        super(id,name,surname);
    }


    public List<Allergen> getAllergies() {return allergies;}
    public void setAllergies(List<Allergen> allergies){this.allergies = allergies;}

}
