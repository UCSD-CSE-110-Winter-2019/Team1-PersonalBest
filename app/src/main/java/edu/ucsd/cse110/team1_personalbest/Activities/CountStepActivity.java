package edu.ucsd.cse110.team1_personalbest.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        Button btnEndWalk = (Button) findViewById(R.id.buttonEndWalk);
        final Button historyBtn = findViewById(R.id.buttonHistory2);

        final SessionData sessionData = new SessionData();

        final FitnessService service = FitnessServiceFactory.create(MainActivity.GOOGLE_FITNESS, this);
        service.setup();

        // TODO fix an error here that causes crash and no data appearing
        //service.startListening();
        //service.updateSessionData(sessionData);

        btnEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //service.stopListening();
                finish();
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionData.isShouldUpdate())
                    Toast.makeText(getBaseContext(), sessionData.toString(), Toast.LENGTH_LONG).show();
                    service.updateSessionData(sessionData);
            }
        });
    }

    /* Show an encouragement when current steps is half of the goal steps */
    public void showEncouragementForDouble(){
        Context context = getApplicationContext();
        CharSequence text = "You've nearly doubled your steps. Keep up the good work!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /* Show an encouragement when click the end button and current steps is not greater than goal steps */
    public void showEncouragementAtEnd(int currentSteps, int goalSteps){
        // Round double value to 2 decimal points
        double percentage = Math.round(currentSteps/goalSteps*10000.0)/100.0;

        Context context = getApplicationContext();
        CharSequence text = "Good job! You're already at " + percentage + "% of the daily recommended number of steps.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
