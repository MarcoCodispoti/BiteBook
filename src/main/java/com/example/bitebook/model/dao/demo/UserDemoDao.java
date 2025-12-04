package com.example.bitebook.model.dao.demo;

import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.enums.SpecializationType;


import java.util.ArrayList;
import java.util.List;



public class UserDemoDao implements UserDao {
    private static final List<Client> demoClients = new ArrayList<>();
    private static final List<Chef> demoChefs = new ArrayList<>();

    static {
        Client client = new Client();
        client.setId(1);
        client.setName("Mario");
        client.setSurname("Rossi");
        client.setEmail("mrossi@mail.it");
        client.setPassword("password12");

        demoClients.add(client);

        Chef chef = new Chef();
        chef.setId(1);
        chef.setName("Alessandro");
        chef.setSurname("Borghese");
        chef.setCity("Roma");
        chef.setEmail("aborghe@chef.it");
        chef.setPassword("password12");
        chef.setStyle(CookingStyle.MODERN);
        chef.setSpecializations(List.of(SpecializationType.SEAFOOD, SpecializationType.VEGAN));

        demoChefs.add(chef);
    }

    // Okk
    @Override
    public Role getCredentialsRole(String email, String password) throws WrongCredentialsException{
        for (Client c : demoClients) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return Role.CLIENT;
            }
        }

        for (Chef c : demoChefs) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return Role.CHEF;
            }
        }

        throw new WrongCredentialsException("Wrong credentials");
    }



    @Override
    public Client getClientInfo(String email, String password){
        for (Client c : demoClients) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }



    @Override
    public Chef getChefInfo(String email, String password){
        for (Chef c : demoChefs) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }



}

