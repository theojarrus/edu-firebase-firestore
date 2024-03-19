package com.example.firebaseapp.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class User {

    // Аннотация позволяет записать в поле класса имя документа
    @DocumentId
    public String id;
    public String email;
    public String phone;

    public List<String> importantNotes;

    // Пустой конструктор необходим для парсинга модели firestore
    public User() {

    }

    public User(String email, String phone) {
        this.email = email;
        this.phone = phone;
        this.importantNotes = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
        return "User = {id=" + id + ", email=" + email + ", phone=" + phone + "}";
    }
}
