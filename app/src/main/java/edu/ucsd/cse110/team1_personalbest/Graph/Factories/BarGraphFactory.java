package edu.ucsd.cse110.team1_personalbest.Graph.Factories;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivityGraph;
import edu.ucsd.cse110.team1_personalbest.Graph.Interface.GraphFactory;
import edu.ucsd.cse110.team1_personalbest.R;

public class BarGraphFactory implements GraphFactory {
    public GraphView makeGraph(int[] steps, GraphView graph) {
        String[] days = new String[]{"Su", "M", "T", "W", "Th" , "F", "S"};
        String[] lables = new String[7];
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.DAY_OF_WEEK;
        dayOfWeek++;
        for(int i = 0;i<7;i++){
            lables[i] = days[dayOfWeek%7];
            dayOfWeek++;
        }



        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, steps[0]),
                new DataPoint(2, steps[1]),
                new DataPoint(3, steps[2]),
                new DataPoint(4, steps[3]),
                new DataPoint(5, steps[4]),
                new DataPoint(6, steps[5]),
                new DataPoint(7, steps[6])
        });

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, steps[0]),
                new DataPoint(2, steps[1]),
                new DataPoint(3, steps[2]),
                new DataPoint(4, steps[3]),
                new DataPoint(5, steps[4]),
                new DataPoint(6, steps[5]),
                new DataPoint(7, steps[6])
        });
        graph.addSeries(series);
        graph.addSeries(series2);
        graph.setTitle("Weekly Steps");
        GridLabelRenderer labelRenderer = graph.getGridLabelRenderer();
        //LegendRenderer legendRenderer = graph.getLegendRenderer();
        //legendRenderer.setVisible(true);
        //viewport.setMaxX(7);
        //viewport.setMinX(0);
        graph.getViewport().setScalable(true);


        graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 4 because of the space
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(7);
        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
        labelRenderer.setHorizontalAxisTitle("Days of the Week");
        labelRenderer.setVerticalAxisTitle("Steps for the Day");
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        staticLabelsFormatter.setHorizontalLabels(lables);

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
        series.setDataWidth(0.6);
        series.setSpacing(5);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        return graph;
    }
}
