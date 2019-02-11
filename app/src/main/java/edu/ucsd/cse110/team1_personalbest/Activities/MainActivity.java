package edu.ucsd.cse110.team1_personalbest.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucsd.cse110.team1_personalbest.Fitness.Adapters.GoogleFitAdapter;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.Steps;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.Permissions;
import edu.ucsd.cse110.team1_personalbest.R;
import edu.ucsd.cse110.team1_personalbest.SetGoalOptionActivity;

public class MainActivity extends AppCompatActivity {

    public static final String GOOGLE_LOGIN = "GLOGIN";
    public static final String GOOGLE_FITNESS = "GFIT";
    public static final String STEP_KEY = "INITIAL_STEPS";

    final Steps steps = new Steps(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permissions.requestPermissions(this);

        LoginServiceFactory.put(GOOGLE_LOGIN, new LoginServiceFactory.BluePrint() {
            @Override
            public LoginService create(Activity activity) {
                return new GoogleLogInService(activity);
            }
        });

        final LoginService loginService = LoginServiceFactory.create(GOOGLE_LOGIN, this);
        loginService.login();

        FitnessServiceFactory.put(GOOGLE_FITNESS, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(Activity activity) {
                return new GoogleFitAdapter(activity);
            }
        });

        final FitnessService fitnessService = FitnessServiceFactory.create(GOOGLE_FITNESS, this);
        fitnessService.setup();
        fitnessService.updateStepCount(steps);


        Button btnStartWalk = findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStepCountActivity();
            }
        });

        final TextView stepCount = findViewById(R.id.current_step_view);
        // TODO add deamon to do this im just hijacking this button for now
        Button btnViewHist = findViewById(R.id.buttonHistory);
        btnViewHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (steps.isShouldUpdate()) {
                    stepCount.setText(Integer.toString(steps.getSteps()));
                    fitnessService.updateStepCount(steps);
                }
            }
        });


    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, CountStepActivity.class);
        intent.putExtra(STEP_KEY,steps.getSteps());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
