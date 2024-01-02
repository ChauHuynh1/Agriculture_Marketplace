package com.example.agriculture_marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Repository.ForumRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final ForumRepository forumRepository = new ForumRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Forum forum = new Forum("name", "category", "description");
        forumRepository.saveForumToFirebase(forum);
        CompletableFuture<ArrayList<Forum>> future = forumRepository.getAllForums();
        future.thenAccept(forums -> {
            Log.d(TAG, "onCreate: " + forums.toString());
        });
    }
}