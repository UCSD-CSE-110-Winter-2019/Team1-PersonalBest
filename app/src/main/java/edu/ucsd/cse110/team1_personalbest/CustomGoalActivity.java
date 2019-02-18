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
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;

public class CustomGoalActivity extends AppCompatActivity {

    public static final String GOOGLE_LOGIN = "GLOGIN";
    public static final String GOOGLE_FITNESS = "GFIT";
    public static final String STEP_KEY = "INITIAL_STEPS";
    public static final String STEP_GOAL_KEY = "STEP_GOAL";
    private FitnessService fitnessService;
    private TextView current_step_view;

    private static final String TAG = "[CustomGoalActivity]";

    private String login_key;
    private String fitness_key;

    private Database db;
    private String FILENAME = "steps.json";
    IDataObject result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_goal);

        db = new Database(getApplicationContext());

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

                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                String today = format.format(date);
                result = db.readDataObject(today);

                int customGoal = getCustomGoal();
                saveCustomGoal(customGoal);
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    public int getCustomGoal(){
        EditText newGoal = (EditText) findViewById(R.id.customGoal);
        return Integer.parseInt(newGoal.getText().toString());
    }

    public void saveCustomGoal(int cusGoal){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        IDataObject result = db.readDataObject(today);
        result.setDailyStepGoal(cusGoal);
        db.putDataObject(result);
    }

    public void setKeys(String login_key, String fitness_key) {
        this.login_key = login_key;
        this.fitness_key = fitness_key;
    }

    public void setDataBase(StepDataObject day1) {
        db.putDataObject(day1);
    }

    public Database getDataBase() {
        return this.db;
    }
}
