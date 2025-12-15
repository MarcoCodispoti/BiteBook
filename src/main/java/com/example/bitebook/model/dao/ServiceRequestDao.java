package com.example.bitebook.model.dao;

import com.example.bitebook.exceptions.FailedInsertException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.FailedUpdateException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.ServiceRequest;

import java.util.List;

public interface ServiceRequestDao{

    void saveServiceRequest(ServiceRequest serviceRequest) throws FailedInsertException;

    List<ServiceRequest> getClientServiceRequests(Client client) throws FailedSearchException;

    List<ServiceRequest> getChefServiceRequests(Chef chef) throws FailedSearchException;

    void manageRequest(ServiceRequest serviceRequest) throws FailedUpdateException;

}
