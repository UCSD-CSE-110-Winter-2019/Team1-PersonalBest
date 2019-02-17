package edu.ucsd.cse110.team1_personalbest.Graph.Interface;
import com.jjoe64.graphview.GraphView;

public interface GraphFactory {
    GraphView makeGraph(int goal, int[]intentionalSteps, int[] steps, GraphView graph);

}
