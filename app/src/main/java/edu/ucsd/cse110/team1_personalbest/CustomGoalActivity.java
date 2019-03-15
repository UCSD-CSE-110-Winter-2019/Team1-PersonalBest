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

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;

public class CustomGoalActivity extends AppCompatActivity {


    private static final String TAG = "[CustomGoalActivity]";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_goal);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        this.user = UserSession.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "Null User");
            user = new User();
        }


        Button btnCancelCustomGoal = (Button) findViewById(R.id.buttonCancelCustomGoal);
        btnCancelCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnAcceptCustomGoal = (Button) findViewById(R.id.buttonAcceptCustomGoal);
        btnAcceptCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int customGoal = getCustomGoal();
                saveCustomGoal(customGoal);
                finish();
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

        user.setStepGoal(today, cusGoal);
        UserSession.writeUserToDB(user);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notify", true).apply();
    }

}
