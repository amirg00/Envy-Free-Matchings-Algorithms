package GraphGui;

import api.UndirectedBipartiteGraph;
import api.UndirectedBipartiteGraphAlgorithms;

import javax.swing.*;
import java.io.IOException;

enum states{
    HUNGARIAN(0),
    ENVY_FREE_MAX_CARDINALITY(1),
    ENVY_FREE_MAX_WEIGHT(2);

    private final int value;
    states(int value) {this.value = value;}
}

public class Engine extends Thread{
    private UndirectedBipartiteGraph graph;
    private FrameGraph frame;
    private states currAlgo;

    public Engine(UndirectedBipartiteGraph graph, FrameGraph frame, states algo){
        this.graph = graph;
        this.frame = frame;
        this.currAlgo = algo;
    }

    public void run() {
        try {
            /* Random Graph*/
            UndirectedBipartiteGraphAlgorithms bf_algo = new UndirectedBipartiteGraphAlgorithms();
            bf_algo.init(graph);
            switch (currAlgo) {
                case HUNGARIAN -> {
                    frame.delay(1000); // Wait one second before starting algorithm.
                    bf_algo.hungarianMethod(frame.getPanel(), frame);
                }
                case ENVY_FREE_MAX_CARDINALITY -> {
                    frame.delay(1000);
                    bf_algo.envyFreeMaxCardinality(frame.getPanel(), frame);
                }
                case ENVY_FREE_MAX_WEIGHT -> {
                    frame.delay(1000);
                    bf_algo.envyFreeMaxWeight(frame.getPanel(), frame);
                }
            }
        } catch (Exception e) { // User aborted quickSort.

        } finally {// Make sure running is false and button label is

        }
    }

}