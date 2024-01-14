package com.example.agriculture_marketplace.User.Model;

public class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String fcmToken;



    //Constructors
    public User(String name, String email, String password, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;

    }
    public User() {
        this.userId = "";
        this.name = "";
        this.email = "";
        this.password = "";
    }
    public User(User user) {
        this.userId = user.userId;
        this.name = user.name;
        this.email = user.email;
        this.password = user.password;
    }

    //Methods

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmToken() {
        return fcmToken;
    }
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
