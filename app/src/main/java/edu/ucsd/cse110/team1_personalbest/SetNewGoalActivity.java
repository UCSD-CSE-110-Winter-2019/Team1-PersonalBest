package edu.ucsd.cse110.team1_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;

import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity.GOOGLE_LOGIN;

public class SetNewGoalActivity extends AppCompatActivity {

    public static final String TAG = "[SetNewGoalActivity]";
    private int newGoal;
    private User user;
    private String login_key = GOOGLE_LOGIN;
    private String fitness_key = TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_goal);


        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);


        this.user = UserSession.getCurrentUser();
        if (user == null) {
            Log.e("SetNewGoal", "Null User");
            user = new User();
        }

        newGoal = getSuggestedGoal();
        Button btnAcceptSuggestedGoal = (Button) findViewById(R.id.buttonAcceptSuggestedGoal);
        Button btnSetCustomGoal = (Button) findViewById(R.id.buttonSetCustomGoal);
        Button btnCancelSetGoal = (Button) findViewById(R.id.buttonCancelSetGoal);

        btnAcceptSuggestedGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSuggestedGoal(newGoal);
                finish();
            }
        });

        btnSetCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomGoalActivity.class);
                startActivity(intent);
                finish();
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
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        int suggestedGoal = user.getStepGoal(today);
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

        user.setStepGoal(today, suggestedGoal);
        UserSession.writeUserToDB(user);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notify", true).apply();
    }

    public void setKeys(String login_key, String fitness_key) {
        this.login_key = login_key;
        this.fitness_key = fitness_key;
    }
}
