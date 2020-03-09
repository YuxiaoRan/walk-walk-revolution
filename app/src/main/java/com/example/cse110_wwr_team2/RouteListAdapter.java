package com.example.cse110_wwr_team2;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import com.example.cse110_wwr_team2.R;
import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
        public ToggleButton toggleButton;
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
            holder.toggleButton = v.findViewById(R.id.favorite_star);

            holder.toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route currRoute = entries.get(position);
                    String userID = CurrentUserInfo.getId(getContext());
                    if(holder.toggleButton.isChecked()){
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put(currRoute.getId(),null);
                        db.collection("Users").document(userID).collection("Favorites").document(currRoute.getId()).set(map);
                    }
                    else{
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(userID).collection("Favorites").document(currRoute.getId()).delete();
                    }
                }
            });

            v.setTag(holder);
        }
        else
            holder = (ViewHolder)v.getTag();

        final Route currRoute = entries.get(position);
        if(currRoute != null){
            if(routeInfo == null)
                holder.textView.setText(currRoute.toString());
            else
                holder.textView.setText(routeInfo.get(position));
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
                        holder.toggleButton.setChecked(true);
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        holder.toggleButton.setChecked(false);
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
