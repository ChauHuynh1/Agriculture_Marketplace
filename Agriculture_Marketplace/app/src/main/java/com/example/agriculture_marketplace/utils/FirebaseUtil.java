package com.example.agriculture_marketplace.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static CollectionReference getUsersInForum(String forumId) {
        return (CollectionReference) FirebaseFirestore.getInstance().collection("memberForums").
                whereEqualTo("forumId", forumId);
    }

    public static CollectionReference getAllUsers() {
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static Task<QuerySnapshot> getUsersInForumWithDetails(String forumId) {
        CollectionReference memberForumsRef = getUsersInForum(forumId);
        CollectionReference allUsersRef = getAllUsers();

        // Get users in the specified forum
        return memberForumsRef.get().continueWithTask(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                List<String> userIds = new ArrayList<>();

                for (DocumentSnapshot document : documents) {
                    userIds.add(document.getString("userId"));
                }

                // Get details of users in the forum
                return allUsersRef.whereIn("userId", userIds).get();
            } else {
                // Handle the error
                throw task.getException();
            }
        });
    }

    public static DocumentReference getChatroomReference(String chatroomId) {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds) {
        if (userIds.get(0).equals(FirebaseUtil.currentUserId())) {
            return allUserCollectionReference().document(userIds.get(1));
        } else {
            return allUserCollectionReference().document(userIds.get(0));
        }
    }


    public static CollectionReference getChatroomMessageReference(String chatroomId) {
        return getChatroomReference(chatroomId).collection("chats");
    }
}
