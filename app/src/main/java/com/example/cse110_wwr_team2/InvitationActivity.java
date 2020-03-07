package com.example.cse110_wwr_team2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
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

    private String nameToFind;

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

        // set initial visibility
        username.setVisibility(View.GONE);

        // access database
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        // input email
        String emailAddress = email.getText().toString();

        // on clicking search
        btnSearch.setOnClickListener((v) -> {
            String usernameToDisplay = search(emailAddress);
            display(usernameToDisplay);
        });

        btnSend.setOnClickListener((v) -> {
            if(search(emailAddress) != null) {
                SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
                String myGmail = spfs.getString("gmail", null);
                sendInvitation(myGmail, emailAddress);
            }
        });
    }

    // search email address in database
    private String search(String emailAddress) {
        if(emailAddress == null) {
            Toast.makeText(InvitationActivity.this, "please input an email", Toast.LENGTH_SHORT);
            return null;
        }
        nameToFind = null;
        try {
            Query queryUser = usersRef.whereEqualTo("gmail", emailAddress);
            queryUser.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            nameToFind = doc.get("name").toString();
                        }
                    } else {
                        Log.d("query", "Error getting documents: ",
                                task.getException());
                    }
                }
            });
        } catch (Exception e) {
            Log.d("query", "Error query user with email address" + emailAddress);
        }
        return nameToFind;
    }

    // display a searched name
    private void display(String usernameToDisplay) {
        if(usernameToDisplay != null) {
            username.setText(usernameToDisplay);
            username.setVisibility(View.VISIBLE);
            return;
        }
        username.setVisibility(View.GONE);
    }

    // create and send invitation
    private void sendInvitation(String fromGmail, String toGmail) {
        if(fromGmail == null || toGmail == null) {
            Toast.makeText(InvitationActivity.this, "error sending invitation", Toast.LENGTH_SHORT);
            return;
        }
        Invitation invitation = new Invitation(fromGmail, toGmail);
        InvitationOnlineSaver ios = new InvitationOnlineSaver(invitation);
        ios.write();
        Toast.makeText(InvitationActivity.this, "invitation sent", Toast.LENGTH_SHORT);
        Log.d("invitation", "invitation sent from " + fromGmail + " to " + toGmail);
    }
}
