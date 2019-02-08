package edu.ucsd.cse110.team1_personalbest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CountStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_step);

        Button btnEndWalk = (Button) findViewById(R.id.buttonEndWalk);

        btnEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /* Show an encouragement when current steps is half of the goal steps */
    public void showEncouragementForDouble(int CurrentStep){
        Context context = getApplicationContext();
        CharSequence text = "You've nearly doubled your steps. Keep up the good work!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /* Show an encouragement when click the end button and current steps is not greater than goal steps */
    public void showEncouragementAtEnd(int CurrentStep){
        Context context = getApplicationContext();
        CharSequence text = "Good job! Keep up the good work!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
