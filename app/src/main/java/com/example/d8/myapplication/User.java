package com.example.d8.myapplication;

//A Generic user
//This user represents a parent generic class for all users
//Last Modification: 6/7/2018

class User {
    //====Atr====//
    String userId;
    String email;
    String name;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
