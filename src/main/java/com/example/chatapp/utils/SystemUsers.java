package com.example.chatapp.utils;

public enum SystemUsers {

    ADMIN("linh0001");
    public String username;

    SystemUsers(String username) {
        this.username = username;
    }

    public String getUsername(){return username;}
}
