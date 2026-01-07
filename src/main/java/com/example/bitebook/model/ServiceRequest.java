package com.example.bitebook.model;

import com.example.bitebook.model.enums.MenuLevel;
import com.example.bitebook.model.enums.RequestStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class ServiceRequest{

    private int id;
    private Client client;
    private Chef chef;
    private Menu menu;
    private int totalPrice;
    private RequestStatus status;
    private final ReservationDetails reservationDetails;


    public ServiceRequest(){
        this.reservationDetails = new ReservationDetails();
    }


    public ServiceRequest(LocalDate date, LocalTime time, String address, int participantsNumber, MenuLevel selectedMenuLevel){
        this.reservationDetails = new ReservationDetails(date,time,address,participantsNumber,selectedMenuLevel);
    }


    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public Client getClient(){return client;}
    public void setClient(Client client){this.client = client;}

    public Chef getChef(){return chef;}
    public void setChef(Chef chef){this.chef = chef;}

    public Menu getMenu(){return menu;}
    public void setMenu(Menu menu){this.menu = menu;}

    public int getTotalPrice(){return totalPrice;}
    public void setTotalPrice(int totalPrice){this.totalPrice = totalPrice;}

    public RequestStatus getStatus(){return status;}
    public void setStatus(RequestStatus status){this.status = status;}

    public ReservationDetails getReservationDetails(){return reservationDetails;}
    // No setter: reservationDetails is part of a Composition relationship.
}
