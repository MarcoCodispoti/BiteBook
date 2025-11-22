package com.example.bitebook.controller.application;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.persistence.AllergenDbDao;
import com.example.bitebook.model.singleton.LoggedUser;

import java.sql.SQLException;
import java.util.Vector;

public class AllergiesController{

    public static Vector<AllergenBean> getClientAllergies() throws SQLException{
        Vector<AllergenBean> clientAllergyBeans = new Vector<>();
        Vector<Allergen> clientAllergies;

        try{
//            AllergenDao allergenDao = DaoFactory.getAllergenDao();
//            clientAllergies = allergenDao.getClientAllergies(LoggedUser.getInstance().getClient());
            clientAllergies = LoggedUser.getInstance().getClient().getAllergies();
            if( clientAllergies == null || clientAllergies.isEmpty() ){
                return clientAllergyBeans;
            }
            for(Allergen allergen : clientAllergies){
                AllergenBean allergenBean = new AllergenBean();
                allergenBean.setId(allergen.getId());
                allergenBean.setName(allergen.getName());
                clientAllergyBeans.add(allergenBean);
            }
        } catch(Exception e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            throw new SQLException();
        }
        return clientAllergyBeans;
    }


    public static void removeClientAllergy(int allergenId) throws Exception{
        try{
            AllergenDao allergenDao = DaoFactory.getAllergenDao();
            allergenDao.removeClientAllergy(LoggedUser.getInstance().getClient().getId(),allergenId);
            // AllergenDbDao.removeClientAllergy(LoggedUser.getInstance().getClient().getId(),allergenId);
        } catch (Exception e) {
            throw new Exception(e);
        }
        LoggedUser.getInstance().getClient().getAllergies().removeIf(allergen -> allergen.getId() == allergenId);
    }

    public static Vector<AllergenBean> getAllergens() throws Exception{
        Vector<AllergenBean> allergenBeans = new Vector<>();
        Vector<Allergen> allergens;

        try{
            AllergenDao allergenDao = new AllergenDbDao();
            // allergens = AllergenDbDao.getAllergens();
            allergens = allergenDao.getAllergens();     // le prendo dal database perché altrimenti dovrei inserire manualmente tutte le allergie

            for(Allergen allergen : allergens){
                AllergenBean allergenBean = new AllergenBean();
                allergenBean.setId(allergen.getId());
                allergenBean.setName(allergen.getName());
                allergenBeans.add(allergenBean);
            }
        }  catch(Exception e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            throw new SQLException();
        }
        return allergenBeans;
    }

    public static void insertAllergy(AllergenBean allergenBean) throws Exception{
        Allergen allergen = new Allergen();
        allergen.setId(allergenBean.getId());
        allergen.setName(allergenBean.getName());
        try {
            AllergenDao allergenDao = DaoFactory.getAllergenDao();
            allergenDao.insertAllergy(allergen, LoggedUser.getInstance().getClient().getId());
            // AllergenDbDao.insertAllergy(allergen, LoggedUser.getInstance().getClient().getId());
        } catch (Exception e) {
            throw new Exception(e);
        }
        LoggedUser.getInstance().getClient().getAllergies().add(allergen);
    }



}
