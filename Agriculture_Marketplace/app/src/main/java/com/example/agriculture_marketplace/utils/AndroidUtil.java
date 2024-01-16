package com.example.agriculture_marketplace.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.User.Model.User;

public class AndroidUtil {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void passforumModelAsIntent(Intent intent, Forum model) {
        intent.putExtra("name", model.getName());
        intent.putExtra("category", model.getCategory());
        intent.putExtra("description", model.getDescription());
        intent.putExtra("id", model.getId());
    }

    public static Forum getForumModelFromIntent(Intent intent) {
        Forum forumModel = new Forum();
        if (intent != null) {
            forumModel.setName(intent.getStringExtra("name"));
            forumModel.setCategory(intent.getStringExtra("category"));
            forumModel.setDescription(intent.getStringExtra("description"));
            forumModel.setId(intent.getStringExtra("id"));
        }
        return forumModel;
    }
    public static void passUserAsIntent(Intent intent, User model){
        intent.putExtra("username",model.getName());
        intent.putExtra("email",model.getEmail());
        intent.putExtra("userId",model.getId());
        intent.putExtra("fcmToken",model.getFcmToken());

    }

    public static User getUserModelFromIntent(Intent intent){
        User userModel = new User();
        userModel.setName(intent.getStringExtra("username"));
        userModel.setEmail(intent.getStringExtra("email"));
        userModel.setId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }
}

