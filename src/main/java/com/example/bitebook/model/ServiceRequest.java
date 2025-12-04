package com.example.bitebook.model;

import com.example.bitebook.model.enums.RequestStatus;

public class ServiceRequest{

    private int id;
    private Client client;
    private Chef chef;
    private Menu menu;
    private int totalPrice;
    private RequestStatus status;
    private ReservationDetails reservationDetails;

    public ServiceRequest(){}

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
    public void setReservationDetails(ReservationDetails reservationDetails){this.reservationDetails = reservationDetails;}
}
