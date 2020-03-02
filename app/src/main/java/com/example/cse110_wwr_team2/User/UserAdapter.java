package com.example.cse110_wwr_team2.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cse110_wwr_team2.firebasefirestore.FireBaseFireStoreService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;
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

    public static void saveLocalUserInfo(FirebaseUser user, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Log.d(TAG, "saveUserInfo: "+user.getUid());
        editor.putString("id", user.getUid());
        editor.putString("gmail", user.getEmail());
        editor.putString("name", user.getDisplayName());
        editor.apply();
    }


}
