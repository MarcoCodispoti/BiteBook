package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.Menu;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.DishBean;
import com.example.bitebook.model.bean.MenuBean;
import com.example.bitebook.model.dao.ChefDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.DishDao;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.singleton.LoggedUser;

import java.util.ArrayList;
import java.util.List;

public class ExplorationController{

    public Boolean checkCityChefs(ChefBean chefBean) {
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

    public List<ChefBean> getChefsInCity(ChefBean chefBean) {
        List<ChefBean> chefInCityBeans = new ArrayList<>();
        ChefDao chefDao = DaoFactory.getChefDao();

        try {
            List<Chef> chefsInCity = chefDao.getChefsInCity(chefBean.getCity());
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




    public List<MenuBean> getChefMenus(ChefBean chefBean){
        List<Menu> chefMenus;
        MenuDao menuDao = DaoFactory.getMenuDao();
        List<MenuBean> chefMenuBeans = new ArrayList<>();
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


    public List<DishBean> getCourses(MenuBean menuBean){
        List<Dish> courses;
        MenuDao menuDao = DaoFactory.getMenuDao();

        List<DishBean> coursesBean = new ArrayList<>();

        try{
            courses = menuDao.getMenuCourses(menuBean.getId());
            DishBean dishBean;


            for(Dish dish : courses){
                List< Allergen> dishAllergens;
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


    public List<AllergenBean> getMenuAllergens(List<DishBean> courseBeans){
        List<AllergenBean> menuAllergenBeans = new ArrayList<>();

        for (DishBean dish : courseBeans) {
            List<Allergen> dishAllergens = dish.getAllergens();
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
