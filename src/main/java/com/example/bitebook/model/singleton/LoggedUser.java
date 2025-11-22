package com.example.bitebook.model.singleton;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.enums.Role;

public class LoggedUser{
    private Client client;
    private Chef chef;
    private Role role;

    private LoggedUser(){}

    public static LoggedUser getInstance(){
        if(instance==null){
            instance = new LoggedUser();
        }
        return instance;
    }

    public static void logout(){
        if(instance!=null) {
            instance.client = null;
            instance.chef = null;
            instance.role = null;
        }
    }


    public void setRole(Role role){this.role=role;}
    public Role getRole(){return role;}

    public Chef getChef(){return chef;}
    public void setChef(Chef chef){this.chef=chef;}

    public Client getClient(){return client;}
    public void setClient(Client client){this.client=client;}

    private static LoggedUser instance;
}
