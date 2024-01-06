package com.example.agriculture_marketplace.User.Model;

import com.example.agriculture_marketplace.Config.CollectionConfig;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class UserRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CompletableFuture<User> getUserbyId(String id) {
        CompletableFuture<User> future = new CompletableFuture<>();
        db.collection(CollectionConfig.USER_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        future.complete(user);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                });
        return future;
    }
    public void saveUserToFirebase(User user) {
        db.collection(CollectionConfig.USER_COLLECTION)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    db.collection(CollectionConfig.USER_COLLECTION)
                            .document(id)
                            .update("id", id)
                            .addOnSuccessListener(aVoid -> System.out.println("updateUserId: Success"))
                            .addOnFailureListener(e -> System.out.println("updateUserId: Failed"));
                })
                .addOnFailureListener(e -> {
                    System.out.println("saveUserToFirebase: Failed");
                });
    }
}
