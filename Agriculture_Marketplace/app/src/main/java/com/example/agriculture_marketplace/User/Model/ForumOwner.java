package com.example.agriculture_marketplace.User.Model;

import com.example.agriculture_marketplace.Forum.Model.Forum;

public class ForumOwner extends User{
    private Forum[] forums;

    //Constructors
    public ForumOwner(String id, String name, String email, String password, String[] ratings, Forum[] forums) {
        super(id, name, email, password, ratings);
        this.forums = forums;
    }
    public ForumOwner() {
        super();
        this.forums = new Forum[0];
    }
    public ForumOwner(ForumOwner forumOwner) {
        super(forumOwner);
        this.forums = forumOwner.forums;
    }

    //Methods
    public void addForum() {
        //TODO
    }
    public void removeForum() {
        //TODO
    }

    public Forum[] getForums() {
        return forums;
    }

    public void setForums(Forum[] forums) {
        this.forums = forums;
    }

    @Override
    public String toString() {
        return "ForumOwner{" +
                "forums=" + forums +
                '}';
    }
}
