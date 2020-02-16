package com.example.cse110_wwr_team2.fitness;

import com.example.cse110_wwr_team2.MainActivity;
import com.example.cse110_wwr_team2.MockActivity;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;

import static com.google.android.gms.fitness.data.DataSource.TYPE_RAW;
import static com.google.android.gms.fitness.data.DataType.TYPE_STEP_COUNT_DELTA;

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

//    private void addRouteStat(){
//        DataSource.Builder sourceBuilder = new DataSource.Builder();
//        sourceBuilder.setDataType(TYPE_STEP_COUNT_DELTA);
//        sourceBuilder.setStreamName("Mocking");
//        sourceBuilder.setAppPackageName("com.example.cse110_wwr_team2");
//        sourceBuilder.setType(TYPE_RAW);
//        DataSource dataSource = sourceBuilder.build();
//        DataSet dataSet = DataSet.create(dataSource);
//        DataPoint dataPoint = dataSet.createDataPoint();
//
//        DataPoint.Builder pointBuilder = new DataPoint.Builder().setField(Field.FIELD_STEPS, activity.getCurrStep());
//
//
//    }
}
