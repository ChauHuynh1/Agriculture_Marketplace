package com.example.agriculture_marketplace.Forum.Repository;

import android.util.Log;

import com.example.agriculture_marketplace.Config.CollectionConfig;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ForumRepository {
    private static final String TAG = "[ForumRepository]";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ForumRepository() {
    }
    public void saveForumToFirebase(Forum forum) {
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .add(forum)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "saveForumToFirebase: Success");
                    String id = documentReference.getId();
                    db.collection(CollectionConfig.FORUM_COLLECTION)
                            .document(id)
                            .update("id", id)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "updateForumId: Success"))
                            .addOnFailureListener(e -> Log.d(TAG, "updateForumId: Failed"));
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "saveForumToFirebase: Failed");
                });
    }
    public void updateForum(Forum forum) {
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .document(forum.getId())
                .set(forum)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "updateForum: Success"))
                .addOnFailureListener(e -> Log.d(TAG, "updateForum: Failed"));
    }
    public void deleteForum(Forum forum) {
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .document(forum.getId())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "deleteForum: Success"))
                .addOnFailureListener(e -> Log.d(TAG, "deleteForum: Failed"));
    }
    public void getForumById(String id) {
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "getForumById: Success");
                        Forum forum = documentSnapshot.toObject(Forum.class);
                        assert forum != null;
                        Log.d(TAG, "getForumById: " + forum.toString());
                    } else {
                        Log.d(TAG, "getForumById: Failed");
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "getForumById: Failed"));
    }
    public CompletableFuture<ArrayList<Forum>> getAllForums() {
        CompletableFuture<ArrayList<Forum>> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "getAllForums: Success");
                    ArrayList<Forum> forums = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        forums.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Forum.class));
                    }
                    future.complete(forums);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "getAllForums: Failed");
                    future.complete(null);
                });
        return future;
    }

}
