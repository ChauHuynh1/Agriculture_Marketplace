package com.example.agriculture_marketplace.Rating.Repository;

import android.util.Log;

import com.example.agriculture_marketplace.Config.CollectionConfig;
import com.example.agriculture_marketplace.Rating.Model.ForumRating;
import com.google.api.ConfigChangeProto;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ForumRatingRepository {
    private static final String TAG = "[ForumRatingRepository]";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CompletableFuture<ArrayList<String>> getForumRatingAndAmount(String forumId){
      CompletableFuture<ArrayList<String>> future = new CompletableFuture<>();
      ArrayList<String> result = new ArrayList<>();
        db.collection(CollectionConfig.FORUM_RATING_COLLECTION)
                .whereEqualTo("forumId", forumId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "getForumRatingAndAmount: Failed");
                        future.complete(result);
                    } else {
                        Log.d(TAG, "getForumRatingAndAmount: Success");
                        List<ForumRating> forumRatings = queryDocumentSnapshots.toObjects(ForumRating.class);
                        int totalRating = 0;
                        for (ForumRating forumRating : forumRatings) {
                            totalRating += Integer.parseInt(forumRating.getRating());
                        }
                        result.add(String.valueOf(totalRating));
                        result.add(String.valueOf(forumRatings.size()));
                        future.complete(result);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "getForumRatingAndAmount: Failed");
                    future.complete(result);
                });
        return future;
    };
    public CompletableFuture<Void> saveForumRatingToFirebase(ForumRating forumRating) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection(CollectionConfig.FORUM_RATING_COLLECTION)
                .add(forumRating)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    db.collection(CollectionConfig.FORUM_RATING_COLLECTION)
                            .document(id)
                            .update("id", id)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "saveForumRatingToFirebase: Success");
                                future.complete(null);
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "saveForumRatingToFirebase: Failed");
                                future.complete(null);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "saveForumRatingToFirebase: Failed");
                });
        return future;
    }

}
