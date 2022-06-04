package tests;
import GraphGui.FrameGraph;
import GraphGui.PanelBipartiteGraph;
import api.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class test_envy_free {
    UndirectedBipartiteGraphAlgorithms bg_algo;
    UndirectedBipartiteGraphAlgorithms bg_algo_2;
    // Set graphs for tests
    public test_envy_free(){
        UndirectedBipartiteGraph bg = new UndirectedBipartiteGraph();
        bg.clear();
        bg.addNode(0, "A"); bg.addNode(1, "A"); bg.addNode(2, "A"); bg.addNode(3, "B"); bg.addNode(4, "B");
        bg.connect(0, 3); bg.connect(1, 3); bg.connect(2, 3); bg.connect(2, 4);
        bg_algo = new UndirectedBipartiteGraphAlgorithms();
        bg_algo.init(bg);

        UndirectedBipartiteGraph bg_2 = new UndirectedBipartiteGraph();
        bg_2.clear();
        bg_2.addNode(0, "A", new GeoLocation(32.1, 35.2, 0)); bg_2.addNode(1, "A", new GeoLocation(32.15, 35.2, 0));
        bg_2.addNode(2, "A", new GeoLocation(32.2, 35.2, 0)); bg_2.addNode(3, "B", new GeoLocation(32.2, 35.15, 0));
        bg_2.addNode(4, "B", new GeoLocation(32.15, 35.15, 0)); bg_2.addNode(5, "B", new GeoLocation(32.1, 35.15, 0));
        bg_2.connect(0, 5, 1); bg_2.connect(1, 4, 8); bg_2.connect(2, 3, 1);
        bg_2.connect(2, 5, 4); bg_2.connect(0, 4, 6); bg_2.connect(1, 3, 6);
        bg_algo_2 = new UndirectedBipartiteGraphAlgorithms();
        bg_algo_2.init(bg_2);
    }

    @Test
    void max_cardinality_test() throws InterruptedException {
        FrameGraph a = new FrameGraph(bg_algo.getGraph());
        assertEquals(bg_algo.envyFreeMaxCardinality(a.getPanel(), a), 1);
        Thread.sleep(110000000);
    }

    @Test
    void max_weight_matching() throws InterruptedException {
        FrameGraph a = new FrameGraph(bg_algo_2.getGraph());
        bg_algo_2.envyFreeMaxWeight(a.getPanel(), a);
        Thread.sleep(110000000);
    }
}
