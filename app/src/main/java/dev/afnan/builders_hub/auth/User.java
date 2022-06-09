package dev.afnan.builders_hub.auth;

public class User {

    public String name, email, phone;

    public User() {
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }


}
