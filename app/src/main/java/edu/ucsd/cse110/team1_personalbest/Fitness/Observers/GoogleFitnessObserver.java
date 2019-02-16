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

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;

public class GoogleFitnessObserver implements FitnessObserver {
    private static final String TAG = "[GoogleFitnessObserver]";
    private TextView steps;
    private TextView deltaSteps;
    private TextView speed;
    private TextView distance;
    private Context context;
    private Database db;
    private String FILENAME = "steps.json";

    public GoogleFitnessObserver(TextView steps, TextView deltaSteps, TextView speed, TextView distance, Context c) {
        this.steps = steps;
        this.deltaSteps = deltaSteps;
        this.speed = speed;
        this.distance = distance;
        this.context = c;
        db = new Database();
    }

    public void update(Integer numSteps, Integer numStepsDelta, Long timeElapsed, Float deltaDistance) {
        if(numSteps != null && this.steps != null) {
            this.steps.setText(Integer.toString(numSteps));
        }
        if(numStepsDelta != null && this.deltaSteps != null)
            this.deltaSteps.setText(Integer.toString(numStepsDelta));
        if(timeElapsed != null && this.speed != null && deltaDistance !=null && this.distance != null) {
            float curDistance = Float.parseFloat(this.distance.getText().toString());
            float newDistance = deltaDistance + curDistance;

            double newSpeed = 0;
            if (timeElapsed > 0) {
                newSpeed = newDistance / (timeElapsed / 1000);
            }

            this.speed.setText(String.format(Locale.ENGLISH, "%.2f", newSpeed));
            this.distance.setText(String.format(Locale.ENGLISH, "%.2f", newDistance));
        }
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
