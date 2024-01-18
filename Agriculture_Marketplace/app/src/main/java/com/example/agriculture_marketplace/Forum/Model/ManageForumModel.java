package com.example.agriculture_marketplace.Forum.Model;

public class ManageForumModel {
    private String ChatRoomId;
    private String userId;
    private String forumName;
    private boolean isSelected;
    public String getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        ChatRoomId = chatRoomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }



    public ManageForumModel(String chatRoomId, String userId) {
        ChatRoomId = chatRoomId;
        this.userId = userId;
        this.forumName = forumName;
        this.isSelected = isSelected;
    }





}
