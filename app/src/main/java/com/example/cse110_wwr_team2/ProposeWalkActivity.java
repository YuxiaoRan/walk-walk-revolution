package com.example.cse110_wwr_team2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.cse110_wwr_team2.ProposedRoute.ProposedRoute;
import com.example.cse110_wwr_team2.ProposedRoute.ProposedRouteSaver;
import com.example.cse110_wwr_team2.Route.Route;
import com.example.cse110_wwr_team2.firebasefirestore.ProposedRouteCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProposeWalkActivity extends AppCompatActivity {
    private Route route;
    private EditText date;
    private EditText time;
    private DatePickerDialog datepicker;
    private TimePickerDialog timepicker;
    private ProposedRouteSaver saver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_walk);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        route = (Route) args.getSerializable("route");

        TextView name = findViewById(R.id.about);
        name.setText(route.toString());
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        date.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(ProposeWalkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // date picker dialog
                timepicker = new TimePickerDialog(ProposeWalkActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                time.setText(i + ":" + i1);
                            }
                        }, hour, minutes, true);
                timepicker.show();
            }
        });
        Button propose = findViewById(R.id.propose);
        propose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(time.getText().toString().equals("") || date.getText().toString().equals("")){
                    Toast.makeText(ProposeWalkActivity.this,
                            "Please input date and time", Toast.LENGTH_SHORT).show();
                } else {
                    saver = new ProposedRouteSaver(ProposeWalkActivity.this);
                    saver.proposeNewRoute(route.getId(), route.getStartPoint(), route.getName(), "", ProposeWalkActivity.this);
                    finish();
                }
            }
        });
    }

}
