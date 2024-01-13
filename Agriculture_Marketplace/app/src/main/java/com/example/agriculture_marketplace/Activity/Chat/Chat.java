package com.example.agriculture_marketplace.Activity.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculture_marketplace.Activity.Login;
import com.example.agriculture_marketplace.Activity.SignUp;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Message.Model.ChatMessageModel;
import com.example.agriculture_marketplace.Message.Model.ChatroomModel;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.utils.AndroidUtil;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(Chat.this, ChatForum.class));
        });

        sendMessage = findViewById(R.id.message_send_btn);
        messageInput = findViewById(R.id.chat_message_input);
        recyclerView = findViewById(R.id.chat_recycle_view);
        forum_detail_name = findViewById(R.id.forum_detail_name);

        String forumId = getIntent().getStringExtra("forumId");
        String forumName = getIntent().getStringExtra("forumName");

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

        sendMessage.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToUser(message);
        }));

        getOrCreateChatRoomModel();
    }

    private void sendMessageToUser(String message) {
        if (chatroomModel == null) {
            return;
        }
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtil.currentUserId(), Timestamp.now());

        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        messageInput.setText("");
                    }
                });
    }

    private void setupChatRecyclerView() {
        if (chatroomId != null) {
            Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                    .orderBy("timestamp", Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                    .setQuery(query, ChatMessageModel.class)
                    .build();

            adapter = new ChatRecyclerAdapter(options, this);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setReverseLayout(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    recyclerView.smoothScrollToPosition(0);
                }
            });
        }
    }

    private void getOrCreateChatRoomModel() {
        String forumId = otherForum.getId();
        String currentUserId = FirebaseUtil.currentUserId();

        CollectionReference chatRoomsCollection = FirebaseFirestore.getInstance().collection("chatrooms");
        Query query = chatRoomsCollection
                .whereEqualTo("forumId", forumId)
                .whereArrayContains("userIds", currentUserId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = getFirstDocument(task.getResult());
                if (document != null) {
                    handleExistingChatRoom(document);
                    chatroomId = document.getId();
                    setupChatRecyclerView();
                } else {
                    createAndHandleNewChatRoom(forumId, currentUserId);
                }
            } else {
                handleChatRoomError();
            }
        });
    }


    private DocumentSnapshot getFirstDocument(QuerySnapshot querySnapshot) {
        return querySnapshot.isEmpty() ? null : querySnapshot.getDocuments().get(0);
    }

    private void handleExistingChatRoom(DocumentSnapshot document) {
        chatroomModel = document.toObject(ChatroomModel.class);
        if (chatroomModel != null) {
            chatroomId = chatroomModel.getChatroomId();
        }
    }

    private void createAndHandleNewChatRoom(String forumId, String currentUserId) {
        String newChatRoomId = UUID.randomUUID().toString();

        chatroomModel = new ChatroomModel(newChatRoomId, forumId, Collections.singletonList(currentUserId), null, "");
        chatroomModel.setLastMessageTimestamp(Timestamp.now());

        FirebaseFirestore.getInstance().collection("chatrooms")
                .document(newChatRoomId)
                .set(chatroomModel)
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

