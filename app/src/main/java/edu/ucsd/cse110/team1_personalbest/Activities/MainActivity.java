package edu.ucsd.cse110.team1_personalbest.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import edu.ucsd.cse110.team1_personalbest.Fitness.Adapters.GoogleFitAdapter;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Observers.GoogleFitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.Permissions;
import edu.ucsd.cse110.team1_personalbest.R;

public class MainActivity extends AppCompatActivity {

    public static final String GOOGLE_LOGIN = "GLOGIN";
    public static final String GOOGLE_FITNESS = "GFIT";
    public static final String STEP_KEY = "INITIAL_STEPS";
    private FitnessService fitnessService;
    private TextView current_step_view;

    private String login_key;
    private String fitness_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (login_key == null) login_key = GOOGLE_LOGIN;
        if (fitness_key == null) fitness_key = GOOGLE_FITNESS;

        Permissions.requestPermissions(this);
        current_step_view = findViewById(R.id.current_step_view);

        FitnessServiceFactory.put(GOOGLE_FITNESS, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new GoogleFitAdapter(activity, 0);
            }
        });

        LoginServiceFactory.put(GOOGLE_LOGIN, new LoginServiceFactory.BluePrint() {
            @Override
            public LoginService create(Activity activity) {
                return new GoogleLogInService(activity);
            }
        });

        final LoginService loginService = LoginServiceFactory.create(login_key, this);

        if (loginService.login()) {
            setUpFitnessService();
        }

        Button btnStartWalk = findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStepCountActivity();
            }
        });

        // TODO add deamon to do this im just hijacking this button for now
        Button btnViewHist = findViewById(R.id.buttonHistory);

        btnViewHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),MainActivityGraph.class);
                startActivity(intent);

            }
        });


    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, CountStepActivity.class);
        intent.putExtra(STEP_KEY, Integer.parseInt(((TextView)findViewById(R.id.current_step_view)).getText().toString()));
        if ( this.fitnessService != null ) {
            this.fitnessService.stopListening();
            this.fitnessService.removeObservers();
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();

        // show encouragement here
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (this.fitnessService != null) {
            FitnessObserver observer = new GoogleFitnessObserver(current_step_view,
                    null, null, null, null, this);
            this.fitnessService.registerObserver(observer);
            this.fitnessService.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.fitnessService != null) {
            this.fitnessService.removeObservers();
            this.fitnessService.stopListening();
        }
    }

    @Override
    public void onDestroy() {
        if( this.fitnessService != null) {
            this.fitnessService.stopListening();
            this.fitnessService.removeObservers();
        }
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            setUpFitnessService();
        }
    }

    public void setUpFitnessService() {
        FitnessObserver observer = new GoogleFitnessObserver(current_step_view, null, null, null, null, this);
        this.fitnessService = FitnessServiceFactory.create(fitness_key, this);
        if (this.fitnessService !=  null) {
            fitnessService.registerObserver(observer);
            fitnessService.setup();
            fitnessService.startListening();
        }
    }

    public void setKeys(String login_key, String fitness_key) {
        this.login_key = login_key;
        this.fitness_key = fitness_key;
    }
}
