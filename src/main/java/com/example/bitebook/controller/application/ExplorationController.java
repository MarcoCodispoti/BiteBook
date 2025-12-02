package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
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
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.singleton.LoggedUser;

import java.util.ArrayList;
import java.util.List;

public class ExplorationController{

    // Ok -> va bene
    public boolean checkCityChefs(ChefBean chefBean) throws FailedSearchException {
        ChefDao chefDao = DaoFactory.getChefDao();
        try {
            chefDao.findCityChefs(chefBean.getCity());
            return true;
        } catch (NoChefInCityException e){
            return false;
        }
    }


    // Okk -> Va bene così
    public boolean isLoggedClient() {
        return LoggedUser.getInstance().getRole() == Role.CLIENT;
    }


    // Okk -> Va bene
    public List<ChefBean> getChefsInCity(ChefBean chefBean) throws FailedSearchException {
        List<ChefBean> chefInCityBeans = new ArrayList<>();

        List<Chef> chefsInCity = DaoFactory.getChefDao().getChefsInCity(chefBean.getCity());
        if (chefsInCity != null) {
            for (Chef chef : chefsInCity) {
                chefInCityBeans.add(new ChefBean(chef));
            }
        }
        return chefInCityBeans;
    }


    // Okk -> Va bene
    public List<MenuBean> getChefMenus(ChefBean chefBean) throws FailedSearchException {
        List<MenuBean> chefMenuBeans = new ArrayList<>();
        List<Menu> chefMenus = DaoFactory.getMenuDao().getChefMenus(chefBean.getId());
        if (chefMenus != null) {
            for (Menu menu : chefMenus) {
                chefMenuBeans.add(new MenuBean(menu));
            }
        }
        return chefMenuBeans;
    }




    // Okk -> Va bene
    public List<DishBean> getCourses(MenuBean menuBean) throws FailedSearchException {
        List<DishBean> coursesBean = new ArrayList<>();
        List<Dish> courses = DaoFactory.getMenuDao().getMenuCourses(menuBean.getId());
        if (courses != null) {
            DishDao dishDao = DaoFactory.getDishDao();
            for (Dish dish : courses) {
                List<Allergen> dishAllergens = dishDao.getDishAllergens(dish.getId());
                dish.setAllergens(dishAllergens);
                coursesBean.add(new DishBean(dish));
            }
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
