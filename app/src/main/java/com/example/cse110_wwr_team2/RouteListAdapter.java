package com.example.cse110_wwr_team2;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.firebasefirestore.FavoriteAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


/*
    This class is an adapter for ArrayAdapter, it can let the UI show ToggleButton and the text as element of list
 */
public class RouteListAdapter extends ArrayAdapter<Route> {
    private List<Route> entries;
    private Activity activity;
    private List<String> routeInfo;


    public RouteListAdapter(Activity a, int textViewResourceId, List<Route> entries){
        super(a,textViewResourceId,entries);
        this.entries = entries;
        routeInfo = null;
        activity = a;
    }

    public RouteListAdapter(Activity a, int textViewResourceId, List<Route> entries,List<String> info){
        super(a,textViewResourceId,entries);
        this.entries = entries;
        activity = a;
        routeInfo = info;
    }

    // the view format of the route entry
    public static class ViewHolder{
        public TextView textView;
        public ToggleButton Star;
        public ToggleButton CheckBox;
    }

    /**
     * get the view with the format of list_item.xml
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if(v == null){
            LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.list_item,null);
            holder = new ViewHolder();
            holder.textView = v.findViewById(R.id.ListText);
            holder.Star = v.findViewById(R.id.favorite_star);
            holder.CheckBox = v.findViewById(R.id.check_box); // Refer to list_item.xml checkbox is unclickable


            // the onClick listener for each star in the list view
            holder.Star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route currRoute = entries.get(position);
                    String userID = CurrentUserInfo.getId(getContext());
                    // using favoriteAdapter to edit firebase data
                    FavoriteAdapter favoriteAdapter = new FavoriteAdapter();
                    if(holder.Star.isChecked()){
                        favoriteAdapter.addToFavorite(userID,currRoute.getId());
                    }
                    else{
                        favoriteAdapter.deleteFavorite(userID,currRoute.getId());
                    }
                }
            });

            v.setTag(holder);
        }
        else
            holder = (ViewHolder)v.getTag();

        final Route currRoute = entries.get(position);
        if(currRoute != null){
            if(routeInfo == null) {
                Log.d("RouteListAdapter","routeInfo == null");
                holder.textView.setText(currRoute.toString());
            }
            else {
                Log.d("RouteListAdapter","routeInfo != null");
                Log.d("RouteListAdapter",routeInfo.get(position));
                holder.textView.setText(routeInfo.get(position));
            }
        }
        String userID = CurrentUserInfo.getId(this.getContext());

        // check on firebase if the current route is a favorite one
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(userID).collection("Favorites").document(currRoute.getId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        holder.Star.setChecked(true);
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        holder.Star.setChecked(false);
                    }
                    Log.d("RouteListAdapter","fetch document successful");

                }
                else{
                    Log.d("RouteListAdapter","fetch document fail");

                }
            }
        });

        // check on firebase if the current route is walked before
        FirebaseFirestore dbw = FirebaseFirestore.getInstance();
        DocumentReference documentReferencew = dbw.collection("Users").document(userID).collection("Walked").document(currRoute.getId());
        documentReferencew.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        holder.CheckBox.setChecked(true);
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        holder.CheckBox.setChecked(false);
                    }
                    Log.d("RouteListAdapter","fetch document successful");

                }
                else{
                    Log.d("RouteListAdapter","fetch document fail");

                }
            }
        });


        return v;
    }


}
