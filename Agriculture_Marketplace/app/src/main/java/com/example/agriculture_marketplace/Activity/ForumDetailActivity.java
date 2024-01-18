package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.MemberForum.MemberForum;
import com.example.agriculture_marketplace.MemberForum.MemberForumRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.Rating.Repository.ForumRatingRepository;
import com.example.agriculture_marketplace.User.Model.UserRepository;
import com.example.agriculture_marketplace.databinding.ForumDetailBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class ForumDetailActivity extends AppCompatActivity {
    ForumDetailBinding binding;
    UserRepository userRepository = new UserRepository();
    MemberForumRepository memberForumRepository = new MemberForumRepository();
    ForumRatingRepository forumRatingRepository = new ForumRatingRepository();

    Forum forum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ForumDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("forum");
        init();

    }

    private void init() {
        RenderImageHelper.renderImage(forum.getImageUrl(), binding.forumImageView);
        binding.forumDetailName.setText(forum.getName());
        userRepository.getUserbyId(forum.getOwnerId()).thenAccept(user -> {
            binding.forumDetailOwner.setText(user.getName());
        });
        binding.forumDetailDescription.setText(forum.getDescription());
        forumRatingRepository.getForumRatingAndAmount( forum.getId()).thenAccept(forumRating -> {
            String amountResult = forumRating.get(1) + " ratings";
            binding.forumDetailRating.setText(forumRating.get(0));
            binding.forumDetailRatingAmount.setText(amountResult);
        });
        memberForumRepository.getForumMemberCount(forum.getId()).thenAccept(memberForums -> {
            String amountResult = memberForums + " members";
            binding.forumDetailMemberAmount.setText(amountResult);
        });
    }


    private void joinForum() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        MemberForum memberForum = new MemberForum(forum.getId(), auth.getUid());
        memberForumRepository.saveMemberForumToFirebase(memberForum);
    }

}
