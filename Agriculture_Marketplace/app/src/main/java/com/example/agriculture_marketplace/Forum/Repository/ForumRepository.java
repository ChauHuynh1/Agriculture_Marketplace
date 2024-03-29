package com.example.agriculture_marketplace.Forum.Repository;

import android.util.Log;

import com.example.agriculture_marketplace.Config.CollectionConfig;
import com.example.agriculture_marketplace.Config.ForumConfig;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.MemberForum.MemberForumRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ForumRepository {
    private static final String TAG = "[ForumRepository]";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ForumRepository() {
    }
    public CompletableFuture<Void> saveForumToFirebase(Forum forum) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .add(forum)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "saveForumToFirebase: Success");
                    String id = documentReference.getId();
                    db.collection(CollectionConfig.FORUM_COLLECTION)
                            .document(id)
                            .update("id", id)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "saveForumToFirebase: Success");
                                future.complete(null);
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "saveForumToFirebase: Failed");
                                future.completeExceptionally(e);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "saveForumToFirebase: Failed");
                    future.completeExceptionally(e);
                });
        return future;
    }
    public CompletableFuture<Void> updateForum(Forum forum) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .document(forum.getId())
                .set(forum)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "updateForum: Success");
                    future.complete(null);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "updateForum: Failed");
                    future.completeExceptionally(e);
                });
        return future;
    }
    public CompletableFuture<Void> deleteForum(String id) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "deleteForum: Success");
                    future.complete(null);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "deleteForum: Failed");
                    future.completeExceptionally(e);
                });
        return future;
    }
    public CompletableFuture<Forum> getForumById(String id) {
        CompletableFuture<Forum> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Forum forum = documentSnapshot.toObject(Forum.class);
                    assert forum != null;
                    forum.setId(documentSnapshot.getId());
                    future.complete(forum);
                    Log.d(TAG, "getForumById: Success");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "getForumById: Failed");
                    future.completeExceptionally(e);
                });
        return future;
    }
    public CompletableFuture<ArrayList<Forum>> getAllForums() {
        CompletableFuture<ArrayList<Forum>> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "getAllForums: Success");
                    ArrayList<Forum> forums = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        Forum temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Forum.class);
                        assert temp != null;
                        temp.setId(queryDocumentSnapshots.getDocuments().get(i).getId());
                        forums.add(temp);
                    }
                    future.complete(forums);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "getAllForums: Failed");
                    future.complete(null);
                });
        return future;
    }
    public CompletableFuture<ArrayList<Forum>> getForumsByOwnerId(String ownerId) {
        CompletableFuture<ArrayList<Forum>> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .whereEqualTo("ownerId", ownerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "getForumsByOwnerId: Success");
                    ArrayList<Forum> forums = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        Forum temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Forum.class);
                        assert temp != null;
                        temp.setId(queryDocumentSnapshots.getDocuments().get(i).getId());
                        forums.add(temp);
                    }
                    future.complete(forums);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "getForumsByOwnerId: Failed");
                    future.complete(null);
                });
        return future;
    }
    public CompletableFuture<ArrayList<Forum>> getForumsUserNotJoined(String userId) {
        CompletableFuture<ArrayList<Forum>> future = new CompletableFuture<>();
        MemberForumRepository memberForumRepository = new MemberForumRepository();
        memberForumRepository.getForumUserNotJoined(userId).thenAccept(forumIds -> {
            ArrayList<Forum> forums = new ArrayList<>();
            for (String forumId : forumIds) {
                getForumById(forumId).thenAccept(forum -> {
                    forums.add(forum);
                    if (forums.size() == forumIds.size()) {
                        future.complete(forums);
                    }
                });
            }
        }
        );
        return future;
    }
    public CompletableFuture<ArrayList<Forum>> getForumByCategory(String category) {
        CompletableFuture<ArrayList<Forum>> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_COLLECTION)
                .whereEqualTo(ForumConfig.FORUM_CATEGORY, category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "getForumByCategory: Success");
                    ArrayList<Forum> forums = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        Forum temp = queryDocumentSnapshots.getDocuments().get(i).toObject(Forum.class);
                        assert temp != null;
                        temp.setId(queryDocumentSnapshots.getDocuments().get(i).getId());
                        forums.add(temp);
                    }
                    future.complete(forums);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "getForumByCategory: Failed");
                    future.complete(null);
                });
        return future;
    }
}
