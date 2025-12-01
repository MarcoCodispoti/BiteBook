package com.example.bitebook.model;

public abstract class User {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    // private Role userRole; -> Attributo spostato in singleton.Account

    public User(){};

    public User (int id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public void setId(int id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setSurname(String surname){this.surname=surname;}
    public void setEmail(String email){this.email=email;}
    public void setPassword(String password){this.password=password;}
    public int getId(){return id;}
    public String getName(){return name;}
    public String getSurname(){return surname;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}

}
