package com.example.cse110_wwr_team2.FirestoreReferences;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreReferences {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private CollectionReference teamsRef;

    public FirestoreReferences() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");
        teamsRef = db.collection("Teams");
    }

    public CollectionReference getUsersRef() {return usersRef;}
    public CollectionReference getTeamsRef() {return teamsRef;}
}
