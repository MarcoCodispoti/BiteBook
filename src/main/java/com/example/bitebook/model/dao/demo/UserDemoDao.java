package com.example.bitebook.model.dao.demo;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.User;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.Role;

import javax.security.auth.login.FailedLoginException;
import java.util.Vector;

public class UserDemoDao implements UserDao {
    // SEPARIAMO LE LISTE: Simuliamo due tabelle diverse
    private static Vector<Client> fakeClients = new Vector<>();
    private static Vector<Chef> fakeChefs = new Vector<>();

    static {
        // --- 1. CLIENTE DEMO ---
        Client client = new Client();
        client.setId(1);
        client.setName("Mario");
        client.setSurname("Rossi");
        client.setEmail("mrossi@mail.it");
        client.setPassword("password12");
        // NOTA: Non settiamo client.setRole() perché hai detto che non esiste l'attributo.

        fakeClients.add(client);

        // --- 2. CHEF DEMO ---
        Chef chef = new Chef();
        chef.setId(1);
        chef.setName("Alessandro");
        chef.setSurname("Borghese");
        chef.setCity("Roma");
        chef.setEmail("aborghe@chef.it");
        chef.setPassword("password12");

        fakeChefs.add(chef);
    }

    @Override
    public Role getCredentialsRole(String email, String password) throws FailedLoginException {
        // 1. Cerchiamo nella lista dei CLIENTI
        for (Client c : fakeClients) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return Role.CLIENT; // Trovato tra i clienti -> Ritorna enum CLIENT
            }
        }

        // 2. Cerchiamo nella lista degli CHEF
        for (Chef c : fakeChefs) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return Role.CHEF; // Trovato tra gli chef -> Ritorna enum CHEF
            }
        }

        throw new FailedLoginException("Credenziali non valide in Demo Mode");
    }

    @Override
    public Client getClientInfo(String email, String password) {
        // Cerchiamo SOLO nella lista clienti
        for (Client c : fakeClients) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Chef getChefInfo(String email, String password) {
        // Cerchiamo SOLO nella lista chef
        for (Chef c : fakeChefs) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }
}

