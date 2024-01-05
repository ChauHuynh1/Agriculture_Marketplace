package com.example.agriculture_marketplace.Rating.Model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ForumRating extends Rating implements Externalizable {
    private String forumId;

    public ForumRating(String userId, String forumId, String rating) {
        super(userId, rating);
        this.forumId = forumId;
    }

    public ForumRating() {
        super();
        this.forumId = "";
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    @Override
    public String toString() {
        return "ForumRating{" +
                "userId=" + userId +
                ", rating=" + rating +
                "forumId=" + forumId +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(userId);
        out.writeObject(forumId);
        out.writeObject(rating);
    }

    @Override
    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        userId = (String) in.readObject();
        forumId = (String) in.readObject();
        rating = (String) in.readObject();
    }
}
