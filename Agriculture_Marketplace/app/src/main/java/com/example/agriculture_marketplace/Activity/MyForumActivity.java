package com.example.agriculture_marketplace.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Repository.ForumRepository;
import com.example.agriculture_marketplace.Helpers.OnForumClickListener;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Helpers.UserManager;
import com.example.agriculture_marketplace.MemberForum.MemberForumRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.Rating.Repository.ForumRatingRepository;
import com.example.agriculture_marketplace.User.Model.UserRepository;
import com.example.agriculture_marketplace.databinding.BrowseForumBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MyForumActivity extends AppCompatActivity implements OnForumClickListener {
    private BrowseForumBinding binding;
    private ArrayList<Forum> forums;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private static final String TAG = "[MyForumActivity]";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BrowseForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.browseForumSearch.setOnEditorActionListener((v, actionId, event) -> {
//            searchForum(binding.browseForumSearch.getText().toString());
            return false;
        });
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                       init();
                    }
                });
        init();
        binding.browseForumAddForum.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateForumActivity.class);
            someActivityResultLauncher.launch(intent);
        });
    }

    private void init() {
        ForumRepository forumRepository = new ForumRepository();
        forumRepository.getForumsByOwnerId(UserManager.getInstance().getCurrentUser().getId()).thenAccept(returnedForums -> {
            forums = returnedForums; // Correctly reference the outer class
            binding.browseForumWelcome.setText(R.string.my_forum);
            binding.browseForumListView.setAdapter(new MyForumAdapter(this, forums, this));
            binding.browseForumResultAmount.setText(String.format("%d results", forums.size()));
            binding.browseForumCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();

                    if (selectedItem.equals("All")) {
                        forumRepository.getAllForums().thenAccept(newForums -> {
                            forums = newForums;  // Directly update the member variable
                            renderForumList(forums);
                        });
                    } else {
                        forumRepository.getForumByCategory(selectedItem).thenAccept(newForums -> {
                            forums = newForums;  // Directly update the member variable
                            renderForumList(forums);
                        });
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });
        });
    }


    private void renderForumList(ArrayList<Forum> forums) {
        MyForumAdapter forumListAdapter = new MyForumAdapter(this, forums, this);
        binding.browseForumListView.setAdapter(forumListAdapter);
        String forumAmount = String.valueOf(forums.size()) + " results";
        binding.browseForumResultAmount.setText(forumAmount);
    }

    @Override
    public void onForumClick(Forum forum) {
        Intent intent = new Intent(this, UpdateForumActivity.class);
        intent.putExtra("forum", forum);
        someActivityResultLauncher.launch(intent);
    }


    public class MyForumAdapter extends BaseAdapter {
        private final ArrayList<Forum> forums;
        private final AppCompatActivity activity;
        private OnForumClickListener listener ;
        public MyForumAdapter(AppCompatActivity activity, ArrayList<Forum> forums, OnForumClickListener listener) {
            this.activity = activity;
            this.forums = forums;
            this.listener = listener;
        }
        @Override
        public int getCount() {
            return forums.size();
        }
        @Override
        public Object getItem(int position) {
            return forums.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.view.View view = convertView;
            if (view == null) {
                view = activity.getLayoutInflater().inflate(R.layout.list_item_forum, null);
            }
            Forum forum = forums.get(position);
            ImageView forumImage = view.findViewById(R.id.list_item_forum_image);
            RenderImageHelper.renderImage(forum.getImageUrl(), forumImage);
            TextView forumTitle = view.findViewById(R.id.list_item_forum_name);
            TextView forumOwner = view.findViewById(R.id.list_item_forum_owner);
            TextView forumRating = view.findViewById(R.id.list_item_forum_rating);
            TextView forumRatingAmount = view.findViewById(R.id.list_item_forum_rating_amount);
            TextView forumMemberAmount = view.findViewById(R.id.forum_detail_member_amount);
            MemberForumRepository memberForumRepository = new MemberForumRepository();

            memberForumRepository.getForumMemberCount(forum.getId()).thenAccept(memberAmount -> {
                String resultString = memberAmount + " members";
                forumMemberAmount.setText(resultString);
            });
            forumTitle.setText(forum.getName());
            UserRepository userRepository = new UserRepository();
            userRepository.getUserbyId(forum.getOwnerId()).thenAccept(user -> {
                forumOwner.setText(user.getName());
            });
            ForumRatingRepository forumRatingRepository = new ForumRatingRepository();
            forumRatingRepository.getForumRatingAndAmount(forum.getId()).thenAccept(forumRatingAndAmount -> {
                forumRating.setText(forumRatingAndAmount.get(0));
                String amount = "(" + forumRatingAndAmount.get(1) + " ratings )";
                forumRatingAmount.setText(amount);
            }
            );
            view.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onForumClick(forum);
                }
            }
            );
            return view;
        }
    }
}
