package edu.ucsd.cse110.team1_personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        if (!getSystemPreference(1) && context != null) {
            CharSequence text = "You've nearly doubled your steps. Keep up the good work!";
            int duration = Toast.LENGTH_LONG;
            updateSystemPreferences(1, true);
            Toast toast = Toast.makeText(context, text, duration);
            if (toast != null)
                toast.show();
        }
    }

    /* Show an encouragement when user significantly improve daily steps */
    public void showEncouragementNotDouble(){
        if (!getSystemPreference(2) && context != null) {
            CharSequence text = "Good job! You've made great prgroess!";
            int duration = Toast.LENGTH_LONG;
            updateSystemPreferences(2,true);
            Toast toast = Toast.makeText(context, text, duration);
            if (toast != null)
                toast.show();
        }
    }

    /* Show an encouragement when user double the step*/
    public void showEncouragementDouble(){
        if (!getSystemPreference(3) && context != null) {
            updateSystemPreferences(3, true);
            CharSequence text = "Excellent! You've doubled your steps!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            if (toast != null)
                toast.show();
        }
    }

    private void updateSystemPreferences(int i, boolean b) {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(format.format(calendar.getTime()) + Integer.toString(i), b);
            editor.apply();
        }
    }

    private boolean getSystemPreference(int i) {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (pref != null) {
            return pref.getBoolean(format.format(calendar.getTime()) + Integer.toString(i), false);
        }
        else return false;
    }
}
