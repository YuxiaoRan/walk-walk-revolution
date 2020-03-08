package com.example.cse110_wwr_team2.ProposedRoute;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.ProposedRouteCallback;
import com.example.cse110_wwr_team2.firebasefirestore.RouteCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProposedRouteSaver{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;


    public ProposedRouteSaver(Context context){
        this.context = context;
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

        db.collection("Routes")
                .whereEqualTo("userTeamID", teamId)
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
    public static void UpdateRoute(String route_name, String start_point, int step_cnt, float distance, Context context){
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

    /*
     * This function will add a new route into the file, by writing a new name
     * into the Set<String> and update "{route_name}_start_point" and "{route_name}_step_cnt"
     * accordingly
     */
    public void proposeNewRoute(String id, String startPoint, String name, String dataTime, Context context){
        ProposedRouteBuilder builder = new ProposedRouteBuilder();
        ProposedRoute route = builder.setId(id)
                            .setDataTime(dataTime)
                            .setName(name)
                            .setProposerID(CurrentUserInfo.getId(context))
                            .setTeamId(CurrentUserInfo.getTeamId(context))
                            .setStartPoint(startPoint)
                            .getRoute();
        db.collection("Routes").document(route.getId()).set(route);
    }

    public void updateProposedRoute(ProposedRoute route){
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

}
