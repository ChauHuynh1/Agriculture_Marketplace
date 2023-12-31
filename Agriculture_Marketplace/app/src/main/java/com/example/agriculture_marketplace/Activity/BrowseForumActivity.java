package com.example.agriculture_marketplace.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Repository.ForumRepository;
import com.example.agriculture_marketplace.MemberForum.MemberForumRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.Rating.Repository.ForumRatingRepository;
import com.example.agriculture_marketplace.User.Model.UserRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class BrowseForumActivity extends AppCompatActivity {

    private static final String TAG = "BrowseForumActivity";
    private EditText searchForumEditText;
    private ListView forumListLinearLayout;
    private final ForumRatingRepository forumRatingRepository = new ForumRatingRepository();
    private final MemberForumRepository memberForumRepository = new MemberForumRepository();
    private final UserRepository userRepository = new UserRepository();
    private ArrayList<Forum> forums;
    private TextView forumResultAmountTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_forum);
        searchForumEditText = findViewById(R.id.browse_forum_search);
        forumResultAmountTextView = findViewById(R.id.browse_forum_result_amount);
        init();
        searchForumEditText.setOnEditorActionListener((v, actionId, event) -> {
            searchForum(searchForumEditText.getText().toString());
            return false;
        });
    }

    private void init() {
        searchForumEditText = findViewById(R.id.browse_forum_search);
        forumListLinearLayout = findViewById(R.id.browse_forum_list_view);

        CompletableFuture<ArrayList<Forum>> future = new ForumRepository().getAllForums();
        future.thenAccept(forums -> {
            renderForumList(forums);
            this.forums = forums;
        });
    }

    private void renderForumList(ArrayList<Forum> forums) {
        ForumListAdapter forumListAdapter = new ForumListAdapter(this, forums);
        forumListLinearLayout.setAdapter(forumListAdapter);
        String forumAmount = String.valueOf(forums.size()) + " results";
        forumResultAmountTextView.setText(forumAmount);
    }
    //List view Adapter
    public class ForumListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Forum> forums;

        public ForumListAdapter(Context context, ArrayList<Forum> forums) {
            this.context = context;
            this.forums = forums;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.list_item_forum, parent, false);

                TextView forumNameTextView = view.findViewById(R.id.list_item_forum_name);
                TextView forumOwnerTextView = view.findViewById(R.id.list_item_forum_owner);
                TextView forumRatingTextView = view.findViewById(R.id.list_item_forum_rating);
                TextView forumRatingAmountTextView = view.findViewById(R.id.list_item_forum_rating_amount);
                TextView forumMemberAmountTextView = view.findViewById(R.id.forum_detail_member_amount);

                Forum forum = forums.get(position);
                forumNameTextView.setText(forum.getName());
                forumRatingRepository.getForumRatingAndAmount(forum.getId())
                        .thenAccept(result -> {
                            forumRatingTextView.setText(result.get(0));
                            forumRatingAmountTextView.setText(result.get(1));
                        });
                memberForumRepository.getForumMemberCount(forum.getId())
                        .thenAccept(result -> {
                            forumMemberAmountTextView.setText(String.valueOf(result));
                        });
                userRepository.getUserbyId(forum.getOwnerId())
                        .thenAccept(result -> {
                            forumOwnerTextView.setText(result.getName());
                        });
            }
            view.setOnClickListener(v -> {
                Forum forum = forums.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("forum", forum);
                Intent intent = new Intent(context, ForumDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            return view;
        }
    }

    private void searchForum (String forumName) {
        if (forumName.isEmpty()) {
            renderForumList(forums);
            return;
        }
        ArrayList<Forum> searchForums = new ArrayList<>();
        for (Forum forum : forums) {
            if (forum.getName().contains(forumName)) {
                searchForums.add(forum);
            }
        }
        renderForumList(searchForums);
    }


}


