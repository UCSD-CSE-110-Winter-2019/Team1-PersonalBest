package edu.ucsd.cse110.team1_personalbest.Activities;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.team1_personalbest.Fitness.Adapters.GoogleFitAdapter;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.SessionData;
import edu.ucsd.cse110.team1_personalbest.Fitness.Observers.GoogleFitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.R;

import static edu.ucsd.cse110.team1_personalbest.Activities.MainActivity.GOOGLE_LOGIN;

public class CountStepActivity extends AppCompatActivity {

    public static final String TAG = "[CountStepActivity]";
    private FitnessService service;
    private TextView current_daily_steps;
    private TextView delta_steps;
    private TextView speed;
    private TextView distance;
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


        LoginServiceFactory.put(GOOGLE_LOGIN, new LoginServiceFactory.BluePrint() {
            @Override
            public LoginService create(Activity activity) {
                return new GoogleLogInService(activity);
            }
        });

        FitnessObserver observer = new GoogleFitnessObserver(current_daily_steps, delta_steps,
                speed, distance, this);
        final List<FitnessObserver> observers = new ArrayList<>();
        observers.add(observer);

        FitnessServiceFactory.put(TAG, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new GoogleFitAdapter(activity, observers,
                        getIntent().getIntExtra(MainActivity.STEP_KEY, 0));
            }
        });

        final LoginService loginService = LoginServiceFactory.create(GOOGLE_LOGIN, this);

        if (loginService.login()) {
            setUpFitnessService();
        } else {
            Toast.makeText(this,"No google account found", Toast.LENGTH_LONG).show();
        }



        btnEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null) {
                    service.stopListening();
                    service.removeObservers();
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
        service = FitnessServiceFactory.create(TAG, this);
        service.setup();

//        int initialSteps = getIntent().getIntExtra(MainActivity.STEP_KEY, 0);
//        ((TextView)findViewById(R.id.total_daily_step_view)).setText(Integer.toString(initialSteps));

        service.startListening();
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

}
