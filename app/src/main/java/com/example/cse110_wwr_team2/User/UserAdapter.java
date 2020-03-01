package com.example.cse110_wwr_team2.User;

import android.util.Log;

import com.example.cse110_wwr_team2.firebasefirestore.FireBaseFireStoreService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Map;

import androidx.annotation.NonNull;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserAdapter implements FireBaseFireStoreService {
    private User user;
    private FirebaseFirestore db;

    public UserAdapter(User user){
        this.user = user;
        this.db = FirebaseFirestore.getInstance();
    }
    @Override
    public void write() {
        db.collection("Users").document(user.getId()).set(user);
    }

    @Override
    public void read(String id) {
        // Need different implementation because of getting data from firebase is an async task

    }
}
