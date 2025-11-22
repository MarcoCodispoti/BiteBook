package com.example.bitebook.model.dao;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;

import java.sql.SQLException;
import java.util.Vector;

public interface AllergenDao {

    public Vector<Allergen> getClientAllergies(Client client) throws Exception;

    public void removeClientAllergy(int clientId,int allergenId) throws Exception;

    public Vector<Allergen> getAllergens() throws Exception;

    public void insertAllergy(Allergen allergen, int clientId) throws Exception;

}
