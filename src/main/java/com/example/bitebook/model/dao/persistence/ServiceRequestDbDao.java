package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.model.*;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.enums.MenuLevel;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.singleton.LoggedUser;
import com.example.bitebook.util.Connector;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Vector;

public class ServiceRequestDbDao implements ServiceRequestDao {

    public void saveServiceRequest(ServiceRequest serviceRequest) throws SQLException{
        System.out.println("[Modalità persistenza] richiesta di servizio salvata");

        // Da modificare il database -> MenuLevel dovrebbe essere un attributo di ReservationDetails o SendRequest e non del menu

        Connection conn = null;
        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call saveServiceRequest(?,?,?,?,?,?,?,?,?,?)}");
            cstmt.setInt(1, serviceRequest.getClient().getId());
            cstmt.setInt(2, serviceRequest.getChef().getId());
            cstmt.setInt(3, serviceRequest.getMenu().getId());
            cstmt.setString(4, serviceRequest.getReservationDetails().getSelectedMenuLevel().toString());
            cstmt.setInt(5, serviceRequest.getTotalPrice());
            cstmt.setString(6, serviceRequest.getStatus().toString());
            cstmt.setDate(7, Date.valueOf(serviceRequest.getReservationDetails().getDate()));
            cstmt.setTime(8, Time.valueOf(serviceRequest.getReservationDetails().getTime()));
            cstmt.setString(9, serviceRequest.getReservationDetails().getAddress());
            cstmt.setInt(10, serviceRequest.getReservationDetails().getParticipantNumber());

            cstmt.execute();
            cstmt.close();
        } catch (SQLException e){
            e.getMessage();
            e.getCause();
            e.printStackTrace();
            throw new SQLException(e);
        }
    }


    public Vector<ServiceRequest> getClientServiceRequests(Client client) throws Exception{
        Vector<ServiceRequest> serviceRequests = new Vector<>();
        Connection conn = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getClientRequests(?)}");
            cstmt.setInt(1, client.getId());

            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                ServiceRequest serviceRequest = new ServiceRequest();
                Chef chef = new Chef();
                Menu menu = new Menu();
                ReservationDetails reservationDetails = new ReservationDetails();
                serviceRequest.setId(rs.getInt("RequestId"));
                chef.setName(rs.getString("ChefName"));
                chef.setSurname(rs.getString("ChefSurname"));
                menu.setName(rs.getString("MenuName"));
                System.out.println("Dao Nomemenu: " + menu.getName());
                reservationDetails.setSelectedMenuLevel(MenuLevel.valueOf(rs.getString("MenuLevel")));
                reservationDetails.setParticipantNumber(rs.getInt("ParticipantsNumber"));
                serviceRequest.setTotalPrice(rs.getInt("TotalPrice"));
                reservationDetails.setDate(rs.getObject("Date", LocalDate.class));
                reservationDetails.setTime(rs.getObject("Time", LocalTime.class));
                reservationDetails.setAddress(rs.getString("Address"));
                serviceRequest.setStatus(RequestStatus.valueOf(rs.getString("Status")));
                serviceRequest.setClient(client);
                serviceRequest.setChef(chef);
                serviceRequest.setMenu(menu);
                serviceRequest.setReservationDetails(reservationDetails);
                serviceRequests.add(serviceRequest);
            }
            cstmt.close();
            rs.close();
        } catch(SQLException e){
            return  null;
        }

        return  serviceRequests;
    }

    @Override
    public Vector<ServiceRequest> getChefServiceRequests(Chef chef) throws Exception {
        Vector<ServiceRequest> serviceRequests = new Vector<>();
        Connection conn = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getChefRequests(?)}");
            cstmt.setInt(1, chef.getId());

            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                ServiceRequest serviceRequest = new ServiceRequest();
                Client client = new Client();
                Menu menu = new Menu();
                ReservationDetails reservationDetails = new ReservationDetails();
                serviceRequest.setId(rs.getInt("RequestId"));
                client.setName(rs.getString("ClientName"));
                client.setSurname(rs.getString("ClientSurname"));
                menu.setName(rs.getString("MenuName"));
                System.out.println("Dao Nomemenu: " + menu.getName());
                reservationDetails.setSelectedMenuLevel(MenuLevel.valueOf(rs.getString("MenuLevel")));
                reservationDetails.setParticipantNumber(rs.getInt("ParticipantsNumber"));
                serviceRequest.setTotalPrice(rs.getInt("TotalPrice"));
                reservationDetails.setDate(rs.getObject("Date", LocalDate.class));
                reservationDetails.setTime(rs.getObject("Time", LocalTime.class));
                reservationDetails.setAddress(rs.getString("Address"));
                serviceRequest.setStatus(RequestStatus.valueOf(rs.getString("Status")));
                serviceRequest.setClient(client);
                serviceRequest.setChef(chef);
                serviceRequest.setMenu(menu);
                serviceRequest.setReservationDetails(reservationDetails);
                serviceRequests.add(serviceRequest);
            }
            cstmt.close();
            rs.close();

        } catch(Exception e){
            e.getMessage();
            e.getCause();
            e.printStackTrace();
            return new Vector<>();
        }

        return  serviceRequests;
    }

    @Override
    public void approveRequest(ServiceRequest serviceRequest) throws Exception {
        Connection conn = null;

        try {
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt;
            if (serviceRequest.getStatus().equals(RequestStatus.APPROVED)){
                cstmt = conn.prepareCall("{call approveRequest(?)}");
            } else if(serviceRequest.getStatus().equals(RequestStatus.REJECTED)){
                cstmt = conn.prepareCall("{call rejectRequest(?)}");
            } else{
                throw new Exception("Invalid request status");
            }
            cstmt.setInt(1, serviceRequest.getId());
            cstmt.execute();

            cstmt.close();
        } catch(SQLException e){
            e.getMessage();
            e.getCause();
            e.printStackTrace();
            throw new SQLException(e);
        }
    }
}
