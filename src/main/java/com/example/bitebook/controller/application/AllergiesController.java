package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.singleton.LoggedUser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllergiesController{

    // Ok
    public List<AllergenBean> getClientAllergies() throws SQLException{
        List<AllergenBean> clientAllergyBeans = new ArrayList<>();
        List<Allergen> clientAllergies;
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
        return clientAllergyBeans;
    }


    // Ok -> Eccezioni pulite
    public void removeClientAllergy(AllergenBean allergyToRemoveBean) throws Exception{
        AllergenDao allergenDao = DaoFactory.getAllergenDao();
        try{
            allergenDao.removeClientAllergy(LoggedUser.getInstance().getClient().getId(),allergyToRemoveBean.getId());
        } catch (Exception e) {
            throw new Exception(e);
        }
        LoggedUser.getInstance().getClient().getAllergies().removeIf(allergen -> allergen.getId() == allergyToRemoveBean.getId());
    }


    // Ok -> Eccezioni propagate direttamente al controller grafico
    public List<AllergenBean> getAllergens() throws FailedSearchException{
        List<AllergenBean> allergenBeans = new ArrayList<>();

        AllergenDao allergenDao = DaoFactory.getAllergenDao();
        List<Allergen> allergens = allergenDao.getAllergens();

        if (allergens != null) {
            for (Allergen allergen : allergens) {
                AllergenBean allergenBean = new AllergenBean();
                allergenBean.setId(allergen.getId());
                allergenBean.setName(allergen.getName());
                allergenBeans.add(allergenBean);
            }
        }
        return allergenBeans;
    }



    public void insertAllergy(AllergenBean allergenBean) throws Exception{
        Allergen allergen = new Allergen();
        allergen.setId(allergenBean.getId());
        allergen.setName(allergenBean.getName());

        try {
            AllergenDao allergenDao = DaoFactory.getAllergenDao();
            allergenDao.insertAllergy(allergen, LoggedUser.getInstance().getClient().getId());
        } catch (Exception e) {
            throw new Exception(e);
        }

        List<Allergen> currentList = LoggedUser.getInstance().getClient().getAllergies();

        if (currentList == null) {
            currentList = new ArrayList<>();
            LoggedUser.getInstance().getClient().setAllergies(currentList);
        }

        boolean alreadyExists = false;
        for (Allergen existing : currentList) {
            if (existing.getId() == allergen.getId()) {
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) {
            currentList.add(allergen);
        }
    }



}
