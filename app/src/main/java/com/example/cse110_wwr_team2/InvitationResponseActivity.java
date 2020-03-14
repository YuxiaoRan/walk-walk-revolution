package com.example.cse110_wwr_team2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse110_wwr_team2.Invitation.Invitation;
import com.example.cse110_wwr_team2.Invitation.InvitationOnlineSaver;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * take charge if invitation response
 */
public class InvitationResponseActivity extends AppCompatActivity {

    private TextView notificationData;
    private TextView greeting;

    private Button accept;
    private Button decline;

    private String myName;
    private String dest_user_id;
    private String invite_id;

    private String team_ID;

    private FirebaseFirestore db;
    private CollectionReference userRef;
    private CollectionReference inviteRef;
    String DOCUMENT_KEY = "team";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_repsonse);
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
        myName = spfs.getString("name", null);

        String message = getIntent().getStringExtra("message");
        String dest_user_id = getIntent().getStringExtra("dest_user_id");
        String invite_id = getIntent().getStringExtra("invite_id");
        String team_ID = getIntent().getStringExtra("teamID");
        this.team_ID = team_ID;
        this.invite_id = invite_id;
        this.dest_user_id = dest_user_id;

        greeting = (TextView) findViewById(R.id.invitation_repsonse_greeting);
        greeting.setText("Hello " + myName);

        notificationData = (TextView) findViewById(R.id.invitation_message);
        notificationData.setText(message);
        System.out.println("TEAM ID: " + team_ID);
        accept = findViewById(R.id.btn_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUsertoTeam();
                Log.d("button","accept invitation");
                Toast.makeText(getApplicationContext(), "Accepted Invitation", Toast.LENGTH_SHORT).show();
                launchTeamPage();
                finish();

            }
        });

        decline = findViewById(R.id.btn_decline);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineUserInvite();
                Log.d("button","decline invitation");
                Toast.makeText(getApplicationContext(), "Declined Invitation", Toast.LENGTH_SHORT).show();
                launchHome();
                finish();
            }
        });




    }

    private void subscribeToNotificationsTopic() {
        System.out.println("TEAM ID: " + team_ID);
        FirebaseMessaging.getInstance().subscribeToTopic(team_ID)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                                System.out.println("Subscribe to notifications failed");
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    System.out.println("Subscribe to notifications");
                        }

                );
    }

    public void addUsertoTeam() {
        subscribeToNotificationsTopic();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");
        userRef.whereEqualTo("id", dest_user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("teamID", team_ID);
                        Log.d(TAG, "onComplete: teamID"+map.get("teamID"));
                        userRef.document(document.getId()).set(map, SetOptions.merge());
                    }
                }
            }
        });

        //update invites
        db = FirebaseFirestore.getInstance();
        //Update invitation
        inviteRef = db.collection("Invitations");
        inviteRef.whereEqualTo("NameTo", invite_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Map<Object, Integer> map = new HashMap<>();
                        map.put("status", 1);
                        inviteRef.document(document.getId()).set(map, SetOptions.merge());
                    }
                }
            }
        });
    }

    public void declineUserInvite() {
        db = FirebaseFirestore.getInstance();
        //Update invitation
        inviteRef = db.collection("Invitations");
        inviteRef.whereEqualTo("NameTo", invite_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        inviteRef.document(document.getId()).delete();
                    }
                }
            }
        });
    }

    // launch main activity
    public void launchHome(){
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("launch","Home from decline invite");
        startActivity(intent);
        finish();
    }

    // launch invitation activity
    public void launchTeamPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), TeamActivity.class);
                Log.d("launch", "Team from accept invite");
                startActivity(intent);
            }
        }, 2000);
    }
}
