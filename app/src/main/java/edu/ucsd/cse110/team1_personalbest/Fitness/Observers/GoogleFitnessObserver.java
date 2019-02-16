package edu.ucsd.cse110.team1_personalbest.Fitness.Observers;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;

public class GoogleFitnessObserver implements FitnessObserver {
    private static final String TAG = "[GoogleFitnessObserver]";
    private TextView steps;
    private TextView deltaSteps;
    private TextView speed;
    private TextView distance;
    private TextView timeElapsed;
    private Context context;
    private Database db;
    private String FILENAME = "steps.json";

    public GoogleFitnessObserver(TextView steps, TextView deltaSteps, TextView speed, TextView distance, TextView timeElapsed, Context c) {
        this.steps = steps;
        this.deltaSteps = deltaSteps;
        this.speed = speed;
        this.distance = distance;
        this.timeElapsed = timeElapsed;
        this.context = c;
        db = new Database();
    }

    public void update(Integer numSteps, Integer numStepsDelta, Long timeElapsed, Float deltaDistance) {
        Log.d(TAG, "observer recieved data: " + numSteps + ", " + numStepsDelta + ", " +
                timeElapsed + ", " + deltaDistance);
        if(numSteps != null && this.steps != null) {
            this.steps.setText(Integer.toString(numSteps));
        }
        if(numStepsDelta != null && this.deltaSteps != null)
            this.deltaSteps.setText(Integer.toString(numStepsDelta));
        if(timeElapsed != null && this.speed != null && deltaDistance !=null && this.distance != null) {
            float curDistance = Float.parseFloat(this.distance.getText().toString());
            float newDistance = deltaDistance + curDistance;
            newDistance *= 0.000621371; // convert to miles

            double newSpeed = 0;
            if (timeElapsed > 0) {
                newSpeed = newDistance / (timeElapsed / 1000);
            }

            long hours = TimeUnit.MILLISECONDS.toHours(timeElapsed);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed - TimeUnit.HOURS.toMillis(hours));
            long seconds = TimeUnit.MILLISECONDS
                    .toSeconds(timeElapsed - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

            this.timeElapsed.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            this.speed.setText(String.format(Locale.ENGLISH, "%.3f", newSpeed));
            this.distance.setText(String.format(Locale.ENGLISH, "%.3f", newDistance));
        }

        // TODO refactor this to use simple IDatabase call
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        HashMap<String, Integer> map = new HashMap<>();
        JSONObject obj = db.read(FILENAME, context);
        int steps = 0;
        if ( numSteps != null ) {
            steps = numSteps;
        }
        if ( obj == null ) {
            obj = new JSONObject();
            try {
                map.put("steps", steps);
                obj.put(format.format(date), map);
                db.write(FILENAME, obj, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                steps += obj.getJSONObject(format.format(date)).getInt("steps");
                if ( obj.getJSONObject(format.format(date)).has("goal") ) {
                    map.put("goal", obj.getJSONObject(format.format(date)).getInt("goal"));
                }
                map.put("steps", steps);
                obj.put(format.format(date), map);
                db.write(FILENAME, obj, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Wrote " + steps + " steps to file");
        Log.d(TAG, "JSONObject: " + obj);
    }
}
