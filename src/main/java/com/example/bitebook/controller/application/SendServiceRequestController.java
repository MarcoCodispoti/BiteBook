package com.example.bitebook.controller.application;

import com.example.bitebook.model.*;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.ChefDao;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.dao.persistence.ChefDbDao;
import com.example.bitebook.model.dao.persistence.MenuDbDao;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.singleton.LoggedUser;

import java.util.Vector;

public class SendServiceRequestController{
    public static MenuBean getMenuLevelsSurcharge(MenuBean menuBean){

        try{
            MenuDao menuDao = DaoFactory.getMenuDao();
            Menu menu = menuDao.getMenuLevelsSurcharge(menuBean.getId());
            menuBean.setPremiumLevelSurcharge(menu.getPremiumLevelSurcharge());
            menuBean.setLuxeLevelSurcharge(menu.getLuxeLevelSurcharge());
        } catch(Exception e){
            throw new RuntimeException(e);
        }
        return menuBean;
    }

    public static int calculateTotalPrice(ReservationDetailsBean reservationDetailsBean, MenuBean menuBean){
        int totalPrice = -1;
        int singleMenuSurcharge = -1;
        switch (reservationDetailsBean.getSelectedMenuLevel()){
            case BASE: singleMenuSurcharge = 0; break;
            case PREMIUM: singleMenuSurcharge = menuBean.getPremiumLevelSurcharge(); break;
            case LUXE: singleMenuSurcharge = menuBean.getLuxeLevelSurcharge(); break;
            default: break;
        }
        if(singleMenuSurcharge == -1){return -1;}
        return totalPrice = reservationDetailsBean.getParticipantNumber() * (menuBean.getPricePerPerson() +  singleMenuSurcharge );
    }


    public static boolean checkAllergies(Vector<AllergenBean> menuAllergensBean) {
        Vector<Allergen> clientAllergies = LoggedUser.getInstance().getClient().getAllergies();
        if (clientAllergies == null || clientAllergies.isEmpty()) {
            return true;
        }
        for (Allergen clientAllergen : clientAllergies) {
            for (AllergenBean menuAllergen : menuAllergensBean) {
                if (clientAllergen.getName().equals(menuAllergen.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ServiceRequestBean fillServiceRequest(MenuBean menuBean, ReservationDetailsBean reservationDetailsBean) throws Exception{
        ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
        ChefDao chefDao = DaoFactory.getChefDao();
        Chef chef = chefDao.getChefFromMenu(menuBean.getId());
        ChefBean chefBean =  new ChefBean();
        chefBean.setId(chef.getId());
        serviceRequestBean.setChefBean(chefBean);
        // serviceRequestBean.setClient(LoggedUser.getInstance().getClient());
        ClientBean clientBean = new ClientBean(LoggedUser.getInstance().getClient().getName(), LoggedUser.getInstance().getClient().getSurname());
        clientBean.setId(LoggedUser.getInstance().getClient().getId());
        serviceRequestBean.setClientBean(clientBean);

        serviceRequestBean.setMenuBean(menuBean);
        serviceRequestBean.setReservationDetails(reservationDetailsBean);
        serviceRequestBean.setStatus(RequestStatus.PENDING);
        return serviceRequestBean;
    }



    public static void sendServiceRequest(ServiceRequestBean serviceRequestBean) throws Exception{
        System.out.println("Vado a convertire la richiesta dal bean");
        ServiceRequest serviceRequest = ConvertServiceRequestBean(serviceRequestBean);
        System.out.println("" + serviceRequest.getReservationDetails().getAddress());
        System.out.println("Ho convertito la richiesta da Bean");

        System.out.println("Vado a prendere il dao dalla factory");
        ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
        System.out.println("ho preso il dao dalla factory e vado a salvare la richiesta");
        serviceRequestDao.saveServiceRequest(serviceRequest);

    }



    public static ServiceRequest ConvertServiceRequestBean(ServiceRequestBean serviceRequestBean){
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setId(serviceRequestBean.getId());
        Client client = new  Client();
        client.setId(serviceRequestBean.getClientBean().getId());
        client.setName(serviceRequestBean.getClientBean().getName());
        client.setSurname(serviceRequestBean.getClientBean().getSurname());

        System.out.println("Nome e cognome presi dal Bean " + client.getId() + " " + client.getName() + " " +  client.getSurname());

        //serviceRequest.setClient(serviceRequestBean.getClient());
        serviceRequest.setClient(client);
        serviceRequest.setChef(convertChefBean(serviceRequestBean.getChefBean()));
        serviceRequest.setMenu(convertMenuBean(serviceRequestBean.getMenuBean()));
        // System.out.println("Indirizzo dal Bean: " +  serviceRequestBean.getReservationDetails().getAddress());
        serviceRequest.setReservationDetails(convertReservationDetailsBean(serviceRequestBean.getReservationDetails()));
        serviceRequest.setStatus(serviceRequestBean.getStatus());
        serviceRequest.setTotalPrice(calculateTotalPrice(serviceRequestBean.getReservationDetails(), serviceRequestBean.getMenuBean()));
        return serviceRequest;
    }


    public static Chef convertChefBean(ChefBean chefBean){
        Chef chef = new Chef();
        chef.setId(chefBean.getId());
        chef.setName(chefBean.getName());
        chef.setSurname(chefBean.getSurname());
        return chef;
    }

    public static Menu convertMenuBean(MenuBean menuBean){
        Menu menu = new Menu();
        menu.setId(menuBean.getId());
        menu.setName(menuBean.getName());
        menu.setNumberOfCourses(menuBean.getNumberOfCourses());
        menu.setDietType(menuBean.getDietType());
        // menu.setMenuLevel(menuBean.getMenuLevel());
        return menu;
    }

    public static ReservationDetails convertReservationDetailsBean(ReservationDetailsBean reservationDetailsBean){
        ReservationDetails reservationDetails = new ReservationDetails();
        reservationDetails.setDate(reservationDetailsBean.getDate());
        reservationDetails.setTime(reservationDetailsBean.getTime());
        reservationDetails.setParticipantNumber(reservationDetailsBean.getParticipantNumber());
        reservationDetails.setAddress(reservationDetailsBean.getAddress());
        reservationDetails.setSelectedMenuLevel(reservationDetailsBean.getSelectedMenuLevel());
        return reservationDetails;
    }


}
