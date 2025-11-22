package com.example.bitebook.model.bean;

import com.example.bitebook.model.enums.MenuLevel;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDetailsBean{
    private LocalDate date;
    private LocalTime time;
    private String address;
    private int participantNumber;
    private MenuLevel selectedMenuLevel;    // Aggiunto dopo

    public LocalDate getDate(){return date;}
    public void setDate(LocalDate date){this.date = date;}
    public LocalTime getTime(){return time;}
    public void setTime(LocalTime time){this.time = time;}
    public String getAddress(){return address;}
    public void setAddress(String address){this.address = address;}
    public int getParticipantNumber(){return participantNumber;}
    public void setParticipantNumber(int participantNumber){this.participantNumber = participantNumber;}

    // Aggiunto dopo dal MenuBean
    public MenuLevel getSelectedMenuLevel(){return selectedMenuLevel;}
    public void setSelectedMenuLevel(MenuLevel selectedMenuLevel){this.selectedMenuLevel = selectedMenuLevel;}
}
