package com.example.agriculture_marketplace.Activity.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.agriculture_marketplace.User.Model.User;
import com.example.agriculture_marketplace.utils.AndroidUtil;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import okhttp3.MediaType;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.json.JSONObject;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class Chat extends AppCompatActivity {

    Forum otherForum;
    User otherUser;
    ImageButton backButton, sendPictureBtn;
    EditText messageInput;
    ImageButton sendMessage;
    RecyclerView recyclerView;
    TextView forum_detail_name;

    String chatroomId;
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;
    Uri fileUri;
    private String checker = "", myUrl = "";
    private StorageTask uploadTask;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(Chat.this, ChatForum.class));
        });

        sendMessage = findViewById(R.id.message_send_btn);

        loadingBar = new ProgressDialog(this);


        sendPictureBtn = findViewById(R.id.picture_send_btn);
        sendPictureBtn.setOnClickListener(v -> showFilePickerDialog());

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

    private void showFilePickerDialog() {
        CharSequence options[] = new CharSequence[]{
                "Images",
                "PDF Files",
                "Ms Word Files"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the File");
        builder.setItems(options, (dialog, i) -> {
            if (i == 0) {
                checker = "image";
                openImagePicker();
            }
            // Add conditions for other file types if needed
        });
        builder.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select Image"), 438);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 438 && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            fileUri = data.getData();
            if (!checker.equals("image")) {
                // Handle other file types if needed
            } else {
                uploadImageToFirebase();
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase() {
        if (fileUri != null) {
            loadingBar.setTitle("Sending Image");
            loadingBar.setMessage("Please wait, we are sending image..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");
            StorageReference filePath = storageReference.child(UUID.randomUUID().toString() + ".jpg");

            uploadTask = filePath.putFile(fileUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUrl = (Uri) task.getResult();
                    myUrl = downloadUrl.toString();
                    sendMessageToUser("");
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(Chat.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendMessageToUser(String caption) {
        if (chatroomModel == null) {
            return;
        }
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());

        // Check if the message is a text message or an image URL
        if (!myUrl.isEmpty()) {
            // If myUrl is not empty, it means an image is being sent
            chatroomModel.setLastMessage("Image");

            // Update the ChatMessageModel to store the image URL
            ChatMessageModel chatMessageModel = new ChatMessageModel("", FirebaseUtil.currentUserId(), Timestamp.now(), myUrl);

            FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

            FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                    .addOnCompleteListener(task -> {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            // Message sent successfully
                            // You can also handle UI updates or other actions here
                        } else {
                            Toast.makeText(Chat.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            chatroomModel.setLastMessage(caption);

            ChatMessageModel chatMessageModel = new ChatMessageModel(caption, FirebaseUtil.currentUserId(), Timestamp.now(), "");

            FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

            FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                    .addOnCompleteListener(task -> {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            // Message sent successfully
                            // Clear the input bar
                            messageInput.setText("");
                        } else {
                            Toast.makeText(Chat.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
    void sendNotification(String message) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User currentUser = task.getResult().toObject(User.class);
                if (currentUser != null && otherForum != null && chatroomModel != null) {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject notificationObj = new JSONObject();

                    try {
                        notificationObj.put("title", otherForum.getName());
                        notificationObj.put("body", currentUser.getName() + ": " + message);

                        JSONObject dataObj = new JSONObject();
                        dataObj.put("userId", currentUser.getId());

                        jsonObject.put("notification", notificationObj);
                        jsonObject.put("data", dataObj);
                        String otherUserToken = otherUser.getFcmToken();
                        Log.d("FCM", "Other User Token: " + otherUserToken);
                        jsonObject.put("to", otherUserToken);

                        callApi(jsonObject);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Chat", "Current user, other forum, or chatroom model is null");
                }
            }
        });
    }
    private void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();

        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        Log.d("FCM", "Notification Payload: " + jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAJ9D3jwE:APA91bGmNuAbqiXpO5X7MjiqIqIgbKeIqyRwIDHaLURK7PzWHNbqnVZZMYmxwCgAbN8KH234UTdtWtquTqCgy_NgLGgEm4Lnz53xsp4-u1wEesD0PPukNISbJDnaloLYlNkJ-IfOT1jP")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("FCM", "Failed to send notification: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("FCM", "Notification sent successfully");
                } else {
                    Log.e("FCM", "Failed to send notification. Response: " + response.body().string());
                }
            }
        });
    }



    private void handleChatRoomError() {
        Toast.makeText(Chat.this, "Error getting chat room", Toast.LENGTH_SHORT).show();
    }
}

