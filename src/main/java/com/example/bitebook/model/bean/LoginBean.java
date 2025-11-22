package com.example.bitebook.model.bean;

import com.example.bitebook.model.enums.Role;

public class LoginBean {
    private int  id;
    private Role role;
    private String email;
    private String password;

    public boolean validateEmail(){
        // Rimuovi i doppi apici e il punto extra all'inizio.
        // Il singolo backslash davanti al punto nel dominio è sufficiente.
        return email.length() > 6 && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean validatePassword(){
        return password.length() > 8;
    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public Role getRole(){return role;}
    public void setRole(Role role){this.role=role;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
}
