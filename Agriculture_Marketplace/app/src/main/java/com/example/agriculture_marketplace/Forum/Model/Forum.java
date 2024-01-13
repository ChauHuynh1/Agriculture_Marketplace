package com.example.agriculture_marketplace.Forum.Model;

import com.example.agriculture_marketplace.Message.Model.ChatroomModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.List;

public class Forum implements Externalizable {
    private String id;
    private String name;
    private String category;
    private String description;
    private String ownerId;
    private String imageUrl;
    private ChatroomModel[] messages;
    private List<String> memberUserIds;



    //MARKER: Constructors
    public Forum(String name, String category, String description, String ownerId, String imageUrl) {
        this.id = "";
        this.name = name;
        this.category = category;
        this.description = description;
        this.ownerId = ownerId;
    }
    public Forum() {
        this.name = "";
        this.category = "";
        this.description = "";
        this.ownerId = "";
        this.imageUrl = "";
    }
    public Forum(Forum forum) {
        this.id = forum.id;
        this.name = forum.name;
        this.category = forum.category;
        this.description = forum.description;
        this.ownerId = forum.ownerId;
        this.imageUrl = forum.imageUrl;
    }

    //Methods
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
                ", owner=" + ownerId +
                ", messages=" + Arrays.toString(messages) +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    //Getters and Setters

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(id);
        objectOutput.writeObject(name);
        objectOutput.writeObject(category);
        objectOutput.writeObject(description);
        objectOutput.writeObject(ownerId);
        objectOutput.writeObject(imageUrl);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws ClassNotFoundException, IOException {
        id = (String) objectInput.readObject();
        name = (String) objectInput.readObject();
        category = (String) objectInput.readObject();
        description = (String) objectInput.readObject();
        ownerId = (String) objectInput.readObject();
        imageUrl = (String) objectInput.readObject();
    }
}
