package com.example.bitebook.model.dao;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.ServiceRequest;

import java.util.Vector;

public interface ServiceRequestDao{

    public void saveServiceRequest(ServiceRequest serviceRequest) throws Exception;

    public Vector<ServiceRequest> getClientServiceRequests(Client client) throws Exception;

    public Vector<ServiceRequest> getChefServiceRequests(Chef chef) throws Exception;

    public void approveRequest(ServiceRequest serviceRequest) throws Exception;
}
