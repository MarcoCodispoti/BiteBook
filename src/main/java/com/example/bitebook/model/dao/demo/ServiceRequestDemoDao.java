package com.example.bitebook.model.dao.demo;

import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.ServiceRequest;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.enums.RequestStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRequestDemoDao implements ServiceRequestDao {


    private static final Map<Integer, ServiceRequest> fakeTable = new HashMap<>();
    private static int autoIncrementId = 1;

    private static synchronized int getNextId() {
        return autoIncrementId++;
    }


    @Override
    public void saveServiceRequest(ServiceRequest serviceRequest){
        serviceRequest.setId(getNextId());
        if (serviceRequest.getStatus() == null) {
            serviceRequest.setStatus(RequestStatus.PENDING);
        }
        fakeTable.put(serviceRequest.getId(), serviceRequest);
    }


    @Override
    public List<ServiceRequest> getClientServiceRequests(Client client) {
        List<ServiceRequest> result = new ArrayList<>();

        for (ServiceRequest req : fakeTable.values()) {
            if (req.getClient() != null && req.getClient().getId() == client.getId()){
                result.add(req);
            }
        }
        return result;
    }


    @Override
    public List<ServiceRequest> getChefServiceRequests(Chef chef) {
        List<ServiceRequest> result = new ArrayList<>();
        for (ServiceRequest req : fakeTable.values()) {
            if (req.getChef() != null && req.getChef().getId() == chef.getId()) {
                result.add(req);
            }
        }
        return result;
    }


    @Override
    public void manageRequest(ServiceRequest incomingRequest) throws FailedUpdateException {
        ServiceRequest storedRequest = fakeTable.get(incomingRequest.getId());

        if (storedRequest == null) {
            throw new FailedUpdateException("Request not found in the system", null);
        }

        RequestStatus newStatus = incomingRequest.getStatus();

        if (newStatus == RequestStatus.APPROVED || newStatus == RequestStatus.REJECTED) {
            if (storedRequest.getStatus() == RequestStatus.PENDING) {
                storedRequest.setStatus(newStatus);
            } else {
                throw new FailedUpdateException("Unable to modify the status, requests already processed", null);
            }
        } else {
            throw new FailedUpdateException("Status not valid for the update", null);
        }
    }


}
