package com.example.agriculture_marketplace.User.Model;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String[] ratings;


    //Constructors
    public User(String id, String name, String email, String password, String[] ratings) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.ratings = ratings;
    }
    public User() {
        this.id = "";
        this.name = "";
        this.email = "";
        this.password = "";
        this.ratings = new String[0];
    }
    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
        this.password = user.password;
        this.ratings = user.ratings;
    }

    //Methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String[] getRatings() {
        return ratings;
    }

    public void setRatings(String[] ratings) {
        this.ratings = ratings;
    }
}
