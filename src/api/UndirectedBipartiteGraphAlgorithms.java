package api;

import GraphGui.FrameGraph;
import GraphGui.PanelBipartiteGraph;

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
     * The documentations for this are located in DirectedWeightedGraphImpl class.
     * @return a deep of the initialized graph.
     */
    public UndirectedBipartiteGraph copy() {
        return null;
    }


    public void hungarianMethod(PanelBipartiteGraph panel, FrameGraph frame) throws InterruptedException {
        graph.setMatches(new ArrayList<>()); // M = ϕ
        //panel.render();
        while (true){
            DirectedWeightedGraph g = constructDirectedGraph();
            DirectedWeightedGraphAlgorithms g_algo = new DirectedWeightedGraphAlgorithms();
            g_algo.init(g);
            boolean status = g_algo.bfs(graph, panel, frame); // Performs bfs from an unsaturated vertex in A.
            if (!status) {System.out.println(Arrays.toString(graph.getMatches().toArray()));break;}
        }
    }

    public DirectedWeightedGraph constructDirectedGraph() {
        DirectedWeightedGraph constructedGraph = new DirectedWeightedGraph();

        for (NodeData v : graph.getVertices()) {
            constructedGraph.addNode(v);
        }

        for (EdgeData e : graph.getEdges()) {
            NodeData src = graph.getNode(e.getSrc());
            NodeData dest = graph.getNode(e.getDest());
            ArrayList<NodeData> A = graph.getDisjointSet_A();
            ArrayList<NodeData> B = graph.getDisjointSet_B();

            if (graph.isVertexInGroup(src, B) && graph.isVertexInMatches(src)
                    && graph.isVertexInGroup(dest, A) && graph.isVertexInMatches(dest)
                    && graph.isDirectEdgeInMatches(e.getSrc(), e.getDest())) {
                constructedGraph.connect(e.getSrc(), e.getDest(), 0.0);
            } else if (graph.isVertexInGroup(dest, B) && graph.isVertexInMatches(dest)
                    && graph.isVertexInGroup(src, A) && graph.isVertexInMatches(src)
                    && graph.isDirectEdgeInMatches(e.getSrc(), e.getDest())) {
                constructedGraph.connect(e.getDest(), e.getSrc(), 0.0);
            } else {
                if (graph.isVertexInGroup(src, A)) {
                    constructedGraph.connect(e.getSrc(), e.getDest(), 0.0);
                } else {
                    constructedGraph.connect(e.getDest(), e.getSrc(), 0.0);
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
