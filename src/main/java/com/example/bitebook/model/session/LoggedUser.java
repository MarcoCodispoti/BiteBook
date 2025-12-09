package com.example.bitebook.model.session;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.enums.Role;

public class LoggedUser{

    private static volatile LoggedUser instance = null;

    private Client client;
    private Chef chef;
    private Role role;


    private LoggedUser() {}

    public static LoggedUser getInstance() {
        if (instance == null) {
            synchronized (LoggedUser.class) {
                if (instance == null) {
                    instance = new LoggedUser();
                }
            }
        }
        return instance;
    }

    public void logout() {
        this.client = null;
        this.chef = null;
        this.role = null;
    }

    public static void clear() {
        if (instance != null) {
            instance.logout();
        }
    }

    public void setChef(Chef chef) {
        this.chef = chef;
        this.client = null;
        this.role = Role.CHEF;
    }

    public void setClient(Client client) {
        this.client = client;
        this.chef = null;
        this.role = Role.CLIENT;
    }

    public Chef getChef() { return chef; }
    public Client getClient() { return client; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

}
