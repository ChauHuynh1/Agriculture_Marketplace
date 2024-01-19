package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Helpers.UserManager;
import com.example.agriculture_marketplace.MainActivity;
import com.example.agriculture_marketplace.Rating.Model.ForumRating;
import com.example.agriculture_marketplace.Rating.Repository.ForumRatingRepository;
import com.example.agriculture_marketplace.User.Model.UserRepository;
import com.example.agriculture_marketplace.databinding.RatingForumBinding;

public class RatingForumActivity extends AppCompatActivity {
    private RatingForumBinding binding;
    private Float rating;
    private Forum forum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RatingForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("forum");
        init();

    }
    private void init() {
        binding.forumRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            this.rating = rating;
        });
        UserRepository userRepository = new UserRepository();
        userRepository.getUserbyId(forum.getOwnerId()).thenAccept(user -> {
            binding.ratingItemForumOwner.setText(user.getName());
        });
        ForumRatingRepository forumRatingRepository = new ForumRatingRepository();
        forumRatingRepository.getForumRatingAndAmount(forum.getId()).thenAccept(forumRatingAndAmount -> {

            binding.ratingItemForumRating.setText(forumRatingAndAmount.get(0));
            binding.ratingItemForumRatingAmount.setText(forumRatingAndAmount.get(1));
        });
        binding.submitRating.setOnClickListener(v -> {
            submitRating();
        });
        RenderImageHelper.renderImage(forum.getImageUrl(),binding.ratingForumImage );
    }
    private void submitRating() {
        ForumRatingRepository forumRatingRepository = new ForumRatingRepository();
        ForumRating forumRating = new ForumRating(UserManager.getInstance().getCurrentUser().getId(), forum.getId(), String.valueOf(rating), binding.forumFeedback.getText().toString());
        forumRatingRepository.saveForumRatingToFirebase(forumRating).thenAccept(aVoid -> {
            Toast.makeText(this, "Rating success", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        );
    }
}
