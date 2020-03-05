package com.example.cse110_wwr_team2.Messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Invitation extends FirebaseMessagingService {
    String regToken;
    String senderName;
    Message invitation;
    FirebaseMessaging fm;

    public Invitation(String regToken, String senderName) {
        this.fm = FirebaseMessaging.getInstance();
        this.regToken = regToken;
        this.senderName = senderName;
        this.invitation = Message.builder()
                .putData("USER", senderName)
                .setToken(regToken)
                .build();
    }

    public String send() throws FirebaseMessagingException {
        String response = this.fm.send(this.invitation);
        return response;
    }

}
