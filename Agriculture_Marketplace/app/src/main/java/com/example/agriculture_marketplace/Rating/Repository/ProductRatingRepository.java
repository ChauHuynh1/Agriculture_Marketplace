package com.example.agriculture_marketplace.Rating.Repository;

import com.example.agriculture_marketplace.Rating.Model.ProductRating;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class ProductRatingRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CompletableFuture<Void> saveProductRatingToFirebase(ProductRating productRating) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection("productRatings")
                .add(productRating)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    db.collection("productRatings")
                            .document(id)
                            .update("id", id)
                            .addOnSuccessListener(aVoid -> {
                                System.out.println("updateUserId: Success");
                                future.complete(null);
                            })
                            .addOnFailureListener(e -> {
                                System.out.println("updateUserId: Failed");
                                future.complete(null);
                            });
                })
                .addOnFailureListener(e -> {
                    System.out.println("saveUserToFirebase: Failed");
                });
        return future;
    }

}
