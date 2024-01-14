package com.example.agriculture_marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.agriculture_marketplace.Activity.BrowseForumActivity;
import com.example.agriculture_marketplace.Activity.CreateForumActivity;
import com.example.agriculture_marketplace.Activity.ForgotPass;
import com.example.agriculture_marketplace.Activity.Login;
import com.example.agriculture_marketplace.Activity.UpdateForumActivity;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Repository.ForumRepository;
import com.example.agriculture_marketplace.MemberForum.MemberForum;
import com.example.agriculture_marketplace.MemberForum.MemberForumRepository;
import com.example.agriculture_marketplace.User.Model.User;
import com.example.agriculture_marketplace.User.Model.UserRepository;
import com.example.agriculture_marketplace.databinding.ActivityForgotPassBinding;
import com.example.agriculture_marketplace.databinding.ActivityLoginBinding;
import com.example.agriculture_marketplace.databinding.ActivityMainBinding;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final ForumRepository forumRepository = new ForumRepository();
    private final UserRepository userRepository = new UserRepository();
    private final MemberForumRepository memberForumRepository = new MemberForumRepository();
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        User user = new User("An", "an123@gmail.com", "123123123");
//        userRepository.saveUserToFirebase(user);
//        Forum forum = new Forum("name1", "category1", "description1", "6guQj41QNErU1U5i1YNx");
//        Forum forum1 = new Forum("name2", "category2", "description1","6guQj41QNErU1U5i1YNx");
//        forumRepository.saveForumToFirebase(forum);
//        CompletableFuture<ArrayList<Forum>> future = forumRepository.getAllForums();
//        future.thenAccept(forums -> {
//            Log.d(TAG, "onCreate: " + forums.toString());
//        });
//        forumRepository.saveForumToFirebase(forum1);
//        MemberForum memberForum = new MemberForum("6guQj41QNErU1U5i1YNx", "2QsepQyv9ds6j0DzD7ip");
//        memberForumRepository.saveMemberForumToFirebase(memberForum);
//        forumRepository.getForumById("ixsVrpNhldKBGzR5JBBg").thenAccept(forum -> {
//            Log.d(TAG, "onCreate: " + forum.toString());
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
//        });

        binding.fgSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
    }
}