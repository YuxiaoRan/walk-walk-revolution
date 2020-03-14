package com.example.cse110_wwr_team2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse110_wwr_team2.User.UserOnlineSaver;
import com.example.cse110_wwr_team2.firebasefirestore.UserTeamIDCallBack;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

/**
 * new user login
 * assign randomly generated userID, teamID ect
 */
public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "LoginActivity: ";
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //Device token
    private String device_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        device_ID= task.getResult().getToken();
                        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spfs.edit();
                        editor.putString("device_ID",device_ID);
                        editor.apply();
                        System.out.println("Hello");
                        System.out.println(device_ID);

                    }
                });
        /*
        mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                device_ID = getTokenResult.getToken();
                SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = spfs.edit();
                editor.putString("device_ID",device_ID);
                editor.apply();
                System.out.println("Hello");
                System.out.println(device_ID);
            }
        });

         */

        configureSignInButton(signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
    }

    private void configureSignInButton(SignInButton googleButton) {
        for (int i = 0; i < googleButton.getChildCount(); i++) {
            View v = googleButton.getChildAt(i);
            if (v instanceof TextView)
            {
                TextView tv = (TextView) v;
                tv.setTextSize(14);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextColor(Color.parseColor("#C2BCBC"));
                tv.setSingleLine(true);
                tv.setText(R.string.sign_in_button_text);
                tv.setPadding(20, 15, 10, 15);
                return;
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.w("Google Sign In Error",
                    "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Google Sign in Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Get the device token ID for notifications
//                            System.out.println("Hello3");
//                            System.out.println(device_ID);
                            if(task.isComplete()) {
                                Log.d(TAG, "signInWithCredential:complete");
                                saveUserInfo(user, device_ID);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Firebase Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null) {
                saveUserInfo(currentUser, device_ID);
            }
        }
    }

    /**
     * saving Firebase User's info
     * @param user
     */
    private void saveUserInfo(FirebaseUser user, String device_ID){
//        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        //Log.d(TAG, "saveUserInfo: "+user.getUid());
//        editor.putString("id", user.getUid());
//        editor.putString("gmail", user.getEmail());
//        editor.putString("name", user.getDisplayName());
//        editor.apply();
//        System.out.println("Hello2");
//        System.out.println(device_ID);
        UserOnlineSaver.saveLocalUserInfo(user, device_ID,this);

        // Create a reference to the users collection
        db.collection("Users").whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                gotoInputHeight();
                            }else{
                                UserOnlineSaver saver = new UserOnlineSaver();
                                final String[] myTeamID = {""};
                                saver.getTeamIDOnLine(user.getUid(), new UserTeamIDCallBack() {
                                    @Override
                                    public void onCallback(String teamID) {
                                        myTeamID[0] = teamID;
                                        Log.d(TAG, "onComplete: teamID="+myTeamID[0]);
                                        getSharedPreferences("user", MODE_PRIVATE)
                                                .edit()
                                                .putString("teamID", myTeamID[0])
                                                .apply();
                                        Log.d(TAG, "onCallback: "+getSharedPreferences("user", MODE_PRIVATE).getString("teamID", null));
                                        gotoMain();
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void gotoInputHeight(){
        startActivity(new Intent(this, InputHeightActivity.class));
    }

    private void gotoMain(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
