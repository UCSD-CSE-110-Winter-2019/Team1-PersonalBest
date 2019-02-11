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

public class CustomGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_goal);

        Button btnAcceptCustomGoal = (Button) findViewById(R.id.buttonAcceptCustomGoal);
        Button btnCancelCustomGoal = (Button) findViewById(R.id.buttonCancelCustomGoal);
        btnCancelCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        btnAcceptCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomGoal(v);
                setNewGoal(v);
            }
        });
    }

    public void saveCustomGoal(View view){

        EditText newGoal = (EditText) findViewById(R.id.customGoal);

        SharedPreferences sharedPreferences = getSharedPreferences("new_goal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("newGoal", newGoal.getText().toString());

        editor.apply();
        //Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void setNewGoal(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("new_goal", MODE_PRIVATE);
        String new_goal = sharedPreferences.getString("newGoal","");
        TextView displayNewGoal = (TextView) findViewById(R.id.step_goal);
        displayNewGoal.setText(new_goal);
    }
}
