package edu.ucsd.cse110.team1_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;

import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity.GOOGLE_LOGIN;

public class SetNewGoalActivity extends AppCompatActivity {

    public static final String TAG = "[SetNewGoalActivity]";
    private Database db;
    private String FILENAME = "steps.json";
    private int newGoal;
    IDataObject result;
    private String login_key = GOOGLE_LOGIN;
    private String fitness_key = TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_goal);

        db = new Database(getApplicationContext());

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        result = db.readDataObject(today);
        newGoal = getSuggestedGoal();

        Button btnAcceptSuggestedGoal = (Button) findViewById(R.id.buttonAcceptSuggestedGoal);
        Button btnSetCustomGoal = (Button) findViewById(R.id.buttonSetCustomGoal);
        Button btnCancelSetGoal = (Button) findViewById(R.id.buttonCancelSetGoal);

        btnAcceptSuggestedGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSuggestedGoal(newGoal);
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        btnSetCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomGoalActivity.class);
                startActivity(intent);
            }
        });

        btnCancelSetGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public int getSuggestedGoal(){
        int suggestedGoal = result.getDailyStepGoal();
        suggestedGoal = suggestedGoal + 500;
        TextView newSuggestedGoal = findViewById(R.id.newSuggestedGoal);
        newSuggestedGoal.setText(String.valueOf(suggestedGoal));
        return suggestedGoal;
    }

    public void saveSuggestedGoal(int suggestedGoal){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        IDataObject result = db.readDataObject(today);
        result.setDailyStepGoal(suggestedGoal);
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
