package com.example.cse110_wwr_team2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse110_wwr_team2.RandomIDGenerator.UUIDGenerator;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.User.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class InputHeightActivity extends AppCompatActivity {

    EditText heightInputFt;
    EditText heightInputIn;
    private Button btnDone;
    private String TAG = "InputHeightActivity";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_height);

        heightInputFt = findViewById(R.id.input_height_ft);
        heightInputIn = findViewById(R.id.input_height_in);
        btnDone = findViewById(R.id.button_done);
        this.db = FirebaseFirestore.getInstance();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = spfs.edit();

                // handle invalid number
                int heightInt = 0;
                try {
                    // save input and jump to main page
                    int heightIntFt = Integer.parseInt(heightInputFt.getText().toString());
                    int heightIntIn = Integer.parseInt(heightInputIn.getText().toString());
                    // handle non-positive height
                    if(heightIntFt <= 0 || heightIntIn <= 0) {
                        Toast.makeText(InputHeightActivity.this,
                                "Please input a positive height", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    heightInt = 12 * heightIntFt + heightIntIn;

                    editor.putInt("height", heightInt);
                    editor.putBoolean("firstLogin", false);
                    editor.apply();
                    Toast.makeText(InputHeightActivity.this,
                            "Height saved: " + heightInt + " inches", Toast.LENGTH_SHORT).show();
                    extractUserInfo();
                    goToMain();
                    finish();
                } catch (NumberFormatException e) {
                    // prompt the user to re-input
                    Toast.makeText(InputHeightActivity.this,
                            "Please input whole numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void extractUserInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("id", null);

        // Create a reference to the users collection
        CollectionReference citiesRef = db.collection("users");
        // Create a query against the collection.
        Query query = citiesRef.whereEqualTo("id", user_id);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        // if there are no this user, simply write it to the firebase
                        int height = sharedPreferences.getInt("height", 0);
                        String name = sharedPreferences.getString("name", null);
                        String gmail = sharedPreferences.getString("gmail", null);
                        String teamID = UUIDGenerator.uuidHexToUuid64(UUID.randomUUID().toString());
                        User user = new User(user_id, gmail, name, height);
                        user.setTeamID(teamID);
                        UserAdapter servie = new UserAdapter(user);
                        servie.write();
                    }else if(task.getResult().size() != 1){
                        Toast.makeText(InputHeightActivity.this,
                                "More than one users share the same id", Toast.LENGTH_SHORT).show();
                    }else{
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        User userInfo = document.toObject(User.class);
                                        editor.putString("teamId", userInfo.getTeamID());
                                        editor.apply();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }
                }else{
                    Log.d(TAG, "Error getting users: ", task.getException());
                }
            }
        });
    }
}
