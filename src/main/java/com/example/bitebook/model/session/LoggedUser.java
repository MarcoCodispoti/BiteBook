package com.example.bitebook.model.session;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.enums.Role;

@SuppressWarnings("java:S6548")
public class LoggedUser{


    private Client client;
    private Chef chef;
    private Role role;

    private LoggedUser() {}

    private static class SingletonHolder {
        private static final LoggedUser INSTANCE = new LoggedUser();
    }

    public static LoggedUser getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void logout() {
        this.client = null;
        this.chef = null;
        this.role = null;
    }

    public static void clear() {
        getInstance().logout();
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
