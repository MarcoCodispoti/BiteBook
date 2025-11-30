package com.example.bitebook.controller.application;

import com.example.bitebook.model.Client;
import com.example.bitebook.model.ServiceRequest;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.singleton.LoggedUser;

import java.util.Vector;

public class ManageRequestController {

    // Da spostare in Exploration controller??
    public Vector<ServiceRequestBean> getChefRequests() throws Exception{
        Vector<ServiceRequestBean> chefServiceRequestBeans = new Vector<>();
        Vector<ServiceRequest> chefServiceRequests;

        try{
            ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
            chefServiceRequests = serviceRequestDao.getChefServiceRequests(LoggedUser.getInstance().getChef());
            if(!chefServiceRequests.isEmpty()){

                for(ServiceRequest chefServiceRequest : chefServiceRequests){

                    ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
                    Client client = new Client();
                    MenuBean menuBean = new MenuBean();
                    ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
                    serviceRequestBean.setId(chefServiceRequest.getId());
                    client.setName(chefServiceRequest.getClient().getName());
                    client.setSurname(chefServiceRequest.getClient().getSurname());
                    menuBean.setName(chefServiceRequest.getMenu().getName());
                    reservationDetailsBeans.setSelectedMenuLevel(chefServiceRequest.getReservationDetails().getSelectedMenuLevel());
                    reservationDetailsBeans.setParticipantNumber(chefServiceRequest.getReservationDetails().getParticipantNumber());
                    serviceRequestBean.setTotalPrice(chefServiceRequest.getTotalPrice());
                    reservationDetailsBeans.setDate(chefServiceRequest.getReservationDetails().getDate());
                    reservationDetailsBeans.setTime(chefServiceRequest.getReservationDetails().getTime());
                    reservationDetailsBeans.setAddress(chefServiceRequest.getReservationDetails().getAddress());
                    serviceRequestBean.setStatus(chefServiceRequest.getStatus());

                    serviceRequestBean.setClientBean(new ClientBean(client.getName(), client.getSurname()));

                    serviceRequestBean.setMenuBean(menuBean);
                    serviceRequestBean.setReservationDetails(reservationDetailsBeans);
                    chefServiceRequestBeans.add(serviceRequestBean);
                }

            } else{
                // System.out.println("Chef service requests empty");
                throw new Exception();  // da togliere
            }

        } catch(Exception e){
            // to be handled
            throw new Exception();
        }
        return chefServiceRequestBeans;
    }


    public void manageRequest(ServiceRequestBean serviceRequestBean) throws Exception{
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setId(serviceRequestBean.getId());
        serviceRequest.setStatus(serviceRequestBean.getStatus());
        ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
        serviceRequestDao.approveRequest(serviceRequest);
    }

}
