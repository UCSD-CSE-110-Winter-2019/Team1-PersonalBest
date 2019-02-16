package edu.ucsd.cse110.team1_personalbest.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Fitness.Adapters.GoogleFitAdapter;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Observers.GoogleFitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;

import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity.GOOGLE_LOGIN;

public class CountStepActivity extends AppCompatActivity {

    public static final String TAG = "[CountStepActivity]";
    private static final String FILENAME = "steps.json";
    private FitnessService service;
    private TextView current_daily_steps;
    private TextView delta_steps;
    private TextView speed;
    private TextView distance;
    private Database db;

    private String login_key = GOOGLE_LOGIN;
    private String fitness_key = TAG;

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
        db = new Database();

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

        final LoginService loginService = LoginServiceFactory.create(login_key, this);

        if (loginService.isLoggedIn()) {
            setUpFitnessService();
        } else {
            Toast.makeText(this,"No google account found", Toast.LENGTH_LONG).show();
            this.finish();
        }



        btnEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null) {
                    service.stopListening();
                    service.removeObservers();
                }
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                HashMap<String, Integer> map = new HashMap<>();
                JSONObject obj = db.read(FILENAME, getApplicationContext());
                map.put("steps", Integer.valueOf(delta_steps.getText().toString()));
                if ( obj == null ) {
                    obj = new JSONObject();
                    try {
                        obj.put(format.format(date), map);
                        db.write(FILENAME, obj, getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        int steps = obj.getJSONObject(format.format(date)).getInt("steps");
                        map.put("steps", map.get("steps") + steps);
                        obj.put(format.format(date), map);
                        db.write(FILENAME, obj, getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });


        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        service = FitnessServiceFactory.create(fitness_key, this);
        FitnessObserver observer = new GoogleFitnessObserver(current_daily_steps, delta_steps,
                speed, distance, this);
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
            FitnessObserver observer = new GoogleFitnessObserver(this.current_daily_steps,
                    this.delta_steps, this.speed, this.distance, this);
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
        this.fitness_key = fitness_key;
        this.login_key = login_key;
    }

}
