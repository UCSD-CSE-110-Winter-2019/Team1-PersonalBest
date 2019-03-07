package edu.ucsd.cse110.team1_personalbest.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.Encouragement;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Fitness.Adapters.GoogleFitAdapter;
import edu.ucsd.cse110.team1_personalbest.Fitness.Adapters.TestFitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Observers.GoogleFitnessObserver;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.GoogleLogInService;
import edu.ucsd.cse110.team1_personalbest.Login.Adapters.TestLoginService;
import edu.ucsd.cse110.team1_personalbest.Login.Factories.LoginServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Login.Interfaces.LoginService;
import edu.ucsd.cse110.team1_personalbest.Login.Permissions;
import edu.ucsd.cse110.team1_personalbest.R;
import edu.ucsd.cse110.team1_personalbest.SetGoalOptionActivity;
import edu.ucsd.cse110.team1_personalbest.SetNewGoalActivity;

public class MainActivity extends AppCompatActivity {

    public static final String GOOGLE_LOGIN = "GLOGIN";
    public static final String GOOGLE_FITNESS = "GFIT";
    public static final String STEP_KEY = "INITIAL_STEPS";
    public static final String STEP_GOAL_KEY = "STEP_GOAL";
    private FitnessService fitnessService;
    private TextView current_step_view;
    private TextView goal_view;

    private static final String TAG = "[MainActivity]";

    public static String login_key;
    public static String fitness_key;
    public static boolean TESTMODE = false;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (TESTMODE) {
            Toast.makeText(this, "testmode", Toast.LENGTH_LONG).show();
            setKeys("TEST", "TEST");
            LoginServiceFactory.put("TEST", new LoginServiceFactory.BluePrint() {
                @Override
                public LoginService create(Activity activity) {
                    return new TestLoginService();
                }
            });

            FitnessServiceFactory.put("TEST", new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(Activity activity) {
                    return new TestFitnessService();
                }
            });
        }

        db = new Database(getApplicationContext());

        if (login_key == null) login_key = GOOGLE_LOGIN;
        if (fitness_key == null) fitness_key = GOOGLE_FITNESS;

        Log.i(TAG, "Requesting permssions...");
        Permissions.requestPermissions(this);

        current_step_view = findViewById(R.id.current_step_view);
        goal_view = findViewById(R.id.step_goal_view);
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

        Log.i(TAG, "Attempting Login...");
        if (loginService.login()) {
            Log.i(TAG, "Login Successful \n Attempting fitness service setup...");
            setUpFitnessService();
        }

        Button btnStartWalk = findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStepCountActivity();
            }
        });

        Button btnViewHist = findViewById(R.id.buttonHistory);

        btnViewHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),MainActivityGraph.class);
                startActivity(intent);

            }
        });

        setGoal();

        Button btnSetStepGoal = findViewById(R.id.setGoalMain);
        btnSetStepGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetNewGoalActivity.class);
                startActivity(intent);
            }
        });

        Button btnFriends = (Button) findViewById(R.id.buttonFriends);

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsListActivity.class);
                startActivity(intent);
            }
        });


    }

    public void launchStepCountActivity() {
        Log.i(TAG, "Launching Step Count Activity...");
        Intent intent = new Intent(this, CountStepActivity.class);
        intent.putExtra(STEP_KEY, Integer.parseInt(((TextView)findViewById(R.id.current_step_view)).getText().toString()));
        GoogleFitAdapter.putSessionStartTime(this);
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

        setGoal();

        String steps = current_step_view.getText().toString();
        int currSteps = Integer.parseInt(steps);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String preDate = format.format(date);
        IDataObject result = db.readDataObject(preDate);


        if (result != null) {
            int previousSteps = result.getDailyStepCount();

            if(previousSteps != 0)
                if( currSteps >= 1.4 * previousSteps ) {
                    Encouragement enc = new Encouragement(this);
                    enc.showEncouragement(previousSteps, currSteps);
                }
        }

    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (this.fitnessService != null) {
            FitnessObserver observer = new GoogleFitnessObserver(goal_view, current_step_view,
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
            Log.i(TAG, "Login Successfull");
            setUpFitnessService();
        } else {
            Log.e(TAG, "Login failed!! Status code: " + resultCode);
        }
    }

    public void setUpFitnessService() {
        FitnessObserver observer = new GoogleFitnessObserver(goal_view, current_step_view, null, null, null, null, this);
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

    public void setGoal(){

        TextView stepGoal = findViewById(R.id.step_goal_view);
        String currentGoal = stepGoal.getText().toString();
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        IDataObject result = db.readDataObject(today);

        if(result.getDailyStepGoal() == 0){
            //store initial goal
            stepGoal.setText("5");
            result.setDailyStepGoal(5);
            db.putDataObject(result);
        }

        result = db.readDataObject(today);
        stepGoal.setText(Integer.toString(result.getDailyStepGoal()));

    }

    public void setCurrSteps(long currSteps) {
        current_step_view.setText(String.valueOf(currSteps));
    }

    public void setDataBase(StepDataObject day1, StepDataObject day2) {
        db.putDataObject(day1);
        db.putDataObject(day2);
    }
}
