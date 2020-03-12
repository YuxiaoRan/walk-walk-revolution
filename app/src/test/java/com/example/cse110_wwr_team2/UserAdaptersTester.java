package com.example.cse110_wwr_team2;

import android.content.Context;

import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.UserOnlineSaver;
import com.example.cse110_wwr_team2.firebasefirestore.LastWalkIDCallback;
import com.example.cse110_wwr_team2.firebasefirestore.UserCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.UserTeamIDCallBack;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserAdaptersTester {
    private UserOnlineSaver onlineSaver;

    @Before
    public void setup(){
        onlineSaver = mock(UserOnlineSaver.class);
    }

    @Test
    public void testGetCurrentUserID(){
        String userID = "test";
        String walkID = "test";
        LastWalkIDCallback callback = mock(LastWalkIDCallback.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callback.onCallback(walkID);
                return null;
            }
        }).when(onlineSaver).getLastestWalkID(userID, callback);

        onlineSaver.getLastestWalkID(userID, callback);

        verify(onlineSaver, atLeastOnce()).getLastestWalkID(userID, callback);
        verify(callback, atLeastOnce()).onCallback(walkID);
    }

    @Test
    public void testUpdateLastestWalk(){
        String userID = "test";
        String routeID = "test";
        UserCallBack callback = mock(UserCallBack.class);

        doAnswer(p->{
            callback.onCallBack();
            return null;
        }).when(onlineSaver).updateLatestWalk(userID, routeID, callback);

        onlineSaver.updateLatestWalk(userID, routeID, callback);
        verify(onlineSaver, atLeastOnce()).updateLatestWalk(userID, routeID, callback);
        verify(callback, atLeastOnce()).onCallBack();
    }

    @Test
    public void testGetLatestWalk(){
        String userID = "test";
        String walkID = "test";
        LastWalkIDCallback callback = mock(LastWalkIDCallback.class);

        doAnswer(answer->{
            callback.onCallback(walkID);
            return null;
        }).when(onlineSaver).getLastestWalkID(userID, callback);

        onlineSaver.getLastestWalkID(userID,  callback);
        verify(onlineSaver, atLeastOnce()).getLastestWalkID(userID, callback);
        verify(callback, atLeastOnce()).onCallback(walkID);

    }

    @Test
    public void testGetTeamIDOnLine(){
        String userID = "test";
        String teamID = "test";
        UserTeamIDCallBack callBack = mock(UserTeamIDCallBack.class);

        doAnswer(answer->{
            callBack.onCallback(teamID);
            return null;
        }).when(onlineSaver).getTeamIDOnLine(userID, callBack);

        onlineSaver.getTeamIDOnLine(userID, callBack);
        verify(onlineSaver, atLeastOnce()).getTeamIDOnLine(userID, callBack);
        verify(callBack, atLeastOnce()).onCallback(teamID);
    }
}
