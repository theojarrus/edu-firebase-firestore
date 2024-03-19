package com.example.firebaseapp.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebaseapp.domain.UserRepository;
import com.example.firebaseapp.databinding.ActivityMainBinding;
import com.example.firebaseapp.model.Note;
import com.example.firebaseapp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        // Получить интанс firestore (не сохранять в статическую переменную!)
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Получние документа юзера и каст к классу User с помощью toObject
        firestore.collection("users")
                .document("vcr5Aw9onrDwf4bn2hsH")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    Log.wtf("LOGG", user.toString());
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });

        // Получение списка документов юзеров и каст к List<User> с помощью toObjects
        firestore.collection("users")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<User> user = documentSnapshot.toObjects(User.class);
                    Log.wtf("LOGG", user.toString());
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });

        // Последовательные запросы (сначала получаем юзера, потом по его полю - заметку)
        firestore.collection("users")
                .document("vcr5Aw9onrDwf4bn2hsH")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    Log.wtf("LOGG", user.toString());
                    String noteId = user.importantNotes.get(0);
                    firestore.collection("users")
                            .document("vcr5Aw9onrDwf4bn2hsH")
                            .collection("notes")
                            .document(noteId)
                            .get()
                            .addOnSuccessListener(documentSnapshot1 -> {
                                Note note = documentSnapshot1.toObject(Note.class);
                                Log.wtf("LOGG", note.toString());
                            });
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });

        // Полная обработка различных состояний
        firestore.collection("users")
                .document("vcr5Aw9onrDwf4bn2hsH")
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        if (error != null) {
                            binding.textView.setText("Firebase error: " + error.getMessage());
                        } else {
                            binding.textView.setText("No document found");
                        }
                    } else {
                        try {
                            User user = value.toObject(User.class);
                            if (user != null) {
                                Log.wtf("LOGG", user.toString());
                                binding.textView.setText(user.toString());
                            } else {
                                binding.textView.setText("No document found");
                            }
                        } catch (Exception e) {
                            binding.textView.setText("Cast error: " + e.getMessage());
                        }
                    }
                });

        // Создание нового юзера
        User newUser = new User("myemail@gmail.com", "123123");
        firestore.collection("users")
                .document()
                .set(newUser);

        // Точечное обновление полей в формате поле-значение
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("phone", "kjfklsfjklsdf");
        firestore.collection("users")
                .document("b4QggpCq7p3HLsR1BBEF")
                .update(updateMap)
                .addOnSuccessListener(unused -> {
                    Log.wtf("LOGG", "User updated");
                });

        // Вынос логики в репозиторий
        UserRepository.getUserById("A9mkkauBZbZ3e92mys7y")
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    binding.textView.setText(user.toString());
                });

        // Вынос логики в репозиторий вместе с кастом
        UserRepository.getUserById("A9mkkauBZbZ3e92mys7y", user -> {
            binding.textView.setText(user.toString());
        });

        // Дополнительно (опционально) можно добавить обработку ошибки, если возвращать Task
        UserRepository.getUserById("A9mkkauBZbZ3e92mys7y", user -> {
            binding.textView.setText(user.toString());
        }).addOnFailureListener(e -> {
            // Обработка ошибки
        });
    }
}
