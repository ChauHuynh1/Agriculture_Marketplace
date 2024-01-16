package com.example.agriculture_marketplace.Rating.Model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ProductRating extends Rating implements Externalizable {
    private String productId;
    public ProductRating(String userId, String productId, String rating, String description) {
        super(userId, rating, description);
        this.productId = productId;
    }

    public ProductRating() {
        super();
        this.productId = "";
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String forumId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ProductRating{" +
                "userId=" + userId +
                ", rating=" + rating +
                "productId=" + productId +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(userId);
        out.writeObject(productId);
        out.writeObject(rating);
    }

    @Override
    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        userId = (String) in.readObject();
        productId = (String) in.readObject();
        rating = (String) in.readObject();
    }
}
