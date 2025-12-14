package com.example.bitebook.model.session;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.enums.Role;

public class LoggedUser{

//    private static volatile LoggedUser instance = null;
//
//    private Client client;
//    private Chef chef;
//    private Role role;
//
//
//    private LoggedUser() {}
//
//    public static LoggedUser getInstance() {
//        if (instance == null) {
//            synchronized (LoggedUser.class) {
//                if (instance == null) {
//                    instance = new LoggedUser();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public void logout() {
//        this.client = null;
//        this.chef = null;
//        this.role = null;
//    }
//
//    public static void clear() {
//        if (instance != null) {
//            instance.logout();
//        }
//    }
//
//    public void setChef(Chef chef) {
//        this.chef = chef;
//        this.client = null;
//        this.role = Role.CHEF;
//    }
//
//    public void setClient(Client client) {
//        this.client = client;
//        this.chef = null;
//        this.role = Role.CLIENT;
//    }
//
//    public Chef getChef() { return chef; }
//    public Client getClient() { return client; }
//
//    public Role getRole() { return role; }
//    public void setRole(Role role) { this.role = role; }



    private Client client;
    private Chef chef;
    private Role role;

    private LoggedUser() {}

    // --- MODIFICA 2: Classe interna statica (Thread-Safe per definizione) ---
    private static class SingletonHolder {
        private static final LoggedUser INSTANCE = new LoggedUser();
    }

    // --- MODIFICA 3: getInstance restituisce semplicemente l'istanza dell'Holder ---
    public static LoggedUser getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void logout() {
        this.client = null;
        this.chef = null;
        this.role = null;
    }

    // --- MODIFICA 4: Semplificato clear() ---
    public static void clear() {
        // Possiamo chiamare direttamente getInstance() perché è sicuro
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
