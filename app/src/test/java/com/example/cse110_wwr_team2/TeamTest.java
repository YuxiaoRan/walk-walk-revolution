package com.example.cse110_wwr_team2;
import com.example.cse110_wwr_team2.Team.TeamAdapter;
import com.example.cse110_wwr_team2.firebasefirestore.TeammateCallBack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TeamTest {
    @Test
    public void testGetTeammateName(){
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

}
