package edu.ucsd.cse110.team1_personalbest.Activities;

import android.content.ReceiverCallNotAllowedException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.nio.charset.IllegalCharsetNameException;
import java.time.MonthDay;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.R;

public class MainActivityGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_graph);
        TextView weeklySteps = findViewById(R.id.WeeklyStepsText);
        String weeklyStepsInt = Integer.toString(25000);
        weeklySteps.setText("Weekly Steps: " + weeklyStepsInt);

        GraphView graph = findViewById(R.id.weeklyBarGraph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 10000),
                new DataPoint(2, 5000),
                new DataPoint(3, 9000),
                new DataPoint(4, 8000),
                new DataPoint(5, 3000),
                new DataPoint(6, 4000),
                new DataPoint(7, 5000),
        });

        graph.addSeries(series);
        graph.setTitle("Weekly Steps");
        Viewport viewport = graph.getViewport();
        GridLabelRenderer labelRenderer = graph.getGridLabelRenderer();
        //LegendRenderer legendRenderer = graph.getLegendRenderer();
        //legendRenderer.setVisible(true);
        //viewport.setMaxX(7);
        //viewport.setMinX(0);
        //viewport.setScalable(true);
        viewport.setScrollable(true);
        labelRenderer.setHorizontalAxisTitle("Days of the Week");
        labelRenderer.setVerticalAxisTitle("Steps for the Day");
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        staticLabelsFormatter.setHorizontalLabels(new String[]{"Su", "M", "T", "W", "Th" , "F", "S"});

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {

                if (data.getY() < 5000){
                    return Color.RED;
                }
                return Color.GREEN;
            }
        });
        series.setDataWidth(0.8);
        series.setSpacing(10);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

        Button backButton = findViewById(R.id.returnToMain);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });


    }
}




