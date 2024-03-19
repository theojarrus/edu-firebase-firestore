package com.example.firebaseapp.model;

import androidx.annotation.NonNull;

public class Note {

    public String text;

    @NonNull
    @Override
    public String toString() {
        return "Note = {text=" + text + "}";
    }
}
