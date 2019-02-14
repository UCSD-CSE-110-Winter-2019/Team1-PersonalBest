package edu.ucsd.cse110.team1_personalbest.Fitness.Observers;

import android.content.Context;
import android.widget.TextView;

import java.util.Locale;

import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessObserver;

public class GoogleFitnessObserver implements FitnessObserver {
    private TextView steps;
    private TextView deltaSteps;
    private TextView speed;
    private TextView distance;
    private Context context;

    public GoogleFitnessObserver(TextView steps, TextView deltaSteps, TextView speed, TextView distance, Context c) {
        this.steps = steps;
        this.deltaSteps = deltaSteps;
        this.speed = speed;
        this.distance = distance;
        this.context = c;
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
    }
}
