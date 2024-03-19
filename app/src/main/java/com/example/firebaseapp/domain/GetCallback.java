package com.example.firebaseapp.domain;

public interface GetCallback<T> {

    void onReceived(T object);
}
