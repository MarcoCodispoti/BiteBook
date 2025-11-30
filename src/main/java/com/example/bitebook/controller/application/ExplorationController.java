package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.*;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.dao.persistence.ChefDbDao;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.singleton.LoggedUser;

import java.sql.SQLException;
import java.util.Vector;

public class ExplorationController{

    public Boolean checkCityChefs(ChefBean chefBean){
        ChefDbDao chefDbDao = new ChefDbDao();
        try{
            chefDbDao.findCityChefs(chefBean.getCity());
        } catch (NoChefInCityException e) {
            return false;
        }
        return true;
    }


    public boolean isLoggedClient(){
        return LoggedUser.getInstance().getClient() != null ;
    }


    public Vector<ChefBean> getChefsInCity(ChefBean chefBean){
        Vector<Chef> chefsInCity;
        Vector<ChefBean> chefInCityBeans = new Vector<>();
        ChefDao chefDao = DaoFactory.getChefDao();
        try{
            chefsInCity = chefDao.getChefsInCity(chefBean.getCity());
        if(chefsInCity == null || chefsInCity.isEmpty()){
            throw new SQLException();
        }

        for(Chef chef : chefsInCity){
            ChefBean newChefbean = new ChefBean(chef);
            chefInCityBeans.add(newChefbean);
        }


        } catch(SQLException e){
            // to be handled
            return null;
        }
        return chefInCityBeans;
    }

    // il controller applicativo non dovrebbe contenere blocchi try-catch (per best practice)
    // dovrebbe o gestire completamente le eccezioni o limitarsi a rilanciare quelle che si presentano in esso

    // Dividere questo controller in due controller: ExploreChefController e ExploreMenuController ??

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


    public Vector<ServiceRequestBean> getClientRequests(){
        Vector<ServiceRequestBean> clientRequestBeans = new Vector<>();
        Vector<ServiceRequest> clientRequests;

        ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
        try {
            clientRequests = serviceRequestDao.getClientServiceRequests(LoggedUser.getInstance().getClient());
            if(clientRequests != null){
                for(ServiceRequest clientRequest : clientRequests){
                    ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
                    ChefBean chefBean = new ChefBean();
                    MenuBean menuBean = new MenuBean();
                    ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
                    serviceRequestBean.setId(clientRequest.getId());
                    chefBean.setName(clientRequest.getChef().getName());
                    chefBean.setSurname(clientRequest.getChef().getSurname());
                    setReservationInfo(clientRequest, serviceRequestBean, menuBean, reservationDetailsBeans);
                    serviceRequestBean.setStatus(clientRequest.getStatus());
                    serviceRequestBean.setChefBean(chefBean);

                    serviceRequestBean.setMenuBean(menuBean);
                    serviceRequestBean.setReservationDetails(reservationDetailsBeans);
                    clientRequestBeans.add(serviceRequestBean);
                }
            } else{
                return null;
            }
        } catch (Exception e){
            // to be handled
            return null;
        }
        return clientRequestBeans;
    }



    public Vector<ServiceRequestBean> getApprovedServiceRequests() throws Exception{
            Vector<ServiceRequestBean> chefServiceRequestBeans = new Vector<>();
            Vector<ServiceRequest> chefServiceRequests;

            try{
                ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
                chefServiceRequests = serviceRequestDao.getChefServiceRequests(LoggedUser.getInstance().getChef());
//                if(!(chefServiceRequestBeans == null)){

                    for(ServiceRequest chefServiceRequest : chefServiceRequests){
                        if(chefServiceRequest.getStatus().equals(RequestStatus.APPROVED)) {
                            ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
                            MenuBean menuBean = new MenuBean();
                            ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
                            serviceRequestBean.setId(chefServiceRequest.getId());
                            setReservationInfo(chefServiceRequest, serviceRequestBean, menuBean, reservationDetailsBeans);
                            serviceRequestBean.setClientBean(new ClientBean(chefServiceRequest.getClient().getName(),chefServiceRequest.getClient().getSurname()));

                            serviceRequestBean.setMenuBean(menuBean);
                            serviceRequestBean.setReservationDetails(reservationDetailsBeans);
                            chefServiceRequestBeans.add(serviceRequestBean);
                        }
                    }
//                } else{
//                    // throw new Exception();
//                }

            } catch(Exception e){
                // to be handled
                // return null;
                throw new Exception();
            }
            return chefServiceRequestBeans;
        }


    private void setReservationInfo(ServiceRequest chefServiceRequest, ServiceRequestBean serviceRequestBean, MenuBean menuBean, ReservationDetailsBean reservationDetailsBeans) {
        menuBean.setName(chefServiceRequest.getMenu().getName());
        reservationDetailsBeans.setSelectedMenuLevel(chefServiceRequest.getReservationDetails().getSelectedMenuLevel());
        reservationDetailsBeans.setParticipantNumber(chefServiceRequest.getReservationDetails().getParticipantNumber());
        serviceRequestBean.setTotalPrice(chefServiceRequest.getTotalPrice());
        reservationDetailsBeans.setDate(chefServiceRequest.getReservationDetails().getDate());
        reservationDetailsBeans.setTime(chefServiceRequest.getReservationDetails().getTime());
        reservationDetailsBeans.setAddress(chefServiceRequest.getReservationDetails().getAddress());
    }


}
