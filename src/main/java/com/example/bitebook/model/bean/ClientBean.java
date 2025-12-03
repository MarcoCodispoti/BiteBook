package com.example.bitebook.model.bean;

public class ClientBean extends UserBean{


    public ClientBean(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public boolean validate(){
        if(!super.validate()){
            return false;
        }
        return  this.name != null && !this.name.isEmpty() && this.surname != null && !this.surname.isEmpty();
    }
}
