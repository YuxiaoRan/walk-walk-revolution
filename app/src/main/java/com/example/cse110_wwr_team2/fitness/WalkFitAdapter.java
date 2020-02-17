package com.example.cse110_wwr_team2.fitness;

import androidx.annotation.NonNull;
import android.util.Log;

import com.example.cse110_wwr_team2.WalkActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class WalkFitAdapter implements FitnessService {
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "WalkFitAdapter";
    private GoogleSignInAccount account;
    private final double STEP_OVER_HEIGHT = 0.414;
    private final double INCH_PER_MILE = 63360;

    private WalkActivity activity;

    public WalkFitAdapter(WalkActivity activity) {
        this.activity = activity;
    }


    public void setup() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();


        account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            getCurrentStep();
            startRecording();
        }
    }

    private void startRecording() {
        if (account == null) {
            return;
        }

        Fitness.getRecordingClient(activity, account)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }


    /**
     *  Reads the current daily step total
     */
    private void getCurrentStep(){
        if (account == null) {
            return;
        }
        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.d(TAG, dataSet.toString());
                                int total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

                                activity.setBaseStep(total);
                                Log.d(TAG, "Total steps so far: " + total);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem getting the current step count.", e);
                            }
                        });
    }

    /**
     * Reads the current daily step total, computed the total step counts after starting a route
     */
    public void updateStepCount() {
        if (account == null) {
            return;
        }

        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.d(TAG, dataSet.toString());
                                int total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                Log.d(TAG, "current steps count: " + total);
                                int base = activity.getBaseStep();
                                activity.setStepCount(total-base);
                                activity.setDistance(getDistance(total-base));
                                Log.d(TAG, "Total steps count travelled: " + total);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem calculating the step count.", e);
                            }
                        });
    }

    private double getDistance(long stepCount){
        return activity.getUserHeight() * stepCount * STEP_OVER_HEIGHT / INCH_PER_MILE;
    }

    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }
}