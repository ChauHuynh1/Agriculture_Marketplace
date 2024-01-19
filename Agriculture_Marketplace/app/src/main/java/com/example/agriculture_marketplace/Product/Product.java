package com.example.agriculture_marketplace.Product;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Product implements Externalizable {
    private String id;
    private String name;
    private String description;
    private String price;
    private String image;
    private String sellerId;
    private String category;
    private String quantity;

    public Product() {
    }

    public Product(String id, String name, String description, String price, String image, String sellerId, String category, String quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.sellerId = sellerId;
        this.category = category;
        this.quantity = quantity;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", category='" + category + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(id);
        out.writeObject(name);
        out.writeObject(description);
        out.writeObject(price);
        out.writeObject(image);
        out.writeObject(sellerId);
        out.writeObject(category);
        out.writeObject(quantity);
    }

    @Override
    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        id = (String) in.readObject();
        name = (String) in.readObject();
        description = (String) in.readObject();
        price = (String) in.readObject();
        image = (String) in.readObject();
        sellerId = (String) in.readObject();
        category = (String) in.readObject();
        quantity = (String) in.readObject();
    }
}
