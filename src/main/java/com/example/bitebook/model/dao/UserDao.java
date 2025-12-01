package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.enums.Role;

import javax.security.auth.login.FailedLoginException;

public interface UserDao{

    public Role getCredentialsRole(String email, String password) throws FailedDatabaseConnectionException, FailedDatabaseConnectionException, FailedLoginException;

    public Chef getChefInfo(String email, String password);

    public Client getClientInfo(String email, String password);
}
