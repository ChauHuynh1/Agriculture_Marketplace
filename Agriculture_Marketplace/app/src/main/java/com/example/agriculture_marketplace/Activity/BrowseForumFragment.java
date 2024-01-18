package com.example.agriculture_marketplace.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Repository.ForumRepository;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Helpers.UserManager;
import com.example.agriculture_marketplace.MemberForum.MemberForumRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.Rating.Repository.ForumRatingRepository;
import com.example.agriculture_marketplace.User.Model.UserRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class BrowseForumFragment extends Fragment {

    private static final String TAG = "BrowseForumActivity";
    private EditText searchForumEditText;
    private ListView forumListLinearLayout;
    private final ForumRatingRepository forumRatingRepository = new ForumRatingRepository();
    private final MemberForumRepository memberForumRepository = new MemberForumRepository();
    private final UserRepository userRepository = new UserRepository();
    private ArrayList<Forum> forums;
    private TextView forumResultAmountTextView;
    private TextView welcomeTextView;
    private ImageButton browseForumAddButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browse_forum, container, false);
        init(view);

        searchForumEditText = view.findViewById(R.id.browse_forum_search);
        forumResultAmountTextView = view.findViewById(R.id.browse_forum_result_amount);
        searchForumEditText.setOnEditorActionListener((v, actionId, event) -> {
            searchForum(searchForumEditText.getText().toString());
            return false;
        });
        browseForumAddButton = view.findViewById(R.id.browse_forum_add_forum);
        browseForumAddButton.setVisibility(View.INVISIBLE);
        return view;
    }
    private void init(View view) {
        searchForumEditText = view.findViewById(R.id.browse_forum_search);
        forumListLinearLayout = view.findViewById(R.id.browse_forum_list_view);
        welcomeTextView = view.findViewById(R.id.browse_forum_welcome);
        String username = UserManager.getInstance().getCurrentUser().getName();
        String welcome = "Welcome, " +  username;
        welcomeTextView.setText(welcome);
        CompletableFuture<ArrayList<Forum>> future = new ForumRepository().getAllForums();
        future.thenAccept(forums -> {
            renderForumList(forums);
            this.forums = forums;
        });
    }

    private void renderForumList(ArrayList<Forum> forums) {
        ForumListAdapter forumListAdapter = new ForumListAdapter(getContext(), forums);
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
                ImageView forumImage = view.findViewById(R.id.list_item_forum_image);
                Forum forum = forums.get(position);
                forumNameTextView.setText(forum.getName());
                RenderImageHelper.renderImage(forum.getImageUrl(), forumImage);
                forumRatingRepository.getForumRatingAndAmount(forum.getId())
                        .thenAccept(result -> {
                            forumRatingTextView.setText(result.get(0));
                            String amount = "(" + result.get(1) + "ratings )";
                            forumRatingAmountTextView.setText(amount);
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


