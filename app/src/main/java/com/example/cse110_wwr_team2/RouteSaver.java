package com.example.cse110_wwr_team2;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static android.content.Context.MODE_PRIVATE;

public class RouteSaver {

    /*
     * This method gets all the routes from the local file and put into routes
     * which will be shown in the activity
     * The file is saved in such format:
     *          "route_list" is a Set<String>, and should be a TreeSet<String>
     *               where each String denotes the route_name of a route.
     *          "{route_name}_start_point" stores the String of the startpoint
     *               of the route with route_name
     *          "{route_name}_step_cnt" stores the int number of the step counts
     *               of the route with route_name
     */
    public static ArrayList<Route> getAllRoutes(Context context){
        SharedPreferences spfs = context.getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        Iterator<String> itr = routes_list.iterator();
        ArrayList<Route> routes = new ArrayList<Route>();
        while(itr.hasNext()){
            String route_name = itr.next();
            String route_start_point = spfs.getString(route_name + "_start_point", "");
            int step_cnt = spfs.getInt(route_name+"_step_cnt", 0);
            float distance = spfs.getFloat(route_name+"_distance", 0);
            routes.add(new Route(route_start_point, route_name, step_cnt,distance));
        }
        return routes;
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
    public static void addNewRoute(String route_name, String start_point, int step_cnt, float distance,Context context){
        SharedPreferences spfs = context.getSharedPreferences("all_routes", MODE_PRIVATE);
        Set<String> routes_list = spfs.getStringSet("route_list", new TreeSet<String>());
        SharedPreferences.Editor editor = spfs.edit();
        try {
            routes_list.add(route_name);
            editor.putStringSet("route_list", routes_list);
            editor.putString(route_name + "_start_point", start_point);
            editor.putInt(route_name + "_step_cnt", step_cnt);
            editor.putFloat(route_name + "_distance", distance);
            editor.apply();
        }catch (Exception e){
            System.err.println(e);
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
