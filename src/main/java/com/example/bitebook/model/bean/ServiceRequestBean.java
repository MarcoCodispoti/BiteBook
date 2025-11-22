package com.example.bitebook.model.bean;

import com.example.bitebook.model.*;
import com.example.bitebook.model.enums.RequestStatus;

public class ServiceRequestBean {
    private int id;
    // private Client client;

    private ClientBean clientBean;

    private ChefBean chef;
    private MenuBean menu;
    private int totalPrice;
    private RequestStatus requestStatus;
    private ReservationDetailsBean reservationDetails;

    public ServiceRequestBean(){}

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    // public Client getClient(){return client;}
    // public void setClient(Client client){this.client = client;}

    public ClientBean getClientBean(){return clientBean;}
    public void setClientBean(ClientBean clientBean){this.clientBean = clientBean;}


    public ChefBean getChefBean(){return chef;}
    public void setChefBean(ChefBean chef){this.chef = chef;}
    public MenuBean getMenuBean(){return menu;}
    public void setMenuBean(MenuBean menu){this.menu = menu;}

    public int getTotalPrice(){return totalPrice;}
    public void setTotalPrice(int totalPrice){this.totalPrice = totalPrice;}
    public RequestStatus getStatus(){return requestStatus;}
    public void setStatus(RequestStatus requestStatus){this.requestStatus = requestStatus;}

    public ReservationDetailsBean getReservationDetails(){return reservationDetails;}
    public void setReservationDetails(ReservationDetailsBean reservationDetails){this.reservationDetails = reservationDetails;}

    public boolean validate(){
        return true;
    }

}
