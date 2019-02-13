package edu.ucsd.cse110.team1_personalbest.Activities;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.SessionData;
import edu.ucsd.cse110.team1_personalbest.R;

public class CountStepActivity extends AppCompatActivity {

    public static final String TAG = "[CountStepActivity]";
    private SessionData sessionData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_step);

        Button btnEndWalk = findViewById(R.id.buttonEndWalk);
        final Button historyBtn = findViewById(R.id.buttonHistory2);

        final FitnessService service = FitnessServiceFactory.create(MainActivity.GOOGLE_FITNESS, this);
        service.setup();

        int initialSteps = getIntent().getIntExtra(MainActivity.STEP_KEY, 0);
        ((TextView)findViewById(R.id.total_daily_step_view)).setText(Integer.toString(initialSteps));
        service.startListening(initialSteps);


        btnEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.stopListening();
                finish();
            }
        });


        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /* Call this function to send an encouragement*/
    public void showEncouragement(int previousSteps, int currentSteps, int goalSteps) {
        /* When current steps is nearly doubled the previous steps*/
        if(currentSteps >= 1.8 * previousSteps && currentSteps < 2 * previousSteps && currentSteps < goalSteps)
            showEncouragementForNearlyDouble();

        if(currentSteps >= 1.4 * previousSteps && currentSteps < 1.8 * previousSteps  && currentSteps < goalSteps)
            showEncouragementNotDouble();

        if(currentSteps >= 2 * previousSteps && currentSteps < goalSteps)
            showEncouragementDouble();
    }

    /* Show an encouragement when current steps nearly doubled previous steps*/
    public void showEncouragementForNearlyDouble(){
        Context context = getApplicationContext();
        CharSequence text = "You've nearly doubled your steps. Keep up the good work!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /* Show an encouragement when user significantly improve daily steps */
    public void showEncouragementNotDouble(){
        Context context = getApplicationContext();
        CharSequence text = "Good job! You've made great prgroess!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /* Show an encouragement when user double the step*/
    public void showEncouragementDouble(){
        Context context = getApplicationContext();
        CharSequence text = "Excellent! You've doubled your steps!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
