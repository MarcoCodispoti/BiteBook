package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.*;
import com.example.bitebook.model.*;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.enums.MenuLevel;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.util.Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDbDao implements ServiceRequestDao {



    @Override
    public void saveServiceRequest(ServiceRequest serviceRequest) throws FailedInsertException {
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call saveServiceRequest(?,?,?,?,?,?,?,?,?,?)}")){
            cstmt.setInt(1, serviceRequest.getClient().getId());
            cstmt.setInt(2, serviceRequest.getChef().getId());
            cstmt.setInt(3, serviceRequest.getMenu().getId());
            cstmt.setString(4, serviceRequest.getReservationDetails().getSelectedMenuLevel().toString());
            cstmt.setInt(5, serviceRequest.getTotalPrice());
            cstmt.setString(6, serviceRequest.getStatus().toString());
            cstmt.setDate(7, java.sql.Date.valueOf(serviceRequest.getReservationDetails().getDate()));
            cstmt.setTime(8, java.sql.Time.valueOf(serviceRequest.getReservationDetails().getTime()));
            cstmt.setString(9, serviceRequest.getReservationDetails().getAddress());
            cstmt.setInt(10, serviceRequest.getReservationDetails().getParticipantNumber());
            cstmt.execute();
        } catch (SQLException e){
            throw new FailedInsertException("Error while saving the request", new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedInsertException(e);
        }
    }



    @Override
    public List<ServiceRequest> getClientServiceRequests(Client client) throws FailedSearchException {
        List<ServiceRequest> serviceRequests = new ArrayList<>();
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call getClientRequests(?)}")) {
            cstmt.setInt(1, client.getId());
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    ServiceRequest request = new ServiceRequest();
                    request.setId(rs.getInt("RequestId"));
                    request.setTotalPrice(rs.getInt("TotalPrice"));
                    request.setStatus(RequestStatus.valueOf(rs.getString("Status")));
                    Chef chef = new Chef();
                    chef.setName(rs.getString("ChefName"));
                    chef.setSurname(rs.getString("ChefSurname"));
                    request.setChef(chef);
                    setRequestDetails(serviceRequests, rs, request, client);
                }
            }
        } catch (SQLException e) {
            throw new FailedSearchException("Errore recupero richieste del cliente", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return serviceRequests;
    }



    @Override
    public List<ServiceRequest> getChefServiceRequests(Chef chef) throws FailedSearchException {
        List<ServiceRequest> serviceRequests = new ArrayList<>();
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call getChefRequests(?)}")) {
            cstmt.setInt(1, chef.getId());
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    ServiceRequest request = new ServiceRequest();
                    request.setId(rs.getInt("RequestId"));
                    request.setTotalPrice(rs.getInt("TotalPrice"));
                    request.setStatus(RequestStatus.valueOf(rs.getString("Status")));
                    request.setChef(chef);
                    Client client = new Client();
                    client.setName(rs.getString("ClientName"));
                    client.setSurname(rs.getString("ClientSurname"));
                    setRequestDetails(serviceRequests, rs, request, client);
                }
            }
        } catch (SQLException e) {
            throw new FailedSearchException("Error while recovering chefs requests", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return serviceRequests;
    }



    @Override
    public void manageRequest(ServiceRequest serviceRequest) throws FailedUpdateException {
        String sql;
        if (serviceRequest.getStatus() == RequestStatus.APPROVED) {
            sql = "{call approveRequest(?)}";
        } else if (serviceRequest.getStatus() == RequestStatus.REJECTED) {
            sql = "{call rejectRequest(?)}";
        } else {
            throw new FailedUpdateException("Request status not valid " + serviceRequest.getStatus(), null);
        }
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, serviceRequest.getId());
            cstmt.execute();
        } catch (SQLException e){
            throw new FailedUpdateException("Request Error", new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedUpdateException(e);
        }
    }



    private void setRequestDetails(List<ServiceRequest> serviceRequests, ResultSet rs, ServiceRequest request, Client client) throws SQLException {
        request.setClient(client);
        Menu menu = new Menu();
        menu.setName(rs.getString("MenuName"));
        request.setMenu(menu);
        ReservationDetails details = request.getReservationDetails();
        details.setAddress(rs.getString("Address"));
        details.setParticipantNumber(rs.getInt("ParticipantsNumber"));
        String menuLevelStr = rs.getString("MenuLevel");
        if(menuLevelStr != null) {
            details.setSelectedMenuLevel(MenuLevel.valueOf(menuLevelStr));
        }
        Date sqlDate = rs.getDate("Date");
        if (sqlDate != null) details.setDate(sqlDate.toLocalDate());
        Time sqlTime = rs.getTime("Time");
        if (sqlTime != null) details.setTime(sqlTime.toLocalTime());
        serviceRequests.add(request);
    }


}
