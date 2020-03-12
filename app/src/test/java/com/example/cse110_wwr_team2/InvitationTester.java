package com.example.cse110_wwr_team2;

import com.example.cse110_wwr_team2.Invitation.Invitation;
import com.example.cse110_wwr_team2.Invitation.InvitationOnlineSaver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class InvitationTester {
    private InvitationOnlineSaver saver;

    @Before
    public void setup(){
        saver = mock(InvitationOnlineSaver.class);
    }

    @Test
    public void testWrite(){
        doNothing().when(saver).write();

        saver.write();
        verify(saver).write();
    }
}
