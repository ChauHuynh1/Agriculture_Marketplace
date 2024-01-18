package com.example.agriculture_marketplace.Activity.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.agriculture_marketplace.Activity.BrowseForumFragment;
import com.example.agriculture_marketplace.Activity.BrowseProductFragment;
import com.example.agriculture_marketplace.Activity.Login;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Model.ForumAdapter;
import com.example.agriculture_marketplace.Forum.Model.ManageForumModel;
import com.example.agriculture_marketplace.MainActivity;
import com.example.agriculture_marketplace.MemberForum.MemberForum;
import com.example.agriculture_marketplace.Message.Model.ChatroomModel;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.User.Model.User;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ChatForum extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText searchInput;
    ChatFragment chatFragment;
    RecyclerView recyclerView;
    TextView name;
    SearchForumRecyclerAdapter adapter;
    Button delete;
    ImageButton close;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference forumCollection = db.collection("forums");

    private Button btnManageForum;
    private RelativeLayout forumManageLayout;
    private RecyclerView manageForumRecyclerView;
    private ManageForumRecyclerAdapter manageForumAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_forum);

        chatFragment = new ChatFragment();
        recyclerView = findViewById(R.id.search_user_recycler_view);
        searchInput = findViewById(R.id.search_chatforum);
        recyclerView.setVisibility(View.GONE);

        name = findViewById(R.id.name);
        FirebaseUtil.currentUserDetails().get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User currentUser = documentSnapshot.toObject(User.class);
                if (currentUser != null) {
                    String userName = currentUser.getName();
                    name.setText("Hi " + userName + "!");
                }
            }
        }).addOnFailureListener(e -> {
            // Handle the failure to fetch user details
            Log.e("ChatForum", "Error fetching user details: " + e.getMessage());
        });

        // manage forum
        btnManageForum = findViewById(R.id.btn_manageforum);
        forumManageLayout = findViewById(R.id.forum_manage_layout);
        manageForumRecyclerView = findViewById(R.id.mange_forum_recycler_view);
        delete = findViewById(R.id.btn_delete);
        close = findViewById(R.id.close_btn);

        btnManageForum.setOnClickListener(view -> showManageForumLayout());

        close.setOnClickListener(view -> hideManageForumLayout());


        delete.setOnClickListener(view -> deleteSelectedChatRoom());

        searchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    searchForums(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Get the current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            setUpRecyclerView(currentUserId);
        }


        //menu to chat
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_item2) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_layout, chatFragment)
                            .commit();
                } else if (item.getItemId() == R.id.navigation_item1) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_layout, new BrowseForumFragment())
                            .commit();
                } else if (item.getItemId() == R.id.navigation_item3) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frame_layout, new BrowseProductFragment())
                            .commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_item2);

        getFCMToken();
        initializeChatForum();
    }


    // manage forum Function
// Call this method in onCreate or wherever appropriate
    private void initializeChatForum() {
        setUpManageForumRecyclerView();
    }

    private void setUpManageForumRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("chatrooms");
        FirestoreRecyclerOptions<ManageForumModel> options = new FirestoreRecyclerOptions.Builder<ManageForumModel>()
                .setQuery(query, ManageForumModel.class)
                .build();

        // Initialize and set up the manage forum adapter
        manageForumAdapter = new ManageForumRecyclerAdapter(options.getSnapshots());

        // Set up the RecyclerView
        manageForumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        manageForumRecyclerView.setAdapter(manageForumAdapter);
    }

    private void showManageForumLayout() {
        forumManageLayout.setVisibility(View.VISIBLE);
        List<ChatroomModel> chatroomDataList = new ArrayList<>();

        // Create an instance of RecentChatRecyclerAdapter
        RecentChatRecyclerAdapter recentChatAdapter = new RecentChatRecyclerAdapter(
                new FirestoreRecyclerOptions.Builder<ChatroomModel>().setQuery(
                        FirebaseFirestore.getInstance().collection("chatrooms"),
                        ChatroomModel.class
                ).build(),
                this
        );
        chatroomDataList.addAll(recentChatAdapter.getSnapshots());

        List<ManageForumModel> manageForumDataList = convertToManageForumModelList(chatroomDataList);

        manageForumAdapter.setData(manageForumDataList);
    }


    private List<ManageForumModel> convertToManageForumModelList(List<ChatroomModel> chatroomDataList) {
        List<ManageForumModel> manageForumDataList = new ArrayList<>();
        for (ChatroomModel chatroom : chatroomDataList) {
            ManageForumModel manageForumModel = new ManageForumModel(chatroom.getForumId(), chatroom.getChatroomId());
            manageForumDataList.add(manageForumModel);
        }
        return manageForumDataList;
    }





    private void hideManageForumLayout() {
        forumManageLayout.setVisibility(View.GONE);

    }
    private void deleteSelectedChatRoom() {
    }


    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Log.d("FCM", "Token: " + token);
                FirebaseUtil.currentUserDetails().update("fcmToken", token);
            }
        });
    }



    private void setUpRecyclerView(String currentUserId) {
        // Query to fetch forums where the current user is a member
        Query query = FirebaseFirestore.getInstance().collection("member_forums")
                .whereEqualTo("userId", currentUserId);

        FirestoreRecyclerOptions<Forum> options = new FirestoreRecyclerOptions.Builder<Forum>()
                .setQuery(query, Forum.class)
                .build();

        // Use the options to initialize the adapter
        adapter = new SearchForumRecyclerAdapter(options, this);

        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Start listening for changes in the data
        adapter.startListening();
    }






    private void searchForums(String searchText) {
        Query query = forumCollection.whereGreaterThanOrEqualTo("name", searchText)
                .whereLessThanOrEqualTo("name", searchText + "\uf8ff");

        FirestoreRecyclerOptions<Forum> options = new FirestoreRecyclerOptions.Builder<Forum>()
                .setQuery(query, Forum.class)
                .build();

        adapter.updateOptions(options);
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

}