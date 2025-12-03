package com.example.bitebook.model.bean;

import com.example.bitebook.model.enums.RequestStatus;

public class ServiceRequestBean {
    private int id;

    private ClientBean clientBean;

    private ChefBean chef;
    private MenuBean menu;
    private int totalPrice;
    private RequestStatus requestStatus;
    private ReservationDetailsBean reservationDetailsBean;

    public ServiceRequestBean(){}

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

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

    public ReservationDetailsBean getReservationDetailsBean(){return reservationDetailsBean;}
    public void setReservationDetailsBean(ReservationDetailsBean reservationDetailsBean){this.reservationDetailsBean = reservationDetailsBean;}


    public boolean validateWithId(){
        return id > 0 && clientBean != null && chef != null && menu != null && totalPrice > 0
                && requestStatus != null && reservationDetailsBean.validate();
    }

    public boolean validate(){
        return clientBean != null && chef != null && menu != null && totalPrice > 0
                && requestStatus != null && reservationDetailsBean != null && reservationDetailsBean.validate();
    }

}
