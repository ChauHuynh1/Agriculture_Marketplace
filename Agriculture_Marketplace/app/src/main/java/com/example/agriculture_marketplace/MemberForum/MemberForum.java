package com.example.agriculture_marketplace.MemberForum;

import androidx.annotation.NonNull;

public class MemberForum {
    private String userId;
    private String forumId;

    public MemberForum(String userId, String forumId) {
        this.userId = userId;
        this.forumId = forumId;
    }

    public MemberForum() {
        this.userId = "";
        this.forumId = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    @NonNull
    @Override
    public String toString() {
        return "MemberForum{" +
                "userId=" + userId +
                ", forumId=" + forumId +
                '}';
    }
}
