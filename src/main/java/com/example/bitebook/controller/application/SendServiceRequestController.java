package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.*;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.session.LoggedUser;
import com.example.bitebook.util.DaoConfigurator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SendServiceRequestController {


    public MenuBean populateMenuSurcharges(MenuBean menuBean) throws FailedSearchException {

        Menu menu = DaoConfigurator.getInstance().getFactory().getMenuDao().populateMenuLevelsSurcharge(menuBean.getId());
        if (menu != null) {
            menuBean.setPremiumLevelSurcharge(menu.getPremiumLevelSurcharge());
            menuBean.setLuxeLevelSurcharge(menu.getLuxeLevelSurcharge());
        } else {
            throw new FailedSearchException("Menu levels surcharges not found");
        }
        return menuBean;
    }



    public int calculateTotalPrice(ReservationDetailsBean reservationDetails, MenuBean menuBean) {

        if (reservationDetails.getParticipantNumber() <= 0 || reservationDetails.getSelectedMenuLevel() == null) {
            throw new IllegalArgumentException("Error: Cannot calculate total price: Insufficient or wrong reservation details data");
        }

        if (menuBean == null || menuBean.getPricePerPerson() <= 0  || menuBean.getPremiumLevelSurcharge() <= 0 || menuBean.getLuxeLevelSurcharge() <= 0 || menuBean.getLuxeLevelSurcharge() <= menuBean.getPremiumLevelSurcharge()) {
            throw new IllegalArgumentException("Error: Cannot calculate total price: Insufficient or wrong menu details data");
        }

        int singleMenuSurcharge;
        switch (reservationDetails.getSelectedMenuLevel()) {
            case BASE -> singleMenuSurcharge = 0;
            case PREMIUM -> singleMenuSurcharge = menuBean.getPremiumLevelSurcharge();
            case LUXE -> singleMenuSurcharge = menuBean.getLuxeLevelSurcharge();
            default -> throw new IllegalArgumentException("Error: Level not supported " + reservationDetails.getSelectedMenuLevel());
        }
        return reservationDetails.getParticipantNumber() * (menuBean.getPricePerPerson() + singleMenuSurcharge);
    }



    public boolean hasAllergyConflict(List<AllergenBean> menuAllergensBean) throws IllegalStateException{

        if(menuAllergensBean == null){
            throw new IllegalStateException("Error: Error while obtaining menu allergens");
        }

        if (menuAllergensBean.isEmpty()) {
            return false;
        }

        Client client = LoggedUser.getInstance().getClient();
        if (client == null) {
            throw new IllegalStateException("Unable to get client info");
        }

        List<Allergen> clientAllergies = client.getAllergies();
        if (clientAllergies == null) {
            throw new IllegalStateException("User data error: Unable to get client allergies info");
        }

        if (clientAllergies.isEmpty()) {
            return false;
        }

        Set<Integer> clientAllergyIds = new HashSet<>();
        for (Allergen allergen : clientAllergies) {
            clientAllergyIds.add(allergen.getId());
        }

        for (AllergenBean menuAllergen : menuAllergensBean) {
            if (clientAllergyIds.contains(menuAllergen.getId())) {
                return true;
            }
        }
        return false;
    }



    public ServiceRequestBean createServiceRequest(MenuBean menuBean, ReservationDetailsBean reservationDetailsBean) throws FailedSearchException {
        ServiceRequestBean serviceRequestBean = new ServiceRequestBean();

        Chef chef = DaoConfigurator.getInstance().getFactory().getChefDao().getChefFromMenu(menuBean.getId());
        if(chef == null) {
            throw new FailedSearchException("Unable to find the chef of the menu");
        }

        ChefBean chefBean = new ChefBean();

        chefBean.setId(chef.getId());
        chefBean.setName(chef.getName());
        chefBean.setSurname(chef.getSurname());
        serviceRequestBean.setChefBean(chefBean);

        Client loggedClient = LoggedUser.getInstance().getClient();
        ClientBean clientBean = new ClientBean(loggedClient.getName(), loggedClient.getSurname());
        clientBean.setId(loggedClient.getId());
        serviceRequestBean.setClientBean(clientBean);

        serviceRequestBean.setMenuBean(menuBean);
        serviceRequestBean.setReservationDetailsBean(reservationDetailsBean);
        serviceRequestBean.setStatus(RequestStatus.PENDING);
        serviceRequestBean.setTotalPrice(calculateTotalPrice(reservationDetailsBean,menuBean));

        return serviceRequestBean;
    }



    public void sendServiceRequest(ServiceRequestBean serviceRequestBean) throws FailedInsertException {
        if(!serviceRequestBean.validate()){
            throw new IllegalArgumentException("Error: Invalid request");
        }
        ServiceRequest serviceRequest = convertServiceRequestBean(serviceRequestBean);
        DaoConfigurator.getInstance().getFactory().getServiceRequestDao().saveServiceRequest(serviceRequest);
    }



    private ServiceRequest convertServiceRequestBean(ServiceRequestBean serviceRequestBean) {
        if (serviceRequestBean == null) {
            return null;
        }
        ReservationDetailsBean reservationDetailsBean = serviceRequestBean.getReservationDetailsBean();

        ServiceRequest serviceRequest = new ServiceRequest(
                reservationDetailsBean.getDate(),
                reservationDetailsBean.getTime(),
                reservationDetailsBean.getAddress(),
                reservationDetailsBean.getParticipantNumber(),
                reservationDetailsBean.getSelectedMenuLevel()
        );

        serviceRequest.setId(serviceRequestBean.getId());
        serviceRequest.setClient(convertClientBean(serviceRequestBean.getClientBean()));
        serviceRequest.setChef(convertChefBean(serviceRequestBean.getChefBean()));
        serviceRequest.setMenu(convertMenuBean(serviceRequestBean.getMenuBean()));
        serviceRequest.setStatus(serviceRequestBean.getStatus());

        if (serviceRequestBean.getReservationDetailsBean() != null && serviceRequestBean.getMenuBean() != null) {
            serviceRequest.setTotalPrice(calculateTotalPrice(
                    serviceRequestBean.getReservationDetailsBean(),
                    serviceRequestBean.getMenuBean()
            ));
        }
        return serviceRequest;
    }



    private Client convertClientBean(ClientBean clientBean) {
        if (clientBean == null) return null;
        Client client = new Client();

        client.setId(clientBean.getId());
        client.setName(clientBean.getName());
        client.setSurname(clientBean.getSurname());

        return client;
    }



    private Chef convertChefBean(ChefBean chefBean) {
        if (chefBean == null) return null;
        Chef chef = new Chef();

        chef.setId(chefBean.getId());
        chef.setName(chefBean.getName());
        chef.setSurname(chefBean.getSurname());

        return chef;
    }



    private Menu convertMenuBean(MenuBean menuBean) {
        if (menuBean == null) return null;
        Menu menu = new Menu();

        menu.setId(menuBean.getId());
        menu.setName(menuBean.getName());
        menu.setNumberOfCourses(menuBean.getNumberOfCourses());
        menu.setDietType(menuBean.getDietType());
        menu.setPremiumLevelSurcharge(menuBean.getPremiumLevelSurcharge());
        menu.setLuxeLevelSurcharge(menuBean.getLuxeLevelSurcharge());

        return menu;
    }


}
