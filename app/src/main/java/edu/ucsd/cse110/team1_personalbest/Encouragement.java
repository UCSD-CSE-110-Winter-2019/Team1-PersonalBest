package edu.ucsd.cse110.team1_personalbest;

import android.content.Context;
import android.widget.Toast;

public class Encouragement {
    private Context context;

    public Encouragement(Context context) {
        this.context = context;
    }

    /* Call this function to send an encouragement*/
    public void showEncouragement(int previousSteps, int currentSteps) {
        /* When current steps is nearly doubled the previous steps*/
        if(currentSteps >= 1.8 * previousSteps && currentSteps < 2 * previousSteps)
            showEncouragementForNearlyDouble();

        if(currentSteps >= 1.4 * previousSteps && currentSteps < 1.8 * previousSteps)
            showEncouragementNotDouble();

        if(currentSteps >= 2 * previousSteps)
            showEncouragementDouble();
    }

    /* Show an encouragement when current steps nearly doubled previous steps*/
    public void showEncouragementForNearlyDouble(){
        CharSequence text = "You've nearly doubled your steps. Keep up the good work!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /* Show an encouragement when user significantly improve daily steps */
    public void showEncouragementNotDouble(){
        CharSequence text = "Good job! You've made great prgroess!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /* Show an encouragement when user double the step*/
    public void showEncouragementDouble(){
        CharSequence text = "Excellent! You've doubled your steps!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
