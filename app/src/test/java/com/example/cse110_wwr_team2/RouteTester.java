package com.example.cse110_wwr_team2;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.Route.RouteSaver;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.RouteUpdateCallback;
import com.example.cse110_wwr_team2.firebasefirestore.SingleRouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.TeamRouteCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class RouteTester {
    @Test
    public void testGetAllRoutes(){
        RouteSaver service = mock(RouteSaver.class);
        RouteCallback callBack = mock(RouteCallback.class);
        List<Route> routes = new ArrayList<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callBack.onCallback(routes);
                return null;
            }
        }).when(service).getAllRoutes(any(RouteCallback.class));

        service.getAllRoutes(callBack);

        verify(service, atLeastOnce()).getAllRoutes(callBack);
        verify(callBack, atLeastOnce()).onCallback(any(List.class));
    }

    @Test
    public void testGetTeamRoutes(){
        RouteSaver service = mock(RouteSaver.class);
        TeamRouteCallback callBack = mock(TeamRouteCallback.class);
        List<Route> routes = new ArrayList<>();
        List<String> routes_info = new ArrayList<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callBack.onCallback(routes, routes_info);
                return null;
            }
        }).when(service).getTeamRoutes(any(TeamRouteCallback.class));

        service.getTeamRoutes(callBack);

        verify(service, atLeastOnce()).getTeamRoutes(callBack);
        verify(callBack, atLeastOnce()).onCallback(any(), any());
    }

    @Test
    public void testGetRoute(){
        RouteSaver service = mock(RouteSaver.class);
        SingleRouteCallback callback = mock(SingleRouteCallback.class);
        String id = "testing";
        Route route = mock(Route.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callback.onCallback(route);
                return null;
            }
        }).when(service)
                .getRoute(id, callback);

        service.getRoute(id, callback);

        verify(service, atLeastOnce()).getRoute(id, callback);
        verify(callback, atLeastOnce()).onCallback(any(Route.class));
    }

    @Test
    public void testUpdateRoute(){
        RouteSaver service = mock(RouteSaver.class);
        RouteUpdateCallback callback = mock(RouteUpdateCallback.class);
        Route route = mock(Route.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callback.onCallback();
                return null;
            }
        }).when(service)
                .UpdateRoute(any(Route.class), any(RouteUpdateCallback.class));

        service.UpdateRoute(route, callback);

        verify(service, atLeastOnce()).UpdateRoute(route, callback);
    }

    @Test
    public void addNewRoute(){
        RouteSaver service = mock(RouteSaver.class);
        Route route = mock(Route.class);
        Boolean done = false;

        doNothing().when(service).addNewRoute(route);

        service.addNewRoute(route);

        verify(service, atLeastOnce()).addNewRoute(route);
    }
}
