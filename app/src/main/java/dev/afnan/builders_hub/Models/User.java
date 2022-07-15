package dev.afnan.builders_hub.Models;

public class User {

    public String UID;
    public String name;
    public String email;
    public String phone;
    public String isUser;

    public User() {
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public User(String name, String email, String phone, String isUser) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isUser = isUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return UID;
    }

    public void setUserID(String UID) {
        this.UID = UID;
    }
}
