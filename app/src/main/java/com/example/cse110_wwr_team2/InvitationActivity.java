package com.example.cse110_wwr_team2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse110_wwr_team2.Invitation.Invitation;
import com.example.cse110_wwr_team2.Invitation.InvitationOnlineSaver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InvitationActivity extends AppCompatActivity {

    private ImageButton btnSearch;
    private EditText email;
    private TextView username;
    private Button btnSend;

    private String myGmail;
    private String fromName;
    private String fromDeviceID;
    private String toUserID;
    private String teamIDToAddTo;

    private CollectionReference usersRef;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        btnSend = (Button) findViewById(R.id.btn_send);
        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        email = (EditText) findViewById(R.id.email);
        username = (TextView) findViewById(R.id.user_tofind);

        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
        myGmail = spfs.getString("gmail", null);
        fromName = spfs.getString("name", null);
        fromDeviceID = spfs.getString("device_ID", null);
        teamIDToAddTo = spfs.getString("teamID", null);
        //toUserID = spfs.getString("id", null);

        // set initial visibility
        username.setVisibility(View.GONE);

        // access database
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        // on clicking search
        btnSearch.setOnClickListener((v) -> {
            String emailAddress = email.getText().toString();
            search(emailAddress, false);
        });

        btnSend.setOnClickListener((v) -> {
            String emailAddress = email.getText().toString();
            search(emailAddress, true);
        });
    }

    // search email address in database
    private void search(String emailAddress, boolean isSending) {

        if(emailAddress == null || emailAddress.equals("") || email.equals("")) {
            Toast.makeText(InvitationActivity.this, "Please input a valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Query queryUser = usersRef.whereEqualTo("gmail", emailAddress);
            queryUser.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String toName = doc.get("name").toString();
                        toUserID = doc.get("id").toString();
                        if (toName != null && !toName.equals("")) {
                            if(!isSending) {
                                display(toName);
                                return;
                            }
                            else {
                                sendInvitation(myGmail, emailAddress, toName, fromName, fromDeviceID, toUserID, teamIDToAddTo);
                                return;
                            }
                        }
                    }
                    // Enter an email that is not valid
                    Toast.makeText(InvitationActivity.this, "Please input a valid email", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("query", "Error query user with email address" + emailAddress);
        }
    }

    // display a searched name
    private void display(String usernameToDisplay) {
        if(usernameToDisplay != null && !usernameToDisplay.equals("")) {
            username.setText(usernameToDisplay);
            username.setVisibility(View.VISIBLE);
            return;
        }
        username.setVisibility(View.GONE);
    }

    // create and send invitation
    private void sendInvitation(String fromGmail, String toGmail, String toName, String fromName, String deviceID, String toUserID, String teamID) {
        if(fromGmail == null || fromGmail.equals("") || toGmail == null || toGmail.equals("")) {
            Toast.makeText(InvitationActivity.this, "error sending invitation", Toast.LENGTH_SHORT);
            return;
        }
        Invitation invitation = new Invitation(fromGmail, toGmail, toName, fromName, deviceID, toUserID, teamID);
        InvitationOnlineSaver ios = new InvitationOnlineSaver(invitation);
        ios.write();
        Toast.makeText(getApplicationContext(), "invitation sent to " + toName, Toast.LENGTH_SHORT).show();
        username.setVisibility(View.GONE);

        Toast.makeText(InvitationActivity.this, "invitation sent", Toast.LENGTH_SHORT);

        Log.d("invitation", "invitation sent from " + fromGmail + " to " + toGmail);
        launchTeamPage();
    }

    // launch invitation activity
    public void launchTeamPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), TeamActivity.class);
                Log.d("launch", "Team from Invitation");
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}