package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.session.LoggedUser;

import java.util.ArrayList;
import java.util.List;



public class AllergiesController{
    public List<AllergenBean> getClientAllergies(){
        List<AllergenBean> clientAllergyBeans = new ArrayList<>();
        if (LoggedUser.getInstance().getClient() == null) return clientAllergyBeans;
        List<Allergen> clientAllergies = LoggedUser.getInstance().getClient().getAllergies();
        if (clientAllergies != null && !clientAllergies.isEmpty()) {
            for (Allergen allergen : clientAllergies) {
                AllergenBean allergenBean = new AllergenBean();
                allergenBean.setId(allergen.getId());
                allergenBean.setName(allergen.getName());
                clientAllergyBeans.add(allergenBean);
            }
        }
        return clientAllergyBeans;
    }


    public void removeClientAllergy(AllergenBean allergyToRemoveBean) throws FailedRemoveException {

        DaoFactory.getAllergenDao().removeClientAllergy(
                LoggedUser.getInstance().getClient().getId(),
                allergyToRemoveBean.getId()
        );

        LoggedUser.getInstance().getClient().getAllergies()
                .removeIf(allergen -> allergen.getId() == allergyToRemoveBean.getId());
    }



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




    public void insertAllergy(AllergenBean allergenBean) throws FailedInsertException{
        Allergen allergen = new Allergen();
        allergen.setId(allergenBean.getId());
        allergen.setName(allergenBean.getName());
        DaoFactory.getAllergenDao().insertAllergy(
                allergen,
                LoggedUser.getInstance().getClient().getId()
        );
        updateLocalSessionList(allergen);
    }


    private void updateLocalSessionList(Allergen newAllergen) {
        List<Allergen> currentList = LoggedUser.getInstance().getClient().getAllergies();
        if (currentList == null) {
            currentList = new ArrayList<>();
            LoggedUser.getInstance().getClient().setAllergies(currentList);
        }
        boolean alreadyExists = false;
        for (Allergen existing : currentList) {
            if (existing.getId() == newAllergen.getId()) {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists) {
            currentList.add(newAllergen);
        }
    }



}
