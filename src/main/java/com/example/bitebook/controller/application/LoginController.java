package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.WrongCredentialsExcpetion;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.bean.LoginBean;
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.singleton.LoggedUser;
import javax.security.auth.login.FailedLoginException;

import static com.example.bitebook.model.enums.Role.CHEF;
import static com.example.bitebook.model.enums.Role.CLIENT;

public class LoginController{

    public void authenticate(LoginBean loginBean) throws FailedLoginException, WrongCredentialsExcpetion {

        Role role;
        Client client = null;
        Chef chef = null;

        try {
            UserDao userDao = DaoFactory.getUserDao();
            role = userDao.getCredentialsRole(loginBean.getEmail(), loginBean.getPassword());

            if (role == CLIENT) {
                client = userDao.getClientInfo(loginBean.getEmail(), loginBean.getPassword());

                if (client != null) {
                    AllergenDao allergenDao = DaoFactory.getAllergenDao();
                    try {
                        client.setAllergies(allergenDao.getClientAllergies(client));
                    } catch (Exception e) {
                        // to b handled
                    }
                }

            } else if (role == CHEF){
                chef = userDao.getChefInfo(loginBean.getEmail(), loginBean.getPassword());
            }

            if ((role == CLIENT && client == null) || (role == CHEF && chef == null)) {
                throw new FailedLoginException("Ruolo trovato ma profilo utente mancante/nullo.");
            }

            LoggedUser.logout();

            if (role == CLIENT) {
                LoggedUser.getInstance().setClient(client);
            } else {
                LoggedUser.getInstance().setChef(chef);
            }

            LoggedUser.getInstance().setRole(role);

        } catch (FailedLoginException e) {
            throw new WrongCredentialsExcpetion("Email o Password errati");

        } catch (Exception e) {
            throw new FailedLoginException("Errore di sistema durante il login");
        }
    }


    public void logout(){
        LoggedUser.logout();
    }
}
