package com.example.agriculture_marketplace.Rating.Model;

public class Rating {
    protected String userId;

    protected String rating;

    public Rating(String userId,  String rating) {
        this.userId = userId;
        this.rating = rating;
    }

    public Rating() {
        this.userId = "";
        this.rating = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "userId=" + userId +
                ", rating=" + rating +
                '}';
    }
}
