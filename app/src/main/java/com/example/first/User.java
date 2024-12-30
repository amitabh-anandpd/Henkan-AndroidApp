package com.example.first;

import java.util.Objects;

public class User{
    private int id;
    private String name;
    private String address;
    private String email;
    private String password;
    User(String name, String email, String pass, int id){
        this.name = name;
        this.address="";
        this.email=email;
        this.password=pass;
        this.id = id;
    }
    User(String name, String email, String address, String pass, int id){
        this.name = name;
        this.address=address;
        this.email=email;
        this.password=pass;
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void updateAddress(String address){
        this.address=address;
    }
    public String getEmail(){
        return this.email;
    }
    public String getName(){
        return this.name;
    }
    public String getAddress(){
        return this.address;
    }
    public Boolean checkPassword(String password){
        return Objects.equals(this.password, password);
    }
}