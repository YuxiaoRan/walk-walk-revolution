package com.example.cse110_wwr_team2.ProposedRoute;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_wwr_team2.Team.Team;
import com.example.cse110_wwr_team2.Team.TeamAdapter;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.GetCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.MapCallBack;
import com.example.cse110_wwr_team2.firebasefirestore.ProposedRouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProposedRouteSaver{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private ArrayList<Observer> observers = new ArrayList<>();


    public ProposedRouteSaver(Context context){
        this.context = context;
    }

    public void register(Observer o){
        observers.add(o);
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
    public void getAllRoutes(ProposedRouteCallback callback){

        String teamId = CurrentUserInfo.getTeamId(context);
        Log.d("teamID", teamId);

        db.collection("ProposedRoutes")
                .whereEqualTo("teamID", teamId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("getAllRoutes","onComplete");
                        if (task.isSuccessful()) {
                            ArrayList<ProposedRoute> routes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ProposedRoute route = document.toObject(ProposedRoute.class);
                                routes.add(route);
                            }
                            callback.onCallback(routes);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /*
     * This function will add a new route into the file, by writing a new name
     * into the Set<String> and update "{route_name}_start_point" and "{route_name}_step_cnt"
     * accordingly
     */


    public void proposeNewRoute(String id, String startPoint, String route_name, String dataTime, Context context){

        ProposedRouteBuilder builder = new ProposedRouteBuilder();
        TeamAdapter ta = new TeamAdapter();
        ta.getAllMap(new MapCallBack() {
            @Override
            public void onCallback(Map<String, Integer> members, FirebaseFirestore db) {
                ta.getTeammatesNames(new GetCallBack() {
                    @Override
                    public void onCallBack(String name) {
                        ProposedRoute route = builder.setId(id)
                                .setDataTime(dataTime)
                                .setName(route_name)
                                .setProposerID(CurrentUserInfo.getId(context))
                                .setTeamId(CurrentUserInfo.getTeamId(context))
                                .setStartPoint(startPoint)
                                .setAcceptMembers(members)
                                .setScheduled(0)
                                .setMessage(name, "proposed a walk")
                                .getRoute();
                        db.collection("ProposedRoutes").document(route.getId()).set(route);
                    }
                }, CurrentUserInfo.getId(context));

            }
        }, CurrentUserInfo.getTeamId(context), CurrentUserInfo.getId(context));
        for(Observer i : observers){
            i.update();
        }
    }

    public void updateProposedRoute(ProposedRoute route, String userID){
        TeamAdapter ta = new TeamAdapter();
        ta.getTeammatesNames(new GetCallBack() {
            @Override
            public void onCallBack(String name) {
                route.setUpdatedUser(name);
                String message = "";
                try {
                    int decision = route.getAcceptMembers().get(userID);
                    switch (decision){
                        case 1:
                            message = "accept the walk";
                            break;
                        case 2:
                            message = "decline the walk due to bad time";
                            break;
                        case 3:
                            message = "decline the walk due to bad place";
                            break;
                        default:
                            break;
                    }
                } catch (NullPointerException e){
                    System.err.println("wrong id");
                }
                route.setUpdatedMessage(message);
                db.collection("ProposedRoutes").document(route.getId()).set(route);
                for(Observer i : observers){
                    i.update();
                }
            }
        }, userID);
        db.collection("ProposedRoutes").document(route.getId()).set(route);
        for(Observer i : observers){
            i.update();
        }
    }


    // clear the mock data saved at midnight
    public static void ClearMockData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MOCKING",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("mock_step", 0);
        editor.putFloat("mock_distance",0);
        editor.commit();
    }

}
