package edu.ucsd.cse110.team1_personalbest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.ucsd.cse110.team1_personalbest.Fitness.Factories.FitnessServiceFactory;
import edu.ucsd.cse110.team1_personalbest.Fitness.Interfaces.FitnessService;
import edu.ucsd.cse110.team1_personalbest.Fitness.Objects.SessionData;
import edu.ucsd.cse110.team1_personalbest.R;

public class CountStepActivity extends AppCompatActivity {

    public static final String TAG = "[CountStepActivity]";
    private SessionData sessionData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_step);

        Button btnEndWalk = (Button) findViewById(R.id.buttonEndWalk);
        final Button historyBtn = findViewById(R.id.buttonHistory2);

        final SessionData sessionData = new SessionData();

        final FitnessService service = FitnessServiceFactory.create(MainActivity.GOOGLE_FITNESS, this);
        service.setup();

        // TODO fix an error here that causes crash and no data appearing
        //service.startListening();
        //service.updateSessionData(sessionData);

        btnEndWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //service.stopListening();
                finish();
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionData.isShouldUpdate())
                    Toast.makeText(getBaseContext(), sessionData.toString(), Toast.LENGTH_LONG).show();
                    service.updateSessionData(sessionData);
            }
        });
    }
}
