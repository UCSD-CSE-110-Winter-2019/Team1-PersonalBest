package edu.ucsd.cse110.team1_personalbest.Fitness.Observers;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;

public class GoogleFitnessObserver implements FitnessObserver {
    private static final String TAG = "[GoogleFitnessObserver]";
    private TextView steps;
    private TextView deltaSteps;
    private TextView speed;
    private TextView distance;
    private TextView timeElapsed;
    private TextView goal;
    private Context context;
    private Database db;
    private String FILENAME = "steps.json";

    public GoogleFitnessObserver(TextView goal, TextView steps, TextView deltaSteps, TextView speed, TextView distance, TextView timeElapsed, Context c) {
        this.goal = goal;
        this.steps = steps;
        this.deltaSteps = deltaSteps;
        this.speed = speed;
        this.distance = distance;
        this.timeElapsed = timeElapsed;
        this.context = c;
        db = new Database(c);
    }

    public void update(Integer numSteps, Integer numStepsDelta, Long timeElapsed, Float deltaDistance) {
        Log.d(TAG, "observer recieved data: " + numSteps + ", " + numStepsDelta + ", " +
                timeElapsed + ", " + deltaDistance);
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        IDataObject object = db.readDataObject(format.format(calendar.getTime()));
        if (object == null) {
            object = new StepDataObject(0,0,0, format.format(Calendar.DATE));
        }
        if (this.goal != null) {
            this.goal.setText(Integer.toString(object.getDailyStepGoal()));
        }
        if(numSteps != null && this.steps != null) {
            this.steps.setText(Integer.toString(numSteps));
            object.setDailyStepCount(numSteps);
        }
        if(numStepsDelta != null && this.deltaSteps != null)
            this.deltaSteps.setText(Integer.toString(numStepsDelta));
        if(timeElapsed != null && this.speed != null && deltaDistance !=null && this.distance != null) {
            float curDistance = Float.parseFloat(this.distance.getText().toString());
            deltaDistance *= 0.000621371F; // convert to miles
            float newDistance = deltaDistance + curDistance;

            double newSpeed = 0;
            if (timeElapsed > 0) {
                newSpeed = (newDistance / (timeElapsed / 1000)) * 3600;
            }

            long hours = TimeUnit.MILLISECONDS.toHours(timeElapsed);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed - TimeUnit.HOURS.toMillis(hours));
            long seconds = TimeUnit.MILLISECONDS
                    .toSeconds(timeElapsed - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));



            this.timeElapsed.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            this.speed.setText(String.format(Locale.ENGLISH, "%.3f", newSpeed));
            this.distance.setText(String.format(Locale.ENGLISH, "%.3f", newDistance));
        }
        db.putDataObject(object);
    }
}
