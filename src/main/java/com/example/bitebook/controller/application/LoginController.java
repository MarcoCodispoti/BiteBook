package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.bean.LoginBean;
import com.example.bitebook.model.dao.factory.DaoFactory;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.session.LoggedUser;
import com.example.bitebook.util.DaoConfigurator;

public class LoginController{



    public void authenticate(LoginBean loginBean) throws WrongCredentialsException, FailedSearchException {
        Role role = checkCredentials(loginBean.getEmail(), loginBean.getPassword());
        loadUserAndSetSession(role, loginBean);
    }



    public void loginAsClient(LoginBean loginBean) throws WrongCredentialsException, FailedSearchException {
        Role role = checkCredentials(loginBean.getEmail(), loginBean.getPassword());

        if (role != Role.CLIENT) {
            throw new WrongCredentialsException("Invalid credentials");
        }

        loadUserAndSetSession(role, loginBean);
    }



    private Role checkCredentials(String email, String password) throws WrongCredentialsException, FailedSearchException {
        Role role = DaoConfigurator.getInstance().getFactory().getUserDao().getCredentialsRole(email, password);
        if (role == null) {
            throw new WrongCredentialsException("Invalid credentials");
        }
        return role;
    }



    private void loadUserAndSetSession(Role role, LoginBean loginBean) throws FailedSearchException {
        DaoFactory daoFactory = DaoConfigurator.getInstance().getFactory();
        UserDao userDao = daoFactory.getUserDao();
        Client client = null;
        Chef chef = null;

        switch (role) {
            case CLIENT:
                client = userDao.getClientInfo(loginBean.getEmail(), loginBean.getPassword());
                if (client == null) throw new FailedSearchException("Error fetching client data");
                client.setAllergies(daoFactory.getAllergenDao().getClientAllergies(client));
                break;
            case CHEF:
                chef = userDao.getChefInfo(loginBean.getEmail(), loginBean.getPassword());
                if (chef == null) throw new FailedSearchException("Error fetching chef data");
                break;
            default:
                throw new FailedSearchException("Corrupted Role");
        }

        LoggedUser.getInstance().logout();
        LoggedUser.getInstance().setRole(role);

        if (role == Role.CLIENT) {
            LoggedUser.getInstance().setClient(client);
        } else {
            LoggedUser.getInstance().setChef(chef);
        }
    }



    public void logout() {
        LoggedUser.getInstance().logout();
    }


}
