package edu.ucsd.cse110.team1_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;

public class CustomGoalActivity extends AppCompatActivity {

    private Database db;
    private String FILENAME = "steps.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_goal);

        Button btnCancelCustomGoal = (Button) findViewById(R.id.buttonCancelCustomGoal);
        btnCancelCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button btnAcceptCustomGoal = (Button) findViewById(R.id.buttonAcceptCustomGoal);
        btnAcceptCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomGoal(v);
            }
        });

        db = new Database(getApplicationContext());
    }

    public void saveCustomGoal(View view){

        EditText newGoal = (EditText) findViewById(R.id.customGoal);
/*
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        IDataObject result = db.readDataObject(today);
        result.setDailyStepGoal(Integer.parseInt(newGoal.getText().toString()));
*/
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
