package com.example.cse110_wwr_team2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse110_wwr_team2.FirestoreReferences.FirestoreReferences;
import com.example.cse110_wwr_team2.Messaging.Invitation;
import com.example.cse110_wwr_team2.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InvitationActivity extends AppCompatActivity {

    EditText email;
    Button btnSend;

    String emailAddress;
    FirestoreReferences refs;
    CollectionReference usersRef, teamsRef;

    String userId;
    String userToken = null;
    User sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        email = (EditText) findViewById(R.id.email);
        btnSend = (Button) findViewById(R.id.btn_send);

        refs = new FirestoreReferences();
        usersRef = refs.getUsersRef();
        teamsRef = refs.getTeamsRef();

        emailAddress = email.getText().toString();

        btnSend.setOnClickListener(v -> {
            sendInvitation();
        });
    }

    private void sendInvitation() {
        // get sender name
        sender = (User) getIntent().getSerializableExtra("sender");
        String senderName = sender.getName();
        String teamName = sender.getTeamID();
        try {
            Query queryUser = usersRef.whereEqualTo("gmail", emailAddress);
            queryUser.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            userId = doc.getData().get("id").toString();
                            Toast.makeText(InvitationActivity.this, "invitation sent to" + emailAddress, Toast.LENGTH_SHORT).show();
                            //userToken = doc.getData().get("regToken").toString();
                        }
                    } else {
                        Log.d("query error", "Error getting documents: ",
                                task.getException());
                    }
                }
            });
        } catch (Exception e) {
            Log.d("query error", "Error query user with email address" + emailAddress);
        }

        // send invitation
        if(userId != null && userToken != null) {
            // This registration token comes from the client FCM SDKs.
            Invitation toSend = new Invitation(userToken, senderName);

            try {
                String response = toSend.send();
                Log.d("success", "Successfully sent invitation: " + response);
            } catch (Exception e) {
                Log.d("send error", "Error sending invitation");
            }
        } else {
            Log.d("send error", "target not found");
        }
    }
}
