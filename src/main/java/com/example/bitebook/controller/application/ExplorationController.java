package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.*;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.singleton.LoggedUser;

import java.util.Vector;

public class ExplorationController{

//    public Boolean checkCityChefs(ChefBean chefBean){
//        ChefDao chefDbDao = DaoFactory.getChefDao();
//        try{
//            chefDbDao.findCityChefs(chefBean.getCity());
//        } catch (NoChefInCityException e) {
//            return false;
//        }
//        return true;
//    }
//
//
//    public boolean isLoggedClient(){
//        return LoggedUser.getInstance().getClient() != null ;
//    }
//
//
//    public Vector<ChefBean> getChefsInCity(ChefBean chefBean){
//        Vector<Chef> chefsInCity;
//        Vector<ChefBean> chefInCityBeans = new Vector<>();
//        ChefDao chefDao = DaoFactory.getChefDao();
//        try{
//            chefsInCity = chefDao.getChefsInCity(chefBean.getCity());
//        if(chefsInCity == null || chefsInCity.isEmpty()){
//            throw new SQLException();
//        }
//
//        for(Chef chef : chefsInCity){
//            ChefBean newChefbean = new ChefBean(chef);
//            chefInCityBeans.add(newChefbean);
//        }
//
//
//        } catch(SQLException e){
//            // to be handled
//            return null;
//        }
//        return chefInCityBeans;
//    }



    public Boolean checkCityChefs(ChefBean chefBean) {
        // CORRETTO: Uso DaoFactory invece di new ChefDbDao()
        ChefDao chefDao = DaoFactory.getChefDao();
        try {
            chefDao.findCityChefs(chefBean.getCity());
        } catch (NoChefInCityException e) {
            return false;
        } catch (Exception e) {
            // Gestione errori generici DB
            return false;
        }
        return true;
    }

    public boolean isLoggedClient() {
        return LoggedUser.getInstance().getClient() != null;
    }

    public Vector<ChefBean> getChefsInCity(ChefBean chefBean) {
        Vector<ChefBean> chefInCityBeans = new Vector<>();
        ChefDao chefDao = DaoFactory.getChefDao();

        try {
            Vector<Chef> chefsInCity = chefDao.getChefsInCity(chefBean.getCity());
            if (chefsInCity == null || chefsInCity.isEmpty()) {
                // Non lanciare SQLException manualmente qui, ritorna lista vuota o gestisci diversamente
                return chefInCityBeans;
            }

            for (Chef chef : chefsInCity) {
                // Ottimo uso del costruttore del Bean!
                chefInCityBeans.add(new ChefBean(chef));
            }

        } catch (Exception e) {
            return null;
        }
        return chefInCityBeans;
    }




    public Vector<MenuBean> getChefMenus(ChefBean chefBean){
        Vector<Menu> chefMenus;
        MenuDao menuDao = DaoFactory.getMenuDao();
        Vector<MenuBean> chefMenuBeans = new Vector<>();
        try{
            chefMenus = menuDao.getChefMenus(chefBean.getId());

            for(Menu menu : chefMenus){
                MenuBean menuBean;
                menuBean = new MenuBean(menu);
                chefMenuBeans.add(menuBean);
            }
        } catch (Exception e){
            return null;
        }
        return chefMenuBeans;
    }


    public Vector<DishBean> getCourses(MenuBean menuBean){
        Vector<Dish> courses;
        MenuDao menuDao = DaoFactory.getMenuDao();

        Vector<DishBean> coursesBean = new Vector<>();

        try{
            courses = menuDao.getMenuCourses(menuBean.getId());
            DishBean dishBean;


            for(Dish dish : courses){
                Vector< Allergen> dishAllergens;
                DishDao dishDao = DaoFactory.getDishDao();
                dishAllergens = dishDao.getDishAllergens(dish.getId());
                dish.setAllergens(dishAllergens);
            }

            for(Dish dish : courses){
                dishBean = new DishBean(dish);
                coursesBean.add(dishBean);
            }
        } catch (Exception e){
            return null;
        }

        return coursesBean;
    }


    public Vector<AllergenBean> getMenuAllergens(Vector<DishBean> courseBeans){
        Vector<AllergenBean> menuAllergenBeans = new Vector<>();

        for (DishBean dish : courseBeans) {
            Vector<Allergen> dishAllergens = dish.getAllergens();
            if (dishAllergens != null) {
                for (Allergen newAllergen : dishAllergens) {
                    boolean alreadyExists = false;
                    for (AllergenBean existingBean : menuAllergenBeans) {
                        if (existingBean.getId() == newAllergen.getId()) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        AllergenBean allergenBean = new AllergenBean(newAllergen);
                        menuAllergenBeans.add(allergenBean);
                    }
                }
            }
        }
        return menuAllergenBeans;
    }


}
