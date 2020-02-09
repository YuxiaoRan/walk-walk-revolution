package com.example.cse110_wwr_team2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputHeightActivity extends AppCompatActivity {

    EditText heightInput;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_height);

        heightInput = findViewById(R.id.input_height);
        btnDone = findViewById(R.id.button_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spfs = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = spfs.edit();

                // handle invalid number
                int heightInt = 0;
                try {
                    // save input and jump to main page
                    heightInt = Integer.parseInt(heightInput.getText().toString());
                    // handle non-positive height
                    if(heightInt <= 0) {
                        Toast.makeText(InputHeightActivity.this,
                                "Please input a positive height", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editor.putInt("height", heightInt);
                    editor.putBoolean("firstLogin", false);
                    editor.apply();
                    Toast.makeText(InputHeightActivity.this,
                            "Height saved", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (NumberFormatException e) {
                    // prompt the user to re-input
                    Toast.makeText(InputHeightActivity.this,
                            "Please input a whole number", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
