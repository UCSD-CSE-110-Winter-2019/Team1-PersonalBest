package edu.ucsd.cse110.team1_personalbest.Fitness.Observers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.team1_personalbest.CustomGoalActivity;
import edu.ucsd.cse110.team1_personalbest.Encouragement;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.StepDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.User;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
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
    private String FILENAME = "steps.json";

    public GoogleFitnessObserver(TextView goal, TextView steps, TextView deltaSteps, TextView speed, TextView distance, TextView timeElapsed, Context c) {
        this.goal = goal;
        this.steps = steps;
        this.deltaSteps = deltaSteps;
        this.speed = speed;
        this.distance = distance;
        this.timeElapsed = timeElapsed;
        this.context = c;
    }

    public void update(Integer numSteps, Integer numStepsDelta, Long timeElapsed, Float deltaDistance) {
        Log.d(TAG, "observer recieved data: " + numSteps + ", " + numStepsDelta + ", " +
                timeElapsed + ", " + deltaDistance);
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = format.format(calendar.getTime());
        User user = UserSession.getCurrentUser();
        if (user == null) user = new User();

        if (this.goal != null) {
            this.goal.setText(Integer.toString(user.getStepGoal(today)));
        }
        if(numSteps != null && this.steps != null) {
            this.steps.setText(Integer.toString(numSteps));
            user.setDailySteps(today,numSteps);
        }
        if(numStepsDelta != null && this.deltaSteps != null) {
            this.deltaSteps.setText(Integer.toString(numStepsDelta));
        }

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

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (pref != null && numSteps != null) {
            Integer stepGoal = user.getStepGoal(today);
            int stepGoalSanitized = 0;
            if (stepGoal != null) stepGoalSanitized = stepGoal;
            boolean messageShown = pref.getBoolean(format.format(calendar.getTime()), false);
            if (stepGoalSanitized != 0 && numSteps != 0 && !messageShown) {
                if (numSteps >= stepGoalSanitized) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(format.format(calendar.getTime()), true);
                    editor.apply();
                    Intent intent = new Intent(context, CustomGoalActivity.class);
                    Toast.makeText(context, "Goal met! Great Job!", Toast.LENGTH_LONG).show();
                    context.startActivity(intent);
                }
            }
        }

        if (numSteps != null) {
            Encouragement encouragement = new Encouragement(context);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date date = cal.getTime();
            String preDate = format.format(date);
            encouragement.showEncouragement(user.getDailySteps(preDate), numSteps);
        }
        UserSession.setCurrentUser(user);
    }
}
