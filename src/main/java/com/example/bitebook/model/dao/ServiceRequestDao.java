package com.example.bitebook.model.dao;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.ServiceRequest;

import java.util.List;

public interface ServiceRequestDao{

    public void saveServiceRequest(ServiceRequest serviceRequest) throws Exception;

    public List<ServiceRequest> getClientServiceRequests(Client client) throws Exception;

    public List<ServiceRequest> getChefServiceRequests(Chef chef) throws Exception;

    public void manageRequest(ServiceRequest serviceRequest) throws Exception;
}
