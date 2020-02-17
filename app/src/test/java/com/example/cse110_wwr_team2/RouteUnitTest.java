package com.example.cse110_wwr_team2;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RouteUnitTest {

    @Test
    public void testRouteStart() {
        Route newRoute = new Route("UCSD", "Morning Jog", 1275, (float) 1.54);
        assertEquals(newRoute.getStartPoint(),"UCSD");
    }

    @Test
    public void testRouteName() {
        Route newRoute = new Route("UCSD", "Morning Jog", 1275, (float) 1.54);
        assertEquals(newRoute.getName(),"Morning Jog");
    }

    @Test
    public void testRouteStepCount() {
        Route newRoute = new Route("UCSD", "Morning Jog", 1275, (float) 1.54);
        assertEquals(newRoute.getStepCnt(),1275);
    }

    @Test
    public void testRouteDistance() {
        Route newRoute = new Route("UCSD", "Morning Jog", 1275, (float) 1.54);
        assertEquals(newRoute.getDistance(),(float)1.54, 0.01);
    }

    @Test
    public void testSetDistance() {
        Route newRoute = new Route("UCSD", "Morning Jog", 1275, (float) 1.54);
        assertEquals(newRoute.getDistance(),(float)1.54, 0.01);
        newRoute.updateDistance((float)3.75);
        assertEquals(newRoute.getDistance(),(float)3.75, 0.01);
    }

    @Test
    public void testSetStep() {
        Route newRoute = new Route("UCSD", "Morning Jog", 1275, (float) 1.54);
        assertEquals(newRoute.getStepCnt(),1275);
        newRoute.updateStep(2055);
        assertEquals(newRoute.getStepCnt(), 2055);
    }

    @Test
    public void testRouteToString() {
        Route newRoute = new Route("UCSD", "Morning Jog", 1275, (float) 1.54);
        String text = newRoute.toString();
        String actual = "Route Name: Morning Jog Start point: UCSD Step Count: 1275";
        assertEquals(text, actual);
    }
}
