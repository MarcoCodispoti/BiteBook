package com.example.bitebook.model;

import java.util.List;

public class Client extends User {

    public Client(){};

    public Client(int id, String name, String surname){
        super(id,name,surname);
    };

    private List<Allergen> allergies;

    public List<Allergen> getAllergies() {return allergies;}
    public void setAllergies(List<Allergen> allergies){this.allergies = allergies;}


    public String GetAllergiesAsString(){
        if (allergies == null || allergies.isEmpty()) {
            return "Nessuna allergia";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allergies.size(); i++) {
            sb.append(allergies.get(i).getName());
            if (i < allergies.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
