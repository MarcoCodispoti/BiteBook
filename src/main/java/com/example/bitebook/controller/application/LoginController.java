package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.bean.LoginBean;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.session.LoggedUser;

public class LoginController{

    public void authenticate(LoginBean loginBean) throws WrongCredentialsException, FailedSearchException {
        UserDao userDao = DaoFactory.getUserDao();
        Role role = userDao.getCredentialsRole(loginBean.getEmail(), loginBean.getPassword());
        if (role == null) {
            throw new WrongCredentialsException("Error while authenticating user: Cannot find users with inserted credentials");
        }
        Client client = null;
        Chef chef = null;
        if (role == Role.CLIENT) {
            client = userDao.getClientInfo(loginBean.getEmail(), loginBean.getPassword());
            if (client == null) {
                throw new FailedSearchException("Client profile not found for the logged user");
            }
            client.setAllergies(DaoFactory.getAllergenDao().getClientAllergies(client));
        } else if (role == Role.CHEF){
            chef = userDao.getChefInfo(loginBean.getEmail(), loginBean.getPassword());
            if (chef == null) {
                throw new FailedSearchException("Chef profile not found for the logged user");
            }
        }
        LoggedUser.getInstance().logout(); // Resetta istanza precedente
        LoggedUser.getInstance().setRole(role);
        if (role == Role.CLIENT) {
            LoggedUser.getInstance().setClient(client);
        } else {
            LoggedUser.getInstance().setChef(chef);
        }
    }

    public void logout(){
        LoggedUser.getInstance().logout();
    }

}
