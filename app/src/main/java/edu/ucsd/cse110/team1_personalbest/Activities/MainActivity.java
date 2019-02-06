package edu.ucsd.cse110.team1_personalbest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import edu.ucsd.cse110.team1_personalbest.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartWalk = findViewById(R.id.buttonStartWalk);
    }
}
