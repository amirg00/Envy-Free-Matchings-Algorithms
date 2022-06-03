package api;

import GraphGui.FrameGraph;
import GraphGui.PanelBipartiteGraph;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph graph;

    public void init(DirectedWeightedGraph g) { this.graph = g; }

    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    /**
     * The method returns a deep copy of the initialized graph.
     * The documentations for this are located in DirectedWeightedGraphImpl class.
     * @return a deep of the initialized graph.
     */
    public DirectedWeightedGraph copy() {return graph.deepCopy(graph);}

    public boolean bfs(UndirectedBipartiteGraph g, PanelBipartiteGraph panel, FrameGraph frame) {
        BFS bfs = new BFS(graph, g);
        ArrayList<NodeData> path = bfs.getAugmentPath();

        drawPath(path, panel , frame);

        System.out.println("-------------- LOOK --------------");

        if (!path.isEmpty()) {
            /*Before any drawing, draw the path with purple and red color for unmatched and matched edges accordingly */
            for (int i = 0; i < path.size() - 1; i++) {

                EdgeData curr = graph.getEdge(path.get(i).getKey(), path.get(i + 1).getKey());

                if (i % 2 == 0) {
                    g.getMatches().add(curr);
                    /* Here: draw edge in red*/
                    //panel.getG2d().setStroke(new BasicStroke(2));

                    panel.drawEdge(curr, new Color(226, 32, 33), 3);
                    //panel.repaint();
                } else {
                    g.removeDirectEdgeInMatches(curr.getSrc(), curr.getDest());
                    /* draw edge back to silver-gray*/
                    //panel.getG2d().setStroke(new BasicStroke(1));
                    panel.drawEdge(curr, new Color(204, 204, 204), 2);
                    //panel.repaint();
                }
                frame.delay(500);
            }
            return true;
        }
        return false;
    }

    public boolean dijkstra(UndirectedBipartiteGraph bg, PanelBipartiteGraph panel, FrameGraph frame){
        DirectedWeightedGraph copyGraph = copy();
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(copyGraph, bg);
        return true;
    }

    public void drawPath(ArrayList<NodeData> path, PanelBipartiteGraph panel, FrameGraph frame) {
        System.out.println(Arrays.toString(path.toArray()));

        if (path.isEmpty()){return;}

        for (int i = 0; i < path.size()-1; i++) {
            if (i % 2 == 0) {
                panel.drawEdge(graph.getEdge(path.get(i).getKey(), path.get(i + 1).getKey()), new Color(99, 73, 160), 3);
            }
            else {
                panel.drawEdge(graph.getEdge(path.get(i).getKey(), path.get(i + 1).getKey()), Color.green, 3);
            }
            frame.delay(500);
        }

    }

}



