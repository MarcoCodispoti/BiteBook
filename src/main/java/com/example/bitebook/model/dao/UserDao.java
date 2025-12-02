package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.enums.Role;

// Okk
public interface UserDao{

    Role getCredentialsRole(String email, String password) throws WrongCredentialsException, FailedSearchException;

    Chef getChefInfo(String email, String password) throws FailedSearchException;

    Client getClientInfo(String email, String password) throws FailedSearchException;
}
