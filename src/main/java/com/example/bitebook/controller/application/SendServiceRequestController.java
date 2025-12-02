package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.*;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.ChefDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.singleton.LoggedUser;

import java.util.List;

public class SendServiceRequestController{


    // Okk -> Va bene
    public MenuBean getMenuLevelsSurcharge(MenuBean menuBean) throws FailedSearchException {
        Menu menu = DaoFactory.getMenuDao().getMenuLevelsSurcharge(menuBean.getId());
        if (menu != null) {
            menuBean.setPremiumLevelSurcharge(menu.getPremiumLevelSurcharge());
            menuBean.setLuxeLevelSurcharge(menu.getLuxeLevelSurcharge());
        } else {
            throw new FailedSearchException("Menu levels surcharge not found");
        }
        return menuBean;
    }




    public int calculateTotalPrice(ReservationDetailsBean reservationDetailsBean, MenuBean menuBean){
        int singleMenuSurcharge = -1;
        switch (reservationDetailsBean.getSelectedMenuLevel()){
            case BASE: singleMenuSurcharge = 0; break;
            case PREMIUM: singleMenuSurcharge = menuBean.getPremiumLevelSurcharge(); break;
            case LUXE: singleMenuSurcharge = menuBean.getLuxeLevelSurcharge(); break;
            default: break;
        }
        if(singleMenuSurcharge == -1){
            return -1;
        }
        return reservationDetailsBean.getParticipantNumber() * (menuBean.getPricePerPerson() +  singleMenuSurcharge );
    }


    // allergiesIncompatibility
    // da rinominare

    public boolean clientAllergiesIncompatibility(List<AllergenBean> menuAllergensBean){
        List<Allergen> clientAllergies = LoggedUser.getInstance().getClient().getAllergies();
        if (clientAllergies == null || clientAllergies.isEmpty()) {
            return false;
        }
        for (Allergen clientAllergen : clientAllergies) {
            for (AllergenBean menuAllergen : menuAllergensBean) {
                if (clientAllergen.getName().equals(menuAllergen.getName())) {
                    return true;
                }
            }
        }
        return false;
    }



    public ServiceRequestBean fillServiceRequest(MenuBean menuBean, ReservationDetailsBean reservationDetailsBean) throws Exception{
        ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
        ChefDao chefDao = DaoFactory.getChefDao();
        Chef chef = chefDao.getChefFromMenu(menuBean.getId());
        ChefBean chefBean =  new ChefBean();
        chefBean.setId(chef.getId());
        serviceRequestBean.setChefBean(chefBean);
        ClientBean clientBean = new ClientBean(LoggedUser.getInstance().getClient().getName(), LoggedUser.getInstance().getClient().getSurname());
        clientBean.setId(LoggedUser.getInstance().getClient().getId());
        serviceRequestBean.setClientBean(clientBean);

        serviceRequestBean.setMenuBean(menuBean);
        serviceRequestBean.setReservationDetails(reservationDetailsBean);
        serviceRequestBean.setStatus(RequestStatus.PENDING);
        return serviceRequestBean;
    }



    public void sendServiceRequest(ServiceRequestBean serviceRequestBean) throws Exception{
        ServiceRequest serviceRequest = ConvertServiceRequestBean(serviceRequestBean);
        ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
        serviceRequestDao.saveServiceRequest(serviceRequest);

    }




    private ServiceRequest ConvertServiceRequestBean(ServiceRequestBean serviceRequestBean){
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setId(serviceRequestBean.getId());
        Client client = new  Client();
        client.setId(serviceRequestBean.getClientBean().getId());
        client.setName(serviceRequestBean.getClientBean().getName());
        client.setSurname(serviceRequestBean.getClientBean().getSurname());

        serviceRequest.setClient(client);
        serviceRequest.setChef(convertChefBean(serviceRequestBean.getChefBean()));
        serviceRequest.setMenu(convertMenuBean(serviceRequestBean.getMenuBean()));
        serviceRequest.setReservationDetails(convertReservationDetailsBean(serviceRequestBean.getReservationDetails()));
        serviceRequest.setStatus(serviceRequestBean.getStatus());
        serviceRequest.setTotalPrice(calculateTotalPrice(serviceRequestBean.getReservationDetails(), serviceRequestBean.getMenuBean()));
        return serviceRequest;
    }



    private Chef convertChefBean(ChefBean chefBean){
        Chef chef = new Chef();
        chef.setId(chefBean.getId());
        chef.setName(chefBean.getName());
        chef.setSurname(chefBean.getSurname());
        return chef;
    }



    private Menu convertMenuBean(MenuBean menuBean){
        Menu menu = new Menu();
        menu.setId(menuBean.getId());
        menu.setName(menuBean.getName());
        menu.setNumberOfCourses(menuBean.getNumberOfCourses());
        menu.setDietType(menuBean.getDietType());
        return menu;
    }



    private ReservationDetails convertReservationDetailsBean(ReservationDetailsBean reservationDetailsBean){
        ReservationDetails reservationDetails = new ReservationDetails();
        reservationDetails.setDate(reservationDetailsBean.getDate());
        reservationDetails.setTime(reservationDetailsBean.getTime());
        reservationDetails.setParticipantNumber(reservationDetailsBean.getParticipantNumber());
        reservationDetails.setAddress(reservationDetailsBean.getAddress());
        reservationDetails.setSelectedMenuLevel(reservationDetailsBean.getSelectedMenuLevel());
        return reservationDetails;
    }


}
