package com.example.cse110_wwr_team2.Invitation;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InvitationOnlineSaver {
    private Invitation invitation;
    private FirebaseFirestore db;

    public InvitationOnlineSaver(Invitation invitation){
        this.invitation = invitation;
        this.db = FirebaseFirestore.getInstance();
    }

    public void write() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("from", invitation.getFromGmail());
        docData.put("to", invitation.getToGmail());
        docData.put("status", invitation.getStatus());

        db.collection("Invitations").add(docData);
    }
}
