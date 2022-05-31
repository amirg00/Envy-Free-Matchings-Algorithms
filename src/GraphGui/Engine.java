package GraphGui;

import api.UndirectedBipartiteGraph;
import api.UndirectedBipartiteGraphAlgorithms;

import javax.swing.*;
import java.io.IOException;

public class Engine extends Thread{
    private UndirectedBipartiteGraph graph;
    private FrameGraph frame;
    public Engine(UndirectedBipartiteGraph graph, FrameGraph frame){
        this.graph = graph;
        this.frame = frame;
    }

    public void run() {
        try {
           /* Random Graph*/
            UndirectedBipartiteGraphAlgorithms bf_algo = new UndirectedBipartiteGraphAlgorithms();
            bf_algo.init(graph);
            frame.delay(1000); // Wait one second before starting algorithm.
            bf_algo.hungarianMethod(frame.getPanel(), frame);
        } catch (Exception e) { // User aborted quickSort.

        } finally {// Make sure running is false and button label is

        }
    }

}