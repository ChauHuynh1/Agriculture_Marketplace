package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Activity.Chat.ChatForum;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.MemberForum.MemberForum;
import com.example.agriculture_marketplace.MemberForum.MemberForumRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.Rating.Repository.ForumRatingRepository;
import com.example.agriculture_marketplace.User.Model.User;
import com.example.agriculture_marketplace.User.Model.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class ForumDetailActivity extends AppCompatActivity {
    TextView forumNameTextView, forumOwnerTextView, forumMemberCountTextView,
            forumRatingTextView, forumRatingAmountTextView;
    Button joinForumButton;
    UserRepository userRepository = new UserRepository();
    MemberForumRepository memberForumRepository = new MemberForumRepository();
    ForumRatingRepository forumRatingRepository = new ForumRatingRepository();

    Forum forum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_detail);
        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("forum");
        init();
        renderForumDetail();
    }

    private void init() {
        forumNameTextView = findViewById(R.id.forum_detail_name);
        forumOwnerTextView = findViewById(R.id.forum_detail_owner);
        forumMemberCountTextView = findViewById(R.id.forum_detail_member_amount);
        forumRatingTextView = findViewById(R.id.forum_detail_rating);
        forumRatingAmountTextView = findViewById(R.id.forum_detail_rating_amount);
        joinForumButton = findViewById(R.id.forum_detail_join_button);

        // Set an OnClickListener for the joinForumButton
        joinForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinForum();
            }
        });
    }

    private void renderForumDetail() {
        forumNameTextView.setText(forum.getName());
        userRepository.getUserbyId(forum.getOwnerId()).thenAccept(user -> {
            forumOwnerTextView.setText(user.getName());
        });
        memberForumRepository.getForumMemberCount(forum.getId()).thenAccept(count -> {
            forumMemberCountTextView.setText(String.valueOf(count));
        });
        forumRatingRepository.getForumRatingAndAmount(forum.getId()).thenAccept(rating -> {
            forumRatingTextView.setText(String.valueOf(rating.get(0)));
            forumRatingAmountTextView.setText(String.valueOf(rating.get(1)));
        });
    }

    private void joinForum() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        MemberForum memberForum = new MemberForum(forum.getId(), auth.getUid());
        memberForumRepository.saveMemberForumToFirebase(memberForum);

        // Navigate to the chatroom activity
        navigateToChatroom();
    }

    private void navigateToChatroom() {
        // You need to implement this method to navigate to the ChatForum or Chat activity
        // For example:
        Intent chatIntent = new Intent(ForumDetailActivity.this, ChatForum.class);
        startActivity(chatIntent);
        // Finish the current activity if needed
        finish();
    }
}
