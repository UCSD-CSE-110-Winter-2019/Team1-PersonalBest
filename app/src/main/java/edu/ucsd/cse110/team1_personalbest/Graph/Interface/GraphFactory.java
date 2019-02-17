package edu.ucsd.cse110.team1_personalbest.Graph.Interface;
import com.jjoe64.graphview.GraphView;

public interface GraphFactory {
    GraphView makeGraph(int[] steps, GraphView graph);
}
