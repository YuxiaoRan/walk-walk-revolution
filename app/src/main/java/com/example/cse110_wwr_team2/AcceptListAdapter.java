package com.example.cse110_wwr_team2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */

public class AcceptListAdapter extends ArrayAdapter<String> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView acceptance;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public AcceptListAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String all = getItem(position);
        String name = all.substring(0, all.length()-1);
        String acceptance = all.substring(all.length()-1);


        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.acceptance = (TextView) convertView.findViewById(R.id.user_acceptance);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        holder.name.setText(name);
        if (acceptance.equals("0")){
            holder.acceptance.setText("Not Decided Yet");
        } else if (acceptance.equals("1")){
            holder.acceptance.setText("Accepted");
        } else if (acceptance.equals("2")){
            holder.acceptance.setText("Declined due to bad time");
        } else if (acceptance.equals("3")){
            holder.acceptance.setText("Declined due to bad place");
        } else {
            holder.acceptance.setText("Decision");
        }


        return result;
    }
}