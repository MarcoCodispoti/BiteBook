package com.example.bitebook.controller.application;

import com.example.bitebook.model.Client;
import com.example.bitebook.model.ServiceRequest;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.singleton.LoggedUser;

import java.util.Vector;

public class RequestManagerController {

//    public Vector<ServiceRequestBean> getClientRequests(){
//        Vector<ServiceRequestBean> clientRequestBeans = new Vector<>();
//        Vector<ServiceRequest> clientRequests;
//
//        ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
//        try {
//            clientRequests = serviceRequestDao.getClientServiceRequests(LoggedUser.getInstance().getClient());
//            if(clientRequests != null){
//                for(ServiceRequest clientRequest : clientRequests){
//                    ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
//                    ChefBean chefBean = new ChefBean();
//                    MenuBean menuBean = new MenuBean();
//                    ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
//                    serviceRequestBean.setId(clientRequest.getId());
//                    chefBean.setName(clientRequest.getChef().getName());
//                    chefBean.setSurname(clientRequest.getChef().getSurname());
//                    setReservation(clientRequest, serviceRequestBean, menuBean, reservationDetailsBeans);
//                    serviceRequestBean.setStatus(clientRequest.getStatus());
//                    serviceRequestBean.setChefBean(chefBean);
//
//                    serviceRequestBean.setMenuBean(menuBean);
//                    serviceRequestBean.setReservationDetails(reservationDetailsBeans);
//                    clientRequestBeans.add(serviceRequestBean);
//                }
//            } else{
//                return null;
//            }
//        } catch (Exception e){
//            // to be handled
//            return null;
//        }
//        return clientRequestBeans;
//    }
//
//
//    public Vector<ServiceRequestBean> getApprovedServiceRequests() throws Exception{
//        Vector<ServiceRequestBean> chefServiceRequestBeans = new Vector<>();
//        Vector<ServiceRequest> chefServiceRequests;
//
//        try{
//            ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
//            chefServiceRequests = serviceRequestDao.getChefServiceRequests(LoggedUser.getInstance().getChef());
//
//            for(ServiceRequest chefServiceRequest : chefServiceRequests){
//                if(chefServiceRequest.getStatus().equals(RequestStatus.APPROVED)) {
//                    ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
//                    MenuBean menuBean = new MenuBean();
//                    ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
//                    serviceRequestBean.setId(chefServiceRequest.getId());
//                    setReservation(chefServiceRequest, serviceRequestBean, menuBean, reservationDetailsBeans);
//                    serviceRequestBean.setClientBean(new ClientBean(chefServiceRequest.getClient().getName(),chefServiceRequest.getClient().getSurname()));
//
//                    serviceRequestBean.setMenuBean(menuBean);
//                    serviceRequestBean.setReservationDetails(reservationDetailsBeans);
//                    chefServiceRequestBeans.add(serviceRequestBean);
//                }
//            }
//        } catch(Exception e){
//            // to be handled
//            // return null;
//            throw new Exception();
//        }
//        return chefServiceRequestBeans;
//    }
//
//
//
//
//    public Vector<ServiceRequestBean> getChefRequests() throws Exception{
//        Vector<ServiceRequestBean> chefServiceRequestBeans = new Vector<>();
//        Vector<ServiceRequest> chefServiceRequests;
//
//        try{
//            ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
//            chefServiceRequests = serviceRequestDao.getChefServiceRequests(LoggedUser.getInstance().getChef());
//            if(!chefServiceRequests.isEmpty()){
//
//                for(ServiceRequest chefServiceRequest : chefServiceRequests){
//
//                    ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
//                    Client client = new Client();
//                    MenuBean menuBean = new MenuBean();
//                    ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
//                    serviceRequestBean.setId(chefServiceRequest.getId());
//                    client.setName(chefServiceRequest.getClient().getName());
//                    client.setSurname(chefServiceRequest.getClient().getSurname());
//                    setReservation(chefServiceRequest, serviceRequestBean, menuBean, reservationDetailsBeans);
//                    serviceRequestBean.setStatus(chefServiceRequest.getStatus());
//
//                    serviceRequestBean.setClientBean(new ClientBean(client.getName(), client.getSurname()));
//
//                    serviceRequestBean.setMenuBean(menuBean);
//                    serviceRequestBean.setReservationDetails(reservationDetailsBeans);
//                    chefServiceRequestBeans.add(serviceRequestBean);
//                }
//
//            } else{
//                // System.out.println("Chef service requests empty");
//                throw new Exception();  // da togliere
//            }
//
//        } catch(Exception e){
//            // to be handled
//            throw new Exception();
//        }
//        return chefServiceRequestBeans;
//    }
//
//
//    public void manageRequest(ServiceRequestBean serviceRequestBean) throws Exception{
//        ServiceRequest serviceRequest = new ServiceRequest();
//        serviceRequest.setId(serviceRequestBean.getId());
//        serviceRequest.setStatus(serviceRequestBean.getStatus());
//        ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
//        serviceRequestDao.approveRequest(serviceRequest);
//    }
//
//    private void setReservation(ServiceRequest chefServiceRequest, ServiceRequestBean serviceRequestBean, MenuBean menuBean, ReservationDetailsBean reservationDetailsBeans) {
//        menuBean.setName(chefServiceRequest.getMenu().getName());
//        reservationDetailsBeans.setSelectedMenuLevel(chefServiceRequest.getReservationDetails().getSelectedMenuLevel());
//        reservationDetailsBeans.setParticipantNumber(chefServiceRequest.getReservationDetails().getParticipantNumber());
//        serviceRequestBean.setTotalPrice(chefServiceRequest.getTotalPrice());
//        reservationDetailsBeans.setDate(chefServiceRequest.getReservationDetails().getDate());
//        reservationDetailsBeans.setTime(chefServiceRequest.getReservationDetails().getTime());
//        reservationDetailsBeans.setAddress(chefServiceRequest.getReservationDetails().getAddress());
//    }




//    private void setReservationInfo(ServiceRequest chefServiceRequest, ServiceRequestBean serviceRequestBean, MenuBean menuBean, ReservationDetailsBean reservationDetailsBeans) {
//        menuBean.setName(chefServiceRequest.getMenu().getName());
//        reservationDetailsBeans.setSelectedMenuLevel(chefServiceRequest.getReservationDetails().getSelectedMenuLevel());
//        reservationDetailsBeans.setParticipantNumber(chefServiceRequest.getReservationDetails().getParticipantNumber());
//        serviceRequestBean.setTotalPrice(chefServiceRequest.getTotalPrice());
//        reservationDetailsBeans.setDate(chefServiceRequest.getReservationDetails().getDate());
//        reservationDetailsBeans.setTime(chefServiceRequest.getReservationDetails().getTime());
//        reservationDetailsBeans.setAddress(chefServiceRequest.getReservationDetails().getAddress());
//    }



    public Vector<ServiceRequestBean> getClientRequests() {
        Vector<ServiceRequestBean> resultBeans = new Vector<>();
        try {
            ServiceRequestDao dao = DaoFactory.getServiceRequestDao();
            // Assicurati che LoggedUser non ritorni null
            Vector<ServiceRequest> requests = dao.getClientServiceRequests(LoggedUser.getInstance().getClient());

            if (requests != null) {
                for (ServiceRequest req : requests) {
                    // RIUTILIZZO IL METODO PRIVATO (DRY!)
                    resultBeans.add(convertToBean(req));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Almeno stampa l'errore!
            return null;
        }
        return resultBeans;
    }

    // --- LATO CHEF (Richieste Approvate) ---
    public Vector<ServiceRequestBean> getApprovedServiceRequests() throws Exception {
        Vector<ServiceRequestBean> resultBeans = new Vector<>();
        try {
            ServiceRequestDao dao = DaoFactory.getServiceRequestDao();
            Vector<ServiceRequest> requests = dao.getChefServiceRequests(LoggedUser.getInstance().getChef());

            if (requests != null) {
                for (ServiceRequest req : requests) {
                    if (req.getStatus().equals(RequestStatus.APPROVED)) {
                        resultBeans.add(convertToBean(req));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione se vuoi gestirla nella UI
        }
        return resultBeans;
    }

    // --- LATO CHEF (Tutte le richieste/Pending) ---
    public Vector<ServiceRequestBean> getChefRequests() throws Exception {
        Vector<ServiceRequestBean> resultBeans = new Vector<>();
        try {
            ServiceRequestDao dao = DaoFactory.getServiceRequestDao();
            Vector<ServiceRequest> requests = dao.getChefServiceRequests(LoggedUser.getInstance().getChef());

            if (requests != null && !requests.isEmpty()) {
                for (ServiceRequest req : requests) {
                    resultBeans.add(convertToBean(req));
                }
            } else {
                // Non lanciare eccezioni se è vuoto, ritorna semplicemente il vettore vuoto
                // return resultBeans;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return resultBeans;
    }

    public void manageRequest(ServiceRequestBean serviceRequestBean) throws Exception {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setId(serviceRequestBean.getId());
        serviceRequest.setStatus(serviceRequestBean.getStatus());
        DaoFactory.getServiceRequestDao().approveRequest(serviceRequest);
    }

    // ---------------------------------------------------------
    // METODO PRIVATO DI CONVERSIONE (HELPER)
    // Tutta la logica "brutta" di set è qui, scritta UNA VOLTA SOLA
    // ---------------------------------------------------------
    private ServiceRequestBean convertToBean(ServiceRequest entity) {
        ServiceRequestBean bean = new ServiceRequestBean();
        bean.setId(entity.getId());
        bean.setStatus(entity.getStatus());
        bean.setTotalPrice(entity.getTotalPrice());

        // Conversione Chef (se presente)
        if (entity.getChef() != null) {
            ChefBean cb = new ChefBean();
            cb.setName(entity.getChef().getName());
            cb.setSurname(entity.getChef().getSurname());
            bean.setChefBean(cb);
        }

        // Conversione Client (se presente)
        if (entity.getClient() != null) {
            bean.setClientBean(new ClientBean(entity.getClient().getName(), entity.getClient().getSurname()));
        }

        // Conversione Menu
        if (entity.getMenu() != null) {
            MenuBean mb = new MenuBean();
            mb.setName(entity.getMenu().getName());
            bean.setMenuBean(mb);
        }

        // Conversione Reservation
        if (entity.getReservationDetails() != null) {
            ReservationDetailsBean rdb = new ReservationDetailsBean();
            rdb.setSelectedMenuLevel(entity.getReservationDetails().getSelectedMenuLevel());
            rdb.setParticipantNumber(entity.getReservationDetails().getParticipantNumber());
            rdb.setDate(entity.getReservationDetails().getDate());
            rdb.setTime(entity.getReservationDetails().getTime());
            rdb.setAddress(entity.getReservationDetails().getAddress());
            bean.setReservationDetails(rdb);
        }

        return bean;
    }




}
