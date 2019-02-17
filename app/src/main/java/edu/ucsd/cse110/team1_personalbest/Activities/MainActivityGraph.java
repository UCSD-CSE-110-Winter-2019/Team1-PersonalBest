package edu.ucsd.cse110.team1_personalbest.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.ucsd.cse110.team1_personalbest.Graph.Factories.BarGraphFactory;
import edu.ucsd.cse110.team1_personalbest.R;

public class MainActivityGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_graph);
        TextView weeklySteps = findViewById(R.id.WeeklyStepsText);
        String weeklyStepsInt = Integer.toString(25000);
        weeklySteps.setText("Weekly Steps: " + weeklyStepsInt);
        BarGraphFactory factory = new BarGraphFactory();
        int[] intArray = {1000,2000,5000,7000,6000,5000,4000};
        //DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        GraphView graph = findViewById(R.id.weeklyBarGraph);
        factory.makeGraph(intArray,graph);
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext(),dateFormat));
        Button backButton = findViewById(R.id.returnToMain);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });


    }
}




