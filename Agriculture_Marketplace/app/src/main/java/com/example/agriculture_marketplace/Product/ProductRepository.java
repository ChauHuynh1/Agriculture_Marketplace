package com.example.agriculture_marketplace.Product;

import android.util.Log;

import com.example.agriculture_marketplace.Config.CollectionConfig;
import com.example.agriculture_marketplace.Config.ProductConfig;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ProductRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "[ProductRepository]";
    public CompletableFuture<Void> saveProductToFirebase(Product product) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection(CollectionConfig.PRODUCT_COLLECTION)
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    db.collection(CollectionConfig.PRODUCT_COLLECTION)
                            .document(id)
                            .update(ProductConfig.PRODUCT_ID, id)
                            .addOnSuccessListener(aVoid -> {
                                future.complete(null);
                                Log.d(TAG, "saveProductToFirebase: Success");
                            })
                            .addOnFailureListener(e -> {
                                future.complete(null);
                                Log.d(TAG, "saveProductToFirebase: Failed");
                            });
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                    Log.d(TAG, "saveProductToFirebase: Failed");
                });
        return future;
    }
    public CompletableFuture<ArrayList<Product>> getAllProducts() {
        CompletableFuture<ArrayList<Product>> future = new CompletableFuture<>();
        db.collection(CollectionConfig.PRODUCT_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        Product product = queryDocumentSnapshots.getDocuments().get(i).toObject(Product.class);
                        products.add(product);
                    }
                    future.complete(products);
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                    Log.d(TAG, "getAllProducts:  + Failed");
                });
        return future;
    }
    CompletableFuture<ArrayList<Product>> getProductsByCategory(String category) {
        CompletableFuture<ArrayList<Product>> future = new CompletableFuture<>();
        db.collection("products")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        Product product = queryDocumentSnapshots.getDocuments().get(i).toObject(Product.class);
                        products.add(product);
                    }
                    future.complete(products);
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                });
        return future;
    }
    CompletableFuture<ArrayList<Product>> getProductsBySellerId(String sellerId) {
        CompletableFuture<ArrayList<Product>> future = new CompletableFuture<>();
        db.collection("products")
                .whereEqualTo("sellerId", sellerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        Product product = queryDocumentSnapshots.getDocuments().get(i).toObject(Product.class);
                        products.add(product);
                    }
                    future.complete(products);
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                });
        return future;
    }
    CompletableFuture<Product> getProductById(String id) {
        CompletableFuture<Product> future = new CompletableFuture<>();
        db.collection("products")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        future.complete(product);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                });
        return future;
    }
    public CompletableFuture<Product> updateProduct(Product product) {
        CompletableFuture<Product> future = new CompletableFuture<>();
        db.collection("products")
                .document(product.getId())
                .set(product)
                .addOnSuccessListener(aVoid -> {
                    future.complete(product);
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                });
        return future;
    }
    public CompletableFuture<Void> deleteProduct(String id) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.collection("products")
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    future.complete(null);
                })
                .addOnFailureListener(e -> {
                    future.complete(null);
                });
        return future;
    }
}
