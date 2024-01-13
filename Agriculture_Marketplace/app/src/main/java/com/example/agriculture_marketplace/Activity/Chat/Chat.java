package com.example.agriculture_marketplace.Activity.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Message.Model.ChatroomModel;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.utils.AndroidUtil;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.UUID;

public class Chat extends AppCompatActivity {

    Forum otherForum;
    ImageButton backButton;
    EditText messageInput;
    ImageButton sendMessage;
    RecyclerView recyclerView;
    TextView forum_detail_name;
    String chatroomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        //get ForumModel
        sendMessage = findViewById(R.id.message_send_btn);
        messageInput = findViewById(R.id.chat_message_input);
        recyclerView = findViewById(R.id.chat_recycle_view);
        forum_detail_name = findViewById(R.id.forum_detail_name);

        // Retrieve forum data from Intent
        String forumId = getIntent().getStringExtra("forumId");
        String forumName = getIntent().getStringExtra("forumName");

        // Create a new Forum object
        otherForum = new Forum();
        otherForum.setId(forumId);
        otherForum.setName(forumName);

        if (otherForum != null) {
            Log.d("ForumDetails", "Forum Name: " + otherForum.getName());
            forum_detail_name.setText(otherForum.getName());
        } else {
            Log.e("ForumDetails", "otherForum is null");
            forum_detail_name.setText("Default Forum Name");
        }
        getOrCreateChatRoomModel();
    }

    private void getOrCreateChatRoomModel() {
        String forumId = otherForum.getId();
        String currentUserId = FirebaseUtil.currentUserId();

        // Create a reference to the "chatRooms" collection in Firestore
        CollectionReference chatRoomsCollection = FirebaseFirestore.getInstance().collection("chatRooms");

        // Query to find a chat room based on forumId and currentUserId
        Query query = chatRoomsCollection
                .whereEqualTo("forumId", forumId)
                .whereArrayContains("userIds", currentUserId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = getFirstDocument(task.getResult());

                if (document != null) {
                    // If the chat room exists, retrieve the chat room details
                    handleExistingChatRoom(document);
                } else {
                    // If the chat room doesn't exist, create a new one
                    createAndHandleNewChatRoom(forumId, currentUserId);
                }
            } else {
                // Handle the error
                handleChatRoomError();
            }
        });
    }

    private DocumentSnapshot getFirstDocument(QuerySnapshot querySnapshot) {
        return querySnapshot.isEmpty() ? null : querySnapshot.getDocuments().get(0);
    }

    private void handleExistingChatRoom(DocumentSnapshot document) {
        ChatroomModel chatRoom = document.toObject(ChatroomModel.class);
        chatroomId = chatRoom != null ? chatRoom.getChatroomId() : null;

        // Now you can use chatRoom as needed
        // Additional logic if necessary
    }

    private void createAndHandleNewChatRoom(String forumId, String currentUserId) {
        String newChatRoomId = UUID.randomUUID().toString();

        ChatroomModel newChatRoom = new ChatroomModel(newChatRoomId, forumId, Collections.singletonList(currentUserId), null, null);
        newChatRoom.setLastMessageTimestamp(Timestamp.now());  // Set current timestamp

        FirebaseFirestore.getInstance().collection("chatRooms")
                .document(newChatRoomId)
                .set(newChatRoom)
                .addOnSuccessListener(aVoid -> {
                    chatroomId = newChatRoomId;
                })
                .addOnFailureListener(e -> {
                    handleChatRoomError();
                });
    }

    private void handleChatRoomError() {
        Toast.makeText(Chat.this, "Error getting chat room", Toast.LENGTH_SHORT).show();
    }



}