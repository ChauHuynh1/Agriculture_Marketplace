package com.example.agriculture_marketplace.Rating.Model;

public class Rating {
    protected String userId;
    protected String rating;
    protected String description;
    public Rating(String userId,  String rating, String description) {
        this.userId = userId;
        this.rating = rating;
        this.description = description;
    }

    public Rating() {
        this.userId = "";
        this.rating = "";
        this.description = "";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "userId=" + userId +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                '}';
    }
}
