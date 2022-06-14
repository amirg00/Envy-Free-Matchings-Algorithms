package api;

import GraphGui.FrameGraph;
import GraphGui.PanelBipartiteGraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UndirectedBipartiteGraphAlgorithms {
    private UndirectedBipartiteGraph graph;

    public void init(UndirectedBipartiteGraph g) { this.graph = g; }

    public UndirectedBipartiteGraph getGraph() {
        return this.graph;
    }

    /**
     * The method returns a deep copy of the initialized graph.
     * @implNote the method doesn't copy the matches!
     * @return a deep of the initialized graph.
     */
    public UndirectedBipartiteGraph copy() {
        return new UndirectedBipartiteGraph(graph);
    }

    /**
     * Method finds an envy free matching with maximum cardinality.
     */
    public int envyFreeMaxCardinality(PanelBipartiteGraph panel, FrameGraph frame){
        EnvyFree envy_free_algo = new EnvyFree(graph);
        hungarianMethod(panel, frame);
        panel.drawAllEdges(new Color(204, 204, 204), 2);
        panel.repaint();
        UndirectedBipartiteGraph isolatedGraph = envy_free_algo.computeSegregation();
        ArrayList<EdgeData> matches = graph.getMatches();
        ArrayList<EdgeData> segregationEdges = isolatedGraph.getEdges();
        ArrayList<EdgeData> intersection = envy_free_algo.intersection(matches, segregationEdges);
        frame.delay(1000);
        for (EdgeData e : intersection) {
            System.out.println(e); panel.drawEdge(e, new Color(226, 32, 33), 3); panel.repaint();}
        return intersection.size();
    }

    /**
     * Method finds an envy free matching with maximum cost,
     * meaning that the sum of the edges is maximal.
     */
    public void envyFreeMaxWeight(PanelBipartiteGraph panel, FrameGraph frame){
        EnvyFree envy_free_algo = new EnvyFree(graph);
        hungarianMethod(panel, frame);
        panel.drawAllEdges(new Color(204, 204, 204), 2);
        frame.delay(1000);
        UndirectedBipartiteGraph isolatedGraph = envy_free_algo.computeSegregation();
        panel.drawAllEdges(new Color(204, 204, 204), 2);
        frame.delay(1000);
        weightedHungarianMethod(isolatedGraph, panel, frame);
    }

    public void weightedHungarianMethod(UndirectedBipartiteGraph graph, PanelBipartiteGraph panel, FrameGraph frame) {
        graph.setMatches(new ArrayList<>()); // M = ϕ
        UndirectedBipartiteGraph graphCopy = new UndirectedBipartiteGraph(graph);

        // Find max edge weight
        double maxWeight = 0;
        for (EdgeData e : graphCopy.getEdges()){
            maxWeight = Math.max(maxWeight, e.getWeight());
        }

        // Update each edge's weight with maximum weight minus the edge's weight
        for (EdgeData e : graphCopy.getEdges()){
            e.setWeight(maxWeight - e.getWeight());
        }
        while (true){
            DirectedWeightedGraph g = constructDirectedGraph(graphCopy);
            DirectedWeightedGraphAlgorithms g_algo = new DirectedWeightedGraphAlgorithms();
            g_algo.init(g);
            boolean status = g_algo.dijkstra(graphCopy, panel, frame); // Performs bfs from an unsaturated vertex in A.
            if (!status) {System.out.println(Arrays.toString(graphCopy.getMatches().toArray()));break;}
        }
    }

    /*
     * Method performs hungarian method to find augmenting path,
     * until they aren't augmenting paths to be found.
     */
    public void hungarianMethod(PanelBipartiteGraph panel, FrameGraph frame) {
        graph.setMatches(new ArrayList<>()); // M = ϕ
        while (true){
            DirectedWeightedGraph g = constructDirectedGraph(graph);
            DirectedWeightedGraphAlgorithms g_algo = new DirectedWeightedGraphAlgorithms();
            g_algo.init(g);
            boolean status = g_algo.bfs(graph, panel, frame); // Performs bfs from an unsaturated vertex in A.
            if (!status) {System.out.println(Arrays.toString(graph.getMatches().toArray()));break;}
        }
    }

    public DirectedWeightedGraph constructDirectedGraph(UndirectedBipartiteGraph graph) {
        DirectedWeightedGraph constructedGraph = new DirectedWeightedGraph();

        // Add all the nodes to the directed graph
        for (NodeData v : graph.getVertices()) {
            constructedGraph.addNode(v);
        }

        // Add all each edge to the directed graph
        for (EdgeData e : graph.getEdges()) {
            NodeData src = graph.getNode(e.getSrc());
            NodeData dest = graph.getNode(e.getDest());
            ArrayList<NodeData> A = graph.getDisjointSet_A();
            ArrayList<NodeData> B = graph.getDisjointSet_B();

            // Check in which set (A, A', B, B') the edge's nodes belong
            if (graph.isVertexInGroup(src, B) && graph.isVertexInMatches(src)
                    && graph.isVertexInGroup(dest, A) && graph.isVertexInMatches(dest)
                    && graph.isDirectEdgeInMatches(e.getSrc(), e.getDest())) {
                constructedGraph.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
            else if (graph.isVertexInGroup(dest, B) && graph.isVertexInMatches(dest)
                    && graph.isVertexInGroup(src, A) && graph.isVertexInMatches(src)
                    && graph.isDirectEdgeInMatches(e.getSrc(), e.getDest())) {
                constructedGraph.connect(e.getDest(), e.getSrc(), e.getWeight());
            }
            else {
                if (graph.isVertexInGroup(src, A)) {
                    constructedGraph.connect(e.getSrc(), e.getDest(), e.getWeight());
                } else {
                    constructedGraph.connect(e.getDest(), e.getSrc(), e.getWeight());
                }
            }
        }
        System.out.println(Arrays.toString(graph.getEdges().toArray()));
        System.out.println(Arrays.toString(graph.getDisjointSet_A().toArray()));
        System.out.println(Arrays.toString(graph.getDisjointSet_B().toArray()));
        System.out.println(Arrays.toString(graph.getMatches().toArray()));
        System.out.println(constructedGraph.getEdges().toString());
        return constructedGraph;
    }

}
