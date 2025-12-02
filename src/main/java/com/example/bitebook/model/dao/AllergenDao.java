package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;

import java.util.List;

public interface AllergenDao {

    public List<Allergen> getClientAllergies(Client client) throws Exception;

    public void removeClientAllergy(int clientId,int allergenId) throws FailedRemoveException;

    public List<Allergen> getAllergens() throws FailedSearchException;

    public void insertAllergy(Allergen allergen, int clientId) throws FailedInsertException;

    public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException;
}
