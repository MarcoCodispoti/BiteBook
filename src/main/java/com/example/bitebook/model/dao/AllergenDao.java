package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;

import java.util.List;

public interface AllergenDao {

    List<Allergen> getClientAllergies(Client client) throws FailedSearchException;

    void removeClientAllergy(int clientId,int allergenId) throws FailedRemoveException;

    List<Allergen> getAllergens() throws FailedSearchException;

    void insertAllergy(Allergen allergen, int clientId) throws FailedInsertException;

    List<Allergen> getDishAllergens(int dishId) throws FailedSearchException;

}
