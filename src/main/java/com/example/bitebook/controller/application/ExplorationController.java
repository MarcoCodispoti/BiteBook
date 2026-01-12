package com.example.bitebook.controller.application;

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
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.model.dao.ChefDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.session.LoggedUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorationController {


    public boolean areChefsAvailableInCity(String insertedCity) throws FailedSearchException {

        ChefDao chefDao = DaoFactory.getChefDao();
        try {
            chefDao.findCityChefs(insertedCity);
            return true;
        } catch (NoChefInCityException _){
            return false;
        }
    }



    public boolean isLoggedClient() {
        return LoggedUser.getInstance().getRole() == Role.CLIENT;
    }



    public List<ChefBean> getChefsInCity(String selectedCity) throws FailedSearchException {
        List<ChefBean> chefInCityBeans = new ArrayList<>();

        List<Chef> chefsInCity = DaoFactory.getChefDao().getChefsInCity(selectedCity);
        if (chefsInCity != null) {
            for (Chef chef : chefsInCity) {
                chefInCityBeans.add(new ChefBean(chef));
            }
        }
        return chefInCityBeans;
    }



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



    public List<DishBean> getCourses(MenuBean menuBean) throws FailedSearchException {
        List<DishBean> coursesBean = new ArrayList<>();

        List<Dish> courses = DaoFactory.getDishDao().getMenuCourses(menuBean.getId());
        if (courses != null) {
            AllergenDao allergenDao = DaoFactory.getAllergenDao();
            for (Dish dish : courses) {
                List<Allergen> dishAllergens = allergenDao.getDishAllergens(dish.getId());
                dish.setAllergens(dishAllergens);
                coursesBean.add(new DishBean(dish));
            }
        }
        return coursesBean;
    }



    public List<AllergenBean> getCoursesAllergens(List<DishBean> courseBeans) {
        Map<Integer, AllergenBean> uniqueAllergensMap = new HashMap<>();

        if (courseBeans != null) {
            for (DishBean dish : courseBeans){
                List<AllergenBean> dishAllergens = dish.getAllergens();
                if (dishAllergens != null) {
                    for (AllergenBean entity : dishAllergens) {
                        if (!uniqueAllergensMap.containsKey(entity.getId())) {
                            uniqueAllergensMap.put(entity.getId(), entity);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(uniqueAllergensMap.values());
    }

}
