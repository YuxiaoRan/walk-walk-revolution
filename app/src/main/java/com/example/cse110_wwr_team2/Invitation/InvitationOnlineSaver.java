package com.example.cse110_wwr_team2.Invitation;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class InvitationOnlineSaver {
    private Invitation invitation;
    private FirebaseFirestore db;
    private CollectionReference invRef;

    public InvitationOnlineSaver(Invitation invitation){
        this.invitation = invitation;
        this.db = FirebaseFirestore.getInstance();
        this.invRef = db.collection("Invitations");
    }

    private void saveData() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("NameTo", invitation.getToName());
        docData.put("NameFrom", invitation.getFromName());
        docData.put("DeviceID", invitation.getDeviceID());
        docData.put("from", invitation.getFromGmail());
        docData.put("to", invitation.getToGmail());
        docData.put("status", invitation.getStatus());

        invRef.add(docData);
    }

    public void write() {
        try {
            Query queryUser = invRef.whereEqualTo("from", invitation.getFromGmail())
                                    .whereEqualTo("to", invitation.getToGmail())
                                    .whereEqualTo("status", invitation.getStatus());
            queryUser.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.size() > 0) {
                        Log.d("write invitation", "invitation is duplicate");
                        return;
                    } else {
                        saveData();
                    }
                }
            });
        } catch (Exception e) {
            Log.d("query", "Error query invitation");
        }
    }
}
