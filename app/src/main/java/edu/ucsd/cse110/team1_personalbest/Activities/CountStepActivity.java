package edu.ucsd.cse110.team1_personalbest.Activities;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import edu.ucsd.cse110.team1_personalbest.CustomGoalActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDatabase;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Fitness.Adapters.GoogleFitAdapter;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Observers.GoogleFitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;
import edu.ucsd.cse110.team1_personalbest.SetNewGoalActivity;

import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity.GOOGLE_LOGIN;
import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity.STEP_KEY;

public class CountStepActivity extends AppCompatActivity {

    public static final String TAG = "[CountStepActivity]";
    private FitnessService service;
    private TextView current_daily_steps;
    private TextView delta_steps;
    private TextView speed;
    private TextView distance;
    private TextView time;


    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_count_step);

        Button btnEndWalk = findViewById(R.id.buttonEndWalk);
        final Button historyBtn = findViewById(R.id.buttonHistory2);
        current_daily_steps = findViewById(R.id.total_daily_step_view);
        delta_steps = findViewById(R.id.exercise_step_view);
        speed = findViewById(R.id.speed_view);
        distance = findViewById(R.id.distance_view);
        time = findViewById(R.id.time_elapsed_view);

        LoginServiceFactory.put(GOOGLE_LOGIN, new LoginServiceFactory.BluePrint() {
            @Override
            public LoginService create(Activity activity) {
                return new GoogleLogInService(activity);
            }
        });

        FitnessServiceFactory.put(TAG, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new GoogleFitAdapter(activity,
                        getIntent().getIntExtra(MainActivity.STEP_KEY, 0));
            }
        });

        final LoginService loginService = LoginServiceFactory.create(MainActivity.login_key, this);

        Log.i(TAG, "Checking login status");
        if (loginService.isLoggedIn()) {
            setUpFitnessService();
            Log.i(TAG, "login found");
        } else {
            Log.e(TAG, "Not Logged in quitting....");
            Toast.makeText(this,"No google account found", Toast.LENGTH_LONG).show();
            this.finish();
        }

        btnEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDatabase db = new Database(getApplicationContext());
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                Calendar calendar = Calendar.getInstance();
                IDataObject object = db.readDataObject(format.format(calendar.getTime()));
                if (object == null) {
                    object = new StepDataObject(0,0,0, format.format(Calendar.DATE));
                }

                object.setDailyIntentionalStepCount(object.getDailyStepCount() +
                        Integer.parseInt(delta_steps.getText().toString()));
                db.putDataObject(object);
                Log.i(TAG, "Completing intentional walk/run");
                if (service != null) {
                    service.stopListening();
                    service.removeObservers();
                }

                db = new Database(getApplication());
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
                String today = format1.format(date);
                IDataObject result = db.readDataObject(today);
                int stepGoal = result.getDailyStepGoal();
                if(Integer.parseInt(current_daily_steps.getText().toString()) == stepGoal){
                    Intent intent = new Intent(getApplicationContext(), CustomGoalActivity.class);
                    startActivity(intent);
                }

                finish();

            }
        });


        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btnSetStepGoal = findViewById(R.id.setGoalCountStep);
        btnSetStepGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetNewGoalActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.service != null) {
            service.removeObservers();
            service.stopListening();
        }

    }


    public void setUpFitnessService() {
        service = FitnessServiceFactory.create(MainActivity.fitness_key, this);
        FitnessObserver observer = new GoogleFitnessObserver(null, current_daily_steps, delta_steps,
                speed, distance, time, this);
        service.setInitialNumSteps(getIntent().getIntExtra(STEP_KEY, 0));
        if (this.service != null) {
            service.registerObserver(observer);
            service.setup();
            service.startListening();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (this.service != null) {
            FitnessObserver observer = new GoogleFitnessObserver(null, this.current_daily_steps,
                    this.delta_steps, this.speed, this.distance, this.time, this);
            this.service.registerObserver(observer);
            this.service.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.service != null) {
            this.service.removeObservers();
            this.service.stopListening();
        }
    }

    public void setKeys(String login_key, String fitness_key) {
        MainActivity.fitness_key = fitness_key;
        MainActivity.login_key = login_key;
    }

}
