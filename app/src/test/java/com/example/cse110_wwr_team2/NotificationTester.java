package com.example.cse110_wwr_team2;

import android.os.Bundle;

import com.example.cse110_wwr_team2.Notifications.MyFirebaseMessagingService;
import com.example.cse110_wwr_team2.Notifications.MyFirebaseMessagingServiceAcceptInvite;
import com.example.cse110_wwr_team2.Notifications.MyFirebaseMessagingServiceDeclineInvite;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class NotificationTester {
    @Test
    public void testMyFirebaseMessagingService(){
        MyFirebaseMessagingService service = mock(MyFirebaseMessagingService.class);
        RemoteMessage message = new RemoteMessage(new Bundle());

        doNothing().when(service).onMessageReceived(message);

        service.onMessageReceived(message);
        verify(service).onMessageReceived(message);
    }

    @Test
    public void testMyFirebaseMessagingServiceDeclineInvite(){
        MyFirebaseMessagingServiceDeclineInvite service = mock(MyFirebaseMessagingServiceDeclineInvite.class);
        RemoteMessage message = new RemoteMessage(new Bundle());

        doNothing().when(service).onMessageReceived(message);

        service.onMessageReceived(message);
        verify(service).onMessageReceived(message);
    }

    @Test
    public void testMyFirebaseMessagingServiceAcceptInvite(){
        MyFirebaseMessagingServiceAcceptInvite service = mock(MyFirebaseMessagingServiceAcceptInvite.class);
        RemoteMessage message = new RemoteMessage(new Bundle());

        doNothing().when(service).onMessageReceived(message);

        service.onMessageReceived(message);
        verify(service).onMessageReceived(message);
    }
}
