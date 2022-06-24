package dev.afnan.builders_hub.auth;

public class User {

    public String name, email, phone, isUser;

    public User() {
    }

    public User(String name, String email, String phone, String isUser) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isUser = isUser;
    }


    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }




}
