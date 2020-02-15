package com.example.cse110_wwr_team2.fitness;

import com.example.cse110_wwr_team2.MainActivity;
import com.example.cse110_wwr_team2.MockActivity;

public class MockWalkAdapter implements FitnessService {
    MockActivity activity;
    private final double STEP_OVER_HEIGHT = 0.414;

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
        activity.setDistance(activity.getUserHeight() * activity.getCurrStep() * STEP_OVER_HEIGHT);
        activity.setStepCount();
    }
    private void getCurrentStep() {
        activity.setCurrStep(0);
        activity.setDistance(activity.getUserHeight() * activity.getCurrStep() * STEP_OVER_HEIGHT);
        activity.setStepCount();
    }
}
