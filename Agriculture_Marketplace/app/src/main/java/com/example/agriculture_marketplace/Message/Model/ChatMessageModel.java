package com.example.agriculture_marketplace.Message.Model;

import com.google.firebase.Timestamp;

public class ChatMessageModel {
    private String message;
    private String senderId;
    private Timestamp timestamp;
    private String imageUrl;

    public ChatMessageModel() {
        // Empty constructor required by Firebase for deserialization
    }

    public ChatMessageModel(String message, String senderId, Timestamp timestamp, String imageUrl) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;  // Corrected variable assignment
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
