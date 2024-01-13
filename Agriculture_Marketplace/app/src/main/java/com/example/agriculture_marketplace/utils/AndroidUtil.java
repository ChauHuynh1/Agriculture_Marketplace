package com.example.agriculture_marketplace.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.agriculture_marketplace.Forum.Model.Forum;

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
}

