package com.example.cse110_wwr_team2.Route;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cse110_wwr_team2.RouteActivity;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.RouteUpdateCallback;
import com.example.cse110_wwr_team2.firebasefirestore.SingleRouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.TeamRouteCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class RouteSaver{

    private FirebaseFirestore db;
    private Context context;


    public RouteSaver(Context context){
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public RouteSaver(){
        db = FirebaseFirestore.getInstance();
    }

    /*
     * This method gets all the routes from the Firebase and put into routes
     * which will be shown in the activity
     * The file is saved in such format:
     *          "route_list" is a Set<String>, and should be a TreeSet<String>
     *               where each String denotes the route_name of a route.
     *          "{route_name}_start_point" stores the String of the startpoint
     *               of the route with route_name
     *          "{route_name}_step_cnt" stores the int number of the step counts
     *               of the route with route_name
     */
    public void getAllRoutes(RouteCallback callback){

        String userId = CurrentUserInfo.getId(context);
        Log.d("userId",userId);

        db.collection("Routes")
                .whereEqualTo("userID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("getAllRoutes","onComplete");
                        if (task.isSuccessful()) {
                            ArrayList<Route> routes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Route route = document.toObject(Route.class);
                                routes.add(route);
                            }
                            callback.onCallback(routes);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getTeamRoutes(TeamRouteCallback callback){
        //String teamID = CurrentUserInfo.getTeamId(context);
        String teamID = "HCteamID";
        Log.d("teamID", teamID);
        String userID = context.getSharedPreferences("user", MODE_PRIVATE).getString("id", null);

        db.collection("Routes")
                .whereEqualTo("userTeamID", teamID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("getAllRoutes","onComplete");
                        if (task.isSuccessful()) {
                            ArrayList<Route> routes = new ArrayList<>();
                            ArrayList<String> routes_info = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Route route = document.toObject(Route.class);
                                if(!userID.equals(route.getUserID())){
                                    routes.add(route);
                                    String route_info = "Route Name: " + route.getName() +
                                            "\nStart point: " + route.getStartPoint() +
                                            "\nCreator: " + route.getUserInitial();
                                    routes_info.add(route_info);
                                }
                            }
                            callback.onCallback(routes, routes_info);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    public void getRoute(String id, SingleRouteCallback callback){
        db.collection("Routes")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                Log.d(TAG, "The route with id "+id+ "is null");
                                callback.onCallback(null);
                            }else {
                                Log.d(TAG, "The route if find");
                                callback.onCallback(task.getResult().getDocuments().get(0).toObject(Route.class));
                            }
                        }else{
                            Log.d(TAG, "fail to find the route with id "+id);
                        }
                    }
                });
    }

    /*
     * This function will update the route into the file, by writing a new name
     * into the FireBase
     */
    public void UpdateRoute(Route route, RouteUpdateCallback callback){
        db.collection("Routes").document(route.getId())
                .update("distance", route.getDistance())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            db.collection("Routes").document(route.getId())
                                   .update("stepCnt", route.getStepCnt())
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               callback.onCallback();
                                           }
                                           else{
                                               Log.d(TAG, "failure to update route's step count");
                                           }
                                       }
                                   });
                        }else{
                            Log.d(TAG, "failure to update route's distance");
                        }
                    }
                });
    }

    public void UpdateRouteMap(Route route, RouteUpdateCallback callback){
        db.collection("Routes").document(route.getId())
                .update("teammateStepCount", route.getTeammateStepCount())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            db.collection("Routes").document(route.getId())
                                    .update("teammateDistance", route.getTeammateDistance())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                callback.onCallback();
                                            }
                                            else{
                                                Log.d(TAG, "failure to update route's teammate distance");
                                            }
                                        }
                                    });
                        }else{
                            Log.d(TAG, "failure to update route's teammate step count");
                        }
                    }
                });
    }

    /*
     * This function will add a new route into the file, by writing a new name
     * into the Set<String> and update "{route_name}_start_point" and "{route_name}_step_cnt"
     * accordingly
     */
    public static void UpdateRoute(String route_id, String route_name, String start_point, int step_cnt, float distance, Context context){
        SharedPreferences spfs = context.getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        SharedPreferences.Editor editor = spfs.edit();
        try {
            routes_list.remove(route_name);
            routes_list.add(route_name);
            editor.putStringSet("route_list", routes_list);
            editor.putString(route_name + "_start_point", start_point);
            editor.putInt(route_name + "_step_cnt", step_cnt);
            editor.putFloat(route_name+"_distance",distance);
            editor.apply();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    // the database version of the above method
    public void UpdateRoute(Route route){
        db.collection("Routes").document(route.getId()).set(route);
    }

    /*
     * This function will add a new route into the file, by writing a new name
     * into the Set<String> and update "{route_name}_start_point" and "{route_name}_step_cnt"
     * accordingly
     */
    public void addNewRoute(String route_name, String start_point, int step_cnt, float distance,
                                   String note_txt, String features, Context context){

        String userId = CurrentUserInfo.getId(context);
        Route route = new Route(start_point,route_name,step_cnt,note_txt,features,distance,userId);
        route.setUserInitial(User.getInitial(context.getSharedPreferences("user", MODE_PRIVATE).getString("name", null)));
        db.collection("Routes").document(route.getId()).set(route);
    }

    public void addNewRoute(Route route){
        route.setUserInitial(User.getInitial(context.getSharedPreferences("user", MODE_PRIVATE).getString("name", null)));
        db.collection("Routes").document(route.getId()).set(route);
    }


    // clear the mock data saved at midnight
    public static void ClearMockData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MOCKING",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mock_step", 0);
        editor.putFloat("mock_distance",0);
        editor.commit();
    }

    /**
     *
     * @param userID
     * @param routeID
     * update in firebase of userID of route with routeID
     */
    public void initializFavorite(String userID, String routeID){
        db.collection("Users").document(userID).collection("Favorites").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){

                    Log.d("Route Saver","no favorites");
                }
            }
        });
    }


}
