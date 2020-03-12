package com.example.cse110_wwr_team2;
import android.content.Context;

import com.example.cse110_wwr_team2.Team.TeamAdapter;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.GetCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.MapCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.TeammateCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.UserCallBack;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Testing for the TeamAdapter test
 */
@RunWith(AndroidJUnit4.class)
public class TeamAdapterTest {
    @Test
    public void testGetTeammatesName1(){
        TeamAdapter service = mock(TeamAdapter.class);
        TeammateCallBack callBack = mock(TeammateCallBack.class);
        List<String> strings = new ArrayList<>();
        strings.add("test");
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callBack.onCallback(strings);
                return null;
            }
        }).when(service).getTeammatesNames(any(TeammateCallBack.class));

        service.getTeammatesNames(callBack);

        verify(service, atLeastOnce()).getTeammatesNames(callBack);
        verify(callBack, atLeastOnce()).onCallback(strings);
    }

    @Test
    public void testGetTeammates(){
        TeamAdapter service = mock(TeamAdapter.class);
        UserCallBack callBack = mock(UserCallBack.class);
        Context context = mock(Context.class);
        ArrayList<User> users = new ArrayList<>();
        users.add(new User());
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callBack.onCallback(users);
                return null;
            }
        }).when(service).getTeammates(any(UserCallBack.class),any(Context.class));

        service.getTeammates(callBack,context);

        verify(service, atLeastOnce()).getTeammates(callBack,context);
        verify(callBack, atLeastOnce()).onCallback(users);
    }

    @Test
    public void testGetTeammatesName2(){
        TeamAdapter service = mock(TeamAdapter.class);
        TeammateCallBack callBack = mock(TeammateCallBack.class);
        Context context = mock(Context.class);
        List<String> strings = new ArrayList<>();
        strings.add("test");
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callBack.onCallback(strings);
                return null;
            }
        }).when(service).getTeammatesNames(any(TeammateCallBack.class),any(Context.class));

        service.getTeammatesNames(callBack,context);

        verify(service, atLeastOnce()).getTeammatesNames(callBack,context);
        verify(callBack, atLeastOnce()).onCallback(strings);
    }

    @Test
    public void testGetTeammatesName3(){
        TeamAdapter service = mock(TeamAdapter.class);
        GetCallBack callBack = mock(GetCallBack.class);
        String userID = "test";
        String userName = "test";
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callBack.onCallBack(userName);
                return null;
            }
        }).when(service).getTeammatesNames(any(GetCallBack.class),anyString());

        service.getTeammatesNames(callBack,userID);

        verify(service, atLeastOnce()).getTeammatesNames(callBack,userID);
        verify(callBack, atLeastOnce()).onCallBack(userName);
    }

    @Test
    public void testGetAllMap(){
        TeamAdapter service = mock(TeamAdapter.class);
        MapCallBack callBack = mock(MapCallBack.class);
        String teamID = "test";
        String proposerID = "test";
        FirebaseFirestore db = mock(FirebaseFirestore.class);

        Map<String,Integer> map = mock(Map.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callBack.onCallback(map,db);
                return null;
            }
        }).when(service).getAllMap(any(MapCallBack.class),anyString(),anyString());

        service.getAllMap(callBack,teamID,proposerID);

        verify(service, atLeastOnce()).getAllMap(callBack,teamID,proposerID);
        verify(callBack, atLeastOnce()).onCallback(map,db);
    }


}
