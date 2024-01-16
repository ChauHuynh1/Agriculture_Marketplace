package com.example.agriculture_marketplace.Product;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ProductRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void saveProductToFirebase(Product product) {
        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    db.collection("products")
                            .document(id)
                            .update("id", id)
                            .addOnSuccessListener(aVoid -> System.out.println("updateUserId: Success"))
                            .addOnFailureListener(e -> System.out.println("updateUserId: Failed"));
                })
                .addOnFailureListener(e -> {
                    System.out.println("saveUserToFirebase: Failed");
                });
    }
    public CompletableFuture<ArrayList<Product>> getAllProducts() {
        CompletableFuture<ArrayList<Product>> future = new CompletableFuture<>();
        db.collection("products")
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
