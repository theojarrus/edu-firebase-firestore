package com.example.firebaseapp.domain;

import com.example.firebaseapp.model.Error;
import com.example.firebaseapp.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
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
    public static Task<DocumentSnapshot> getUserById(String id, SuccessListener<User> successListener) {
        return getUserDocument(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    successListener.onSuccess(user);
                });
    }

    // Callback with error
    public static void getUserById(String id, SuccessListener<User> successListener, ErrorListener errorListener) {
        getUserDocument(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    try {
                        User user = documentSnapshot.toObject(User.class);
                        assert user != null; // throw AssertionError if user not found
                        successListener.onSuccess(user);
                    } catch (ClassCastException exception) {
                        errorListener.onError(Error.CAST);
                    } catch (AssertionError exception) {
                        errorListener.onError(Error.NOT_FOUND);
                    }
                })
                .addOnFailureListener(exception -> {
                    errorListener.onError(getError(exception));
                });
    }

    public static Task<Void> updateUserPhone(String id, String phone) {
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put(DOCUMENT_USER_PHONE, phone);
        return getUserDocument(id).update(updateMap);
    }

    private static DocumentReference getUserDocument(String id) {
        return FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(id);
    }

    private static Error getError(Exception exception) {
        if (exception instanceof FirebaseNetworkException) {
            return Error.NETWORK;
        } else {
            return Error.OTHER;
        }
    }
}
