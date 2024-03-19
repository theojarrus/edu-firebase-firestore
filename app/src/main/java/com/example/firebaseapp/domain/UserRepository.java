package com.example.firebaseapp.domain;

import com.example.firebaseapp.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserRepository {

    private static final String COLLECTION_USERS = "users";
    private static final String DOCUMENT_USER_PHONE = "phone";

    // Option 1 - Task
    public static Task<DocumentSnapshot> getUserById(String id) {
        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(id)
                .get();
    }

    // Option 2 - Callback
    public static Task<DocumentSnapshot> getUserById(String id, GetCallback<User> getCallback) {
        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    getCallback.onReceived(user);
                });
    }

    public static Task<Void> updateUserPhone(String id, String phone) {
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put(DOCUMENT_USER_PHONE, phone);
        return getUserPath(id).update(updateMap);
    }

    private static DocumentReference getUserPath(String id) {
        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(id);
    }
}
