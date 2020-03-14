package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cse110_wwr_team2.ProposedRoute.Observer;
import com.example.cse110_wwr_team2.ProposedRoute.ProposedRoute;
import com.example.cse110_wwr_team2.ProposedRoute.ProposedRouteSaver;
import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.User.CurrentUserInfo;
import com.example.cse110_wwr_team2.User.User;
import com.example.cse110_wwr_team2.firebasefirestore.ProposedRouteCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The detail of user's responses due to a specific proposed walk
 */
public class ProposedWalkDetailActivity extends AppCompatActivity {
    ListView listView;
    ProposedRoute route;
    Boolean isScheduler;
    ProposedRouteSaver saver = new ProposedRouteSaver(ProposedWalkDetailActivity.this);
    ArrayList<User> users;
    ArrayList<ProposedRoute> routes;

    private class UpdateView implements Observer{
        private ArrayList<ProposedRoute> all_routes;
        private int index;
        public UpdateView(int index){this.index = index;}
        public void update(){
            saver.getAllRoutes(new ProposedRouteCallback() {
                @Override
                public void onCallback(List<ProposedRoute> routes) {
                    all_routes = (ArrayList<ProposedRoute>) routes;
                    ArrayList<String> acceptList = new ArrayList<>();
                    acceptList.add("name4");
                    ProposedRoute r = all_routes.get(index);
                    for(String i : r.getAcceptMembers().keySet()){
                        String name = "";
                        for (User j : users){
                            if(j.getId().equals(i)){
                                name = j.getName();
                                break;
                            }
                        }
                        acceptList.add(name + r.getAcceptMembers().get(i).toString());
                    }
                    AcceptListAdapter adapter = new AcceptListAdapter(ProposedWalkDetailActivity.this, R.layout.adapter_view_layout, acceptList);
                    listView.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(listView);
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler_proposed_walk_detail);
        listView = findViewById(R.id.accept_list);
        // get the string passed from route activity
        Intent intent = getIntent();
        // final String[] route = intent.getStringArrayExtra("route");
        Bundle args = intent.getBundleExtra("BUNDLE");
        routes = (ArrayList<ProposedRoute>) args.getSerializable("route");
        users = (ArrayList<User>) args.getSerializable("users");
        int index = intent.getIntExtra("index",-1);
        UpdateView o = new UpdateView(index);
        saver.register(o);
        route = routes.get(index);
        isScheduler = intent.getBooleanExtra("isScheduler", false);

        TextView name = findViewById(R.id.route_name);
        name.setText(route.getName());
        TextView start_point = findViewById(R.id.start_point);
        start_point.setText(route.getStartPoint());
        TextView data_time = findViewById(R.id.data_time);
        data_time.setText(route.getDataTime());
        TextView status = findViewById(R.id.scheduled);
        if(route.getScheduled() == 0){
            status.setText("Proposed");
        } else if (route.getScheduled() == 1){
            status.setText("Scheduled");
        } else {
            status.setText("Withdrawn");
        }

        TextView scheduler = findViewById(R.id.scheduler);

        for (User j : users){
            if(j.getId().equals(route.getProposerID())){
                scheduler.setText(j.getName());
                break;
            }
        }

        ArrayList<String> acceptList = new ArrayList<>();
        acceptList.add("name4");
        for(String i : route.getAcceptMembers().keySet()){
            String username = "";
            for (User j : users){
                if(j.getId().equals(i)){
                    username = j.getName();
                    break;
                }
            }
            System.out.println(username);
            acceptList.add(username + route.getAcceptMembers().get(i).toString());
        }
        AcceptListAdapter adapter = new AcceptListAdapter(this, R.layout.adapter_view_layout, acceptList);
        listView.setAdapter(adapter);
        justifyListViewHeightBasedOnChildren(listView);

        Button positive = findViewById(R.id.schedule_button);
        Button b2 = findViewById(R.id.back_button1);
        if(isScheduler) {
            b2.setText("Withdraw");
            positive.setText("Schedule");
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    route.setScheduled(2);
                    status.setText("Withdrawn");
                    b2.setVisibility(View.GONE);
                    positive.setVisibility(View.GONE);
                    saver.updateProposedRoute(route, CurrentUserInfo.getId(ProposedWalkDetailActivity.this));
                }
            });
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    route.setScheduled(1);
                    status.setText("Scheduled");
                    positive.setClickable(false);
                    positive.setBackgroundColor(Color.rgb(255, 255, 255));
                    b2.setClickable(true);
                    b2.setTextColor(Color.rgb(255, 0, 0));
                    saver.updateProposedRoute(route, CurrentUserInfo.getId(ProposedWalkDetailActivity.this));
                }
            });
            if (route.getScheduled() == 2) {
                b2.setVisibility(View.GONE);
                positive.setVisibility(View.GONE);
            } else if (route.getScheduled() == 1) {
                b2.setVisibility(View.VISIBLE);
                positive.setVisibility(View.VISIBLE);
                positive.setClickable(false);
                positive.setBackgroundColor(Color.rgb(255, 255, 255));
                b2.setClickable(true);
                b2.setTextColor(Color.rgb(255, 0, 0));
            } else {
                positive.setTextColor(Color.parseColor("#4093F6"));
                positive.setClickable(true);
                b2.setClickable(true);
                b2.setTextColor(Color.rgb(255, 0, 0));
            }
        } else {
            positive.setVisibility(View.VISIBLE);
            b2.setVisibility(View.VISIBLE);
            positive.setText("Accept");
            b2.setText("Decline");
            positive.setTextColor(Color.parseColor("#4093F6"));
            b2.setTextColor(Color.rgb(255, 0, 0));
            Map<String, Integer> m = route.getAcceptMembers();
            int choice = route.getAcceptMembers().get(CurrentUserInfo.getId(ProposedWalkDetailActivity.this));
            if(choice == 1){
                positive.setClickable(false);
                positive.setBackgroundColor(Color.rgb(255, 255, 255));
                b2.setClickable(true);
                b2.setBackgroundColor(Color.rgb(211, 211, 211));
            } else if (choice != 0) {
                positive.setClickable(true);
                positive.setBackgroundColor(Color.rgb(211, 211, 211));
                b2.setClickable(false);
                b2.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Integer> map = route.getAcceptMembers();
                    map.replace(CurrentUserInfo.getId(ProposedWalkDetailActivity.this), 1);
                    route.setAcceptMembers(map);
                    positive.setClickable(false);
                    positive.setBackgroundColor(Color.rgb(255, 255, 255));
                    b2.setClickable(true);
                    b2.setBackgroundColor(Color.rgb(211, 211, 211));
                    saver.updateProposedRoute(route, CurrentUserInfo.getId(ProposedWalkDetailActivity.this));
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positive.setClickable(true);
                    positive.setBackgroundColor(Color.rgb(211, 211, 211));
                    b2.setClickable(false);
                    b2.setBackgroundColor(Color.rgb(255, 255, 255));
//                    b2.setPressed(true);
                    String[] colors = {"Bad time", "Bad place"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProposedWalkDetailActivity.this);
                    builder.setTitle("Pick a reason");
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String, Integer> map = route.getAcceptMembers();
                            map.replace(CurrentUserInfo.getId(ProposedWalkDetailActivity.this), which+2);
                            route.setAcceptMembers(map);
                            saver.updateProposedRoute(route, CurrentUserInfo.getId(ProposedWalkDetailActivity.this));
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

}
