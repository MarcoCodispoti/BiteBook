package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.ServiceRequest;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.DaoFactory;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.session.LoggedUser;

import java.util.ArrayList;
import java.util.List;

public class RequestManagerController {


    public List<ServiceRequestBean> getClientRequests() throws FailedSearchException {
        List<ServiceRequestBean> resultBeans = new ArrayList<>();

        List<ServiceRequest> requests = DaoFactory.getServiceRequestDao()
                .getClientServiceRequests(LoggedUser.getInstance().getClient());
        if (requests != null) {
            for (ServiceRequest req : requests) {
                resultBeans.add(convertToBean(req));
            }
        }
        return resultBeans;
    }



    public List<ServiceRequestBean> getApprovedServiceRequests() throws FailedSearchException {
        List<ServiceRequestBean> resultBeans = new ArrayList<>();

        List<ServiceRequest> requests = DaoFactory.getServiceRequestDao()
                .getChefServiceRequests(LoggedUser.getInstance().getChef());
        if (requests != null) {
            for (ServiceRequest req : requests){
                if (req.getStatus() == RequestStatus.APPROVED) {
                    resultBeans.add(convertToBean(req));
                }
            }
        }
        return resultBeans;
    }



    public List<ServiceRequestBean> getChefRequests() throws FailedSearchException {
        List<ServiceRequestBean> resultBeans = new ArrayList<>();

        List<ServiceRequest> requests = DaoFactory.getServiceRequestDao()
                .getChefServiceRequests(LoggedUser.getInstance().getChef());
        if (requests != null) {
            for (ServiceRequest req : requests) {
                resultBeans.add(convertToBean(req));
            }
        }
        return resultBeans;
    }



    public void manageRequest(ServiceRequestBean serviceRequestBean) throws FailedUpdateException {
        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setId(serviceRequestBean.getId());
        serviceRequest.setStatus(serviceRequestBean.getStatus());

        DaoFactory.getServiceRequestDao().manageRequest(serviceRequest);
    }



    private ServiceRequestBean convertToBean(ServiceRequest entity) {
        ServiceRequestBean bean = new ServiceRequestBean();

        bean.setId(entity.getId());
        bean.setStatus(entity.getStatus());
        bean.setTotalPrice(entity.getTotalPrice());

        if (entity.getChef() != null) {
            ChefBean cb = new ChefBean();
            cb.setName(entity.getChef().getName());
            cb.setSurname(entity.getChef().getSurname());
            bean.setChefBean(cb);
        }

        if (entity.getClient() != null) {
            bean.setClientBean(new ClientBean(entity.getClient().getName(), entity.getClient().getSurname()));
        }

        if (entity.getMenu() != null) {
            MenuBean mb = new MenuBean();
            mb.setName(entity.getMenu().getName());
            bean.setMenuBean(mb);
        }

        if (entity.getReservationDetails() != null) {
            ReservationDetailsBean rdb = new ReservationDetailsBean();

            rdb.setSelectedMenuLevel(entity.getReservationDetails().getSelectedMenuLevel());
            rdb.setParticipantNumber(entity.getReservationDetails().getParticipantNumber());
            rdb.setDate(entity.getReservationDetails().getDate());
            rdb.setTime(entity.getReservationDetails().getTime());
            rdb.setAddress(entity.getReservationDetails().getAddress());

            bean.setReservationDetailsBean(rdb);
        }
        return bean;
    }


}
