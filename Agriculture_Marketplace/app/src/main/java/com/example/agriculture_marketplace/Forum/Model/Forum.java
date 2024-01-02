package com.example.agriculture_marketplace.Forum.Model;

import com.example.agriculture_marketplace.Message.Model.Message;
import com.example.agriculture_marketplace.User.Model.ForumOwner;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class Forum implements Externalizable {
    private String id;
    private String name;
    private String category;
    private String description;
    private ForumOwner owner;
    private Message[] messages;
    private String[] members;


    //MARKER: Constructors
    public Forum(String name, String category, String description) {
        this.id = "";
        this.name = name;
        this.category = category;
        this.description = description;
    }
    public Forum() {
        this.id = "";
        this.name = "";
        this.category = "";
        this.description = "";
    }
    public Forum(Forum forum) {
        this.id = forum.id;
        this.name = forum.name;
        this.category = forum.category;
        this.description = forum.description;
    }
    //Methods
    public void addMember() {
        //TODO
    }
    public void removeMember() {
        //TODO
    }
    public void addManager() {
        //TODO
    }
    public void removeManager() {
        //TODO
    }
    public void addMessage() {
        //TODO
    }
    public void removeMessage() {
        //TODO
    }

    @Override
    public String toString() {
        return "Forum{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", messages=" + Arrays.toString(messages) +
                ", members=" + Arrays.toString(members) +
                '}';
    }

    //Getters and Setters
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(id);
        objectOutput.writeObject(name);
        objectOutput.writeObject(category);
        objectOutput.writeObject(description);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws ClassNotFoundException, IOException {
        id = (String) objectInput.readObject();
        name = (String) objectInput.readObject();
        category = (String) objectInput.readObject();
        description = (String) objectInput.readObject();
    }
}
