package edu.ucsd.cse110.team1_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;

public class SetNewGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_goal);

        Button btnAcceptSuggestedGoal = (Button) findViewById(R.id.buttonAcceptSuggestedGoal);
        Button btnSetCustomGoal = (Button) findViewById(R.id.buttonSetCustomGoal);
        Button btnCancelSetGoal = (Button) findViewById(R.id.buttonCancelSetGoal);

        btnAcceptSuggestedGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSuggestedGoal(v);
            }
        });

        btnSetCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomGoalActivity.class);
                startActivity(intent);            }
        });

        btnCancelSetGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    public void saveSuggestedGoal(View view){

        String newGoal = "";
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
