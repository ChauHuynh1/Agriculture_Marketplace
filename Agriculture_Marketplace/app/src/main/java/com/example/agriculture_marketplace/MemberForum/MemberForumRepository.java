package com.example.agriculture_marketplace.MemberForum;

import com.example.agriculture_marketplace.Config.CollectionConfig;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class MemberForumRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "[MemberForumRepository]";
    private final String FORUM_ID = "forumId";
    private final String USER_ID = "userId";
    public void saveMemberForumToFirebase(MemberForum memberForum) {
        db.collection("member_forums")
                .add(memberForum)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    db.collection("member_forums")
                            .document(id)
                            .update("id", id)
                            .addOnSuccessListener(aVoid -> System.out.println("updateMemberForumId: Success"))
                            .addOnFailureListener(e -> System.out.println("updateMemberForumId: Failed"));
                })
                .addOnFailureListener(e -> {
                    System.out.println("saveMemberForumToFirebase: Failed");
                });
    }

    public CompletableFuture<Integer> getForumMemberCount(String forumId) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        db.collection(CollectionConfig.MEMBER_FORUM_COLLECTION)
                .whereEqualTo(FORUM_ID, forumId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        System.out.println("getForumMemberCount: Failed");
                        future.complete(0);
                    } else {
                        System.out.println("getForumMemberCount: Success");
                        future.complete(queryDocumentSnapshots.size());
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("getForumMemberCount: Failed");
                    future.complete(0);
                });
        return future;
    }


}
