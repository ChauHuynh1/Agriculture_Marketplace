package com.example.agriculture_marketplace.Rating.Repository;

import com.example.agriculture_marketplace.Config.CollectionConfig;
import com.example.agriculture_marketplace.Rating.Model.ProductRating;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
    public CompletableFuture<ArrayList<String>> getProductRatingAndAmount(String productId){
        CompletableFuture<ArrayList<String>> future = new CompletableFuture<>();
        ArrayList<String> result = new ArrayList<>();
        db.collection(CollectionConfig.PRODUCT_RATING_COLLECTION)
                .whereEqualTo("productId", productId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        System.out.println("getProductRatingAndAmount: No ratings found");
                        result.add("0");
                        result.add("0");
                        future.complete(result);
                    } else {
                        System.out.println("getProductRatingAndAmount: Success");
                        ArrayList<ProductRating> productRatings = (ArrayList<ProductRating>) queryDocumentSnapshots.toObjects(ProductRating.class);
                        int totalRating = 0;
                        for (ProductRating productRating : productRatings) {
                            totalRating += Integer.parseInt(productRating.getRating());
                        }
                        result.add(String.valueOf(totalRating));
                        result.add(String.valueOf(productRatings.size()));
                        future.complete(result);
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("getProductRatingAndAmount: Failed");
                    future.complete(result);
                });
        return future;
    };

}
