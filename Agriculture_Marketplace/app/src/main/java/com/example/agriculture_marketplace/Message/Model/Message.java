package com.example.agriculture_marketplace.Message.Model;

public class Message {
    private Integer id;
    private String content;
    private String sender;
    private String date;

    //Constructors
    public Message(Integer id, String content, String sender, String date) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.date = date;
    }
    public Message() {
        this.id = 0;
        this.content = "";
        this.sender = "";
        this.date = "";
    }
    public Message(Message message) {
        this.id = message.id;
        this.content = message.content;
        this.sender = message.sender;
        this.date = message.date;
    }



}
