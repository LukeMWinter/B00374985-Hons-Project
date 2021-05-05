package com.example.honsproject;

public class User {

    public String fullName;
    public Integer admin;
    public String Favourite;
    public User(){

    }

    public User(String fullName, String Favourite, Integer admin){
        this.fullName = fullName;
        this.admin  = admin;
        this.Favourite = Favourite;
    }

}
