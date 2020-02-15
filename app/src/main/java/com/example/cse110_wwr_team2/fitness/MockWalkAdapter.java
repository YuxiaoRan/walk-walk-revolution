package com.example.cse110_wwr_team2.fitness;

import com.example.cse110_wwr_team2.MainActivity;
import com.example.cse110_wwr_team2.MockActivity;

public class MockWalkAdapter implements FitnessService {
    MockActivity activity;
    private final double STEP_OVER_HEIGHT = 0.414;
    private final double INCH_PER_MILE = 63360;

    public MockWalkAdapter(MockActivity activity){
        this.activity = activity;
    }
    @Override
    public int getRequestCode(){
        return 0;
    }
    @Override
    public void setup(){
        getCurrentStep();
    }
    @Override
    public void updateStepCount(){
        activity.incrementStep();
        activity.setDistance(getDistance(activity.getCurrStep()));
        activity.setStepCount();
    }
    private void getCurrentStep() {
        activity.setCurrStep(0);
        activity.setDistance(getDistance(0));
        activity.setStepCount();
    }
    private double getDistance(long stepCount){
        return activity.getUserHeight() * stepCount * STEP_OVER_HEIGHT / INCH_PER_MILE;
    }
}
