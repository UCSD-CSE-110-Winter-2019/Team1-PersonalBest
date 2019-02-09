package edu.ucsd.cse110.team1_personalbest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;

public class SetGoalOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal_option);

        Button btnYes = (Button) findViewById(R.id.buttonYes);
        Button btnNo = (Button) findViewById(R.id.buttonNotNow);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSetNewGoalActivity();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    public void launchSetNewGoalActivity() {
        Intent intent = new Intent(this, SetNewGoalActivity.class);
        startActivity(intent);
    }
}
