package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.ServiceRequest;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.factory.AbstractDaoFactory;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.session.LoggedUser;
import com.example.bitebook.util.DaoConfigurator;

import java.util.ArrayList;
import java.util.List;

public class ManageServiceRequestController {


    public List<ServiceRequestBean> getClientRequests() throws FailedSearchException {
        List<ServiceRequestBean> resultBeans = new ArrayList<>();

        List<ServiceRequest> requests = getDaoFactory().getServiceRequestDao()
                .getClientServiceRequests(LoggedUser.getInstance().getClient());
        if (requests != null) {
            for (ServiceRequest req : requests) {
                resultBeans.add(convertRequestToBean(req));
            }
        }
        return resultBeans;
    }



    public List<ServiceRequestBean> getApprovedServiceRequests() throws FailedSearchException {
        List<ServiceRequestBean> resultBeans = new ArrayList<>();

        List<ServiceRequest> requests = getDaoFactory().getServiceRequestDao()
                .getChefServiceRequests(LoggedUser.getInstance().getChef());
        if (requests != null) {
            for (ServiceRequest req : requests){
                if (req.getStatus() == RequestStatus.APPROVED) {
                    resultBeans.add(convertRequestToBean(req));
                }
            }
        }
        return resultBeans;
    }



    public List<ServiceRequestBean> getChefRequests() throws FailedSearchException {
        List<ServiceRequestBean> resultBeans = new ArrayList<>();

        List<ServiceRequest> requests = getDaoFactory().getServiceRequestDao()
                .getChefServiceRequests(LoggedUser.getInstance().getChef());
        if (requests != null) {
            for (ServiceRequest req : requests) {
                resultBeans.add(convertRequestToBean(req));
            }
        }
        return resultBeans;
    }



    public void updateRequestStatus(ServiceRequestBean serviceRequestBean) throws FailedUpdateException {
        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setId(serviceRequestBean.getId());
        serviceRequest.setStatus(serviceRequestBean.getStatus());

        getDaoFactory().getServiceRequestDao().manageRequest(serviceRequest);
    }



    private ServiceRequestBean convertRequestToBean(ServiceRequest request) {
        ServiceRequestBean bean = new ServiceRequestBean();

        bean.setId(request.getId());
        bean.setStatus(request.getStatus());
        bean.setTotalPrice(request.getTotalPrice());

        if (request.getChef() != null) {
            ChefBean cb = new ChefBean();
            cb.setName(request.getChef().getName());
            cb.setSurname(request.getChef().getSurname());
            bean.setChefBean(cb);
        }

        if (request.getClient() != null) {
            bean.setClientBean(new ClientBean(request.getClient().getName(), request.getClient().getSurname()));
        }

        if (request.getMenu() != null) {
            MenuBean mb = new MenuBean();
            mb.setName(request.getMenu().getName());
            bean.setMenuBean(mb);
        }

        if (request.getReservationDetails() != null) {
            ReservationDetailsBean rdb = new ReservationDetailsBean();

            rdb.setSelectedMenuLevel(request.getReservationDetails().getSelectedMenuLevel());
            rdb.setParticipantNumber(request.getReservationDetails().getParticipantNumber());
            rdb.setDate(request.getReservationDetails().getDate());
            rdb.setTime(request.getReservationDetails().getTime());
            rdb.setAddress(request.getReservationDetails().getAddress());
            bean.setReservationDetailsBean(rdb);
        }
        return bean;
    }



    private AbstractDaoFactory getDaoFactory(){
        return DaoConfigurator.getInstance().getFactory();
    }


}
