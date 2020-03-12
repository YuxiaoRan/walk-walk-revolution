package com.example.cse110_wwr_team2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.Route.RouteSaver;
import com.example.cse110_wwr_team2.User.UserOnlineSaver;
import com.example.cse110_wwr_team2.firebasefirestore.LastWalkIDCallback;
import com.example.cse110_wwr_team2.firebasefirestore.SingleRouteCallback;
import com.example.cse110_wwr_team2.fitness.FitnessService;
import com.example.cse110_wwr_team2.fitness.FitnessServiceFactory;
import com.example.cse110_wwr_team2.fitness.MainFitAdapter;
import com.example.cse110_wwr_team2.fitness.WalkFitAdapter;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    public String mainKey = "main";
    private String walkKey = "walk";
    private FitnessService fitnessService;

    private Button toRoute;
    private Button startRoute;
    private Button mock;
    private Button toTeam;
    private Button toProposed;
    private TextView stepCount;
    private TextView CurrDistance;
    private TextView lastStepCnt;
    private TextView lastDist;
    private TextView lastTime;

    private WalkTracker walkTracker;
    private boolean isCancel;
    private final long TEN_SEC = 10 * 1000;

    private final double STEP_OVER_HEIGHT = 0.414;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        // NOTE: for InputHeight page test only
        //clearUserInfo();

        // NOTE: for route details test only
        //clearRouteDetails();
        //clearAllRoute();

        // check if user has input height
        // checkUserInputHeight();

        Intent i = getIntent();
        boolean isMock = i.getBooleanExtra("test_label", false);

        stepCount = findViewById(R.id.main_step_count);
        CurrDistance = findViewById(R.id.main_distance);

        lastStepCnt = findViewById(R.id.main_intention_step_count);
        lastDist = findViewById(R.id.main_intention_distance);
        lastTime = findViewById(R.id.main_intention_time);
        setUpLastStat();

        toTeam = findViewById(R.id.team_btn);
        toTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTeam();
            }
        });

        toProposed = findViewById(R.id.proposed_btn);
        toProposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProposed();
            }
        });


        // not mocking
        if(!isMock) {
            FitnessServiceFactory.put(mainKey, new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(AppCompatActivity mainActivity) {
                    return new MainFitAdapter((MainActivity) mainActivity);
                }
            });
            FitnessServiceFactory.put(walkKey, new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(AppCompatActivity walkActivity) {
                    return new WalkFitAdapter((WalkActivity) walkActivity);
                }
            });
            fitnessService = FitnessServiceFactory.create(mainKey, this);
            fitnessService.setup();

            walkTracker = new WalkTracker();
            isCancel = false;
            walkTracker.execute();

            startRoute = (Button) findViewById(R.id.button_start);
            startRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isCancel = true;
                    walkTracker.cancel(isCancel);
                    goToWalk();
                }
            });

            toRoute = (Button) findViewById(R.id.button_route);
            toRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isCancel = true;
                    walkTracker.cancel(isCancel);
                    goToRoute();
                }
            });

            mock = findViewById(R.id.mock_btn);
            mock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCancel = true;
                    walkTracker.cancel(isCancel);
                    goToMock();
                }
            });
        }
        // while unit testing, avoid using walktracker
        else {
            startRoute = (Button) findViewById(R.id.button_start);
            startRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToWalk();
                }
            });

            toRoute = (Button) findViewById(R.id.button_route);
            toRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToRoute();
                }
            });

            mock = findViewById(R.id.mock_btn);
            mock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToMock();
                }
            });
        }

    }

    private void goToTeam(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), TeamActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }


    private void goToRoute() {
        Intent intent = new Intent(this, RouteDirectorActivity.class);
        startActivity(intent);
    }
  
    private void goToWalk() {
        Intent intent = new Intent(this, WalkActivity.class);
        String routeName = null;
        intent.putExtra("routeName", routeName);
        intent.putExtra("walkKey",walkKey);
        startActivity(intent);
    }

    private void goToMock(){
        Intent intent = new Intent(this, InputMockTime.class);
        startActivity(intent);
    }

    private void goToProposed(){
        Intent intent = new Intent(this, AllProposedActivity.class);
        startActivity(intent);
    }

    /**
     * the method to set up last intentional step count and distance
     */
    private void setUpLastStat(){
        String userID = getSharedPreferences("user", MODE_PRIVATE).getString("id", null);
        final String[] routeID = {""};
        UserOnlineSaver userSaver = new UserOnlineSaver();
        userSaver.getLastestWalkID(userID, new LastWalkIDCallback() {
            @Override
            public void onCallback(String walkID) {
                routeID[0] = walkID;
                RouteSaver routeSaver = new RouteSaver();
                routeSaver.getRoute(routeID[0], new SingleRouteCallback() {
                    @Override
                    public void onCallback(Route route) {
                        if (route == null){
                            lastStepCnt.setText(Integer.toString(0));
                            lastDist.setText(Float.toString(0));
                        }else {
                            if (route.getUserID().equals(userID)) {
                                lastStepCnt.setText(Integer.toString(route.getStepCnt()));
                                lastDist.setText(Float.toString((float) route.getDistance()));
                            } else {
                                int userStepCnt = route.getTeammateStepCount().get(userID);
                                float userDistance = route.getTeammateDistance().get(userID);
                                lastStepCnt.setText(Integer.toString(userStepCnt));
                                lastDist.setText(Float.toString(userDistance));
                            }
                        }
                    }
                });
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("recent_route", MODE_PRIVATE);
        //int lastStep = sharedPreferences.getInt("recent_step_cnt", 0);
        //float lastDistance = sharedPreferences.getFloat("recent_distance", 0);
        String startTime = sharedPreferences.getString("time", "NAN");
        //Log.d(TAG, "setUpLastStat: "+"lastStepCount "+lastStep+" lastDistance "+lastDistance);
        //lastStepCnt.setText(Integer.toString(lastStep));
        //lastDist.setText(Float.toString(lastDistance));
        lastTime.setText(startTime);
    }

    private void clearUserInfo() {
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.clear().commit();
    }

    private void clearRouteDetails() {
        SharedPreferences spfs = getSharedPreferences("route_list", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.clear().commit();
    }

    private void clearAllRoute() {
        SharedPreferences spfs = getSharedPreferences("all_routes", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfs.edit();
        editor.clear().commit();
    }

    public void setStepCount(int total){
        stepCount.setText(String.valueOf(total));
    }

    public void setCurrDistance(double distance){
        CurrDistance.setText(String.valueOf(distance));
    }

    public void setMainKey(String mainKey) {
        this.mainKey = mainKey;
    }

    public int getUserHeight(){
        SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
        int height = spfs.getInt("height",0);
        return height;
    }

    public void ClearMockData(){
        RouteSaver.ClearMockData(this);
    }

    private class WalkTracker extends AsyncTask<String, String, String> {
        private String resp;

        protected String doInBackground(String... param){
            try{
                Log.d("TAG","In Task");
                while(!isCancel) {
                    publishProgress(resp);
                    long time = TEN_SEC;
                    Thread.sleep(time);
                }
            }catch(Exception e){
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result){

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            fitnessService.updateStepCount();
        }

        @Override
        protected  void onCancelled(){
            super.onCancelled();
            Log.d("TAG","onCancelled");
        }
    }

    private void subscribeToNotificationsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("ProposedRoutes")
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }

}
