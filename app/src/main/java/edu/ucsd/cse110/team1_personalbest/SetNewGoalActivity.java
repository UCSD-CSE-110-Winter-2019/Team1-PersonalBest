package edu.ucsd.cse110.team1_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;

public class SetNewGoalActivity extends AppCompatActivity {

    private Database db;
    private String FILENAME = "steps.json";
    private Integer suggestedGoal;
    IDataObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_goal);

        /*
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String today = format.format(date);
        IDataObject result = db.readDataObject(today);
*/
        int suggestedGoal = 5000;
        TextView newSuggestedGoal = findViewById(R.id.newSuggestedGoal);
        suggestedGoal += 500;
        newSuggestedGoal.setText("5000");

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
        //db = new Database(getApplicationContext());
        //result.setDailyStepGoal(suggestedGoal);
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
