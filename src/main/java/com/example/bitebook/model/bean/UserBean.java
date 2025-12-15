package com.example.bitebook.model.bean;

public class UserBean{

    protected int id;
    protected String name;
    protected String surname;


    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getSurname(){return surname;}
    public void setSurname(String surname){this.surname=surname;}


    public boolean validate(){
        return name!=null && !name.isEmpty() && surname!=null && !surname.isEmpty();
    }

}
