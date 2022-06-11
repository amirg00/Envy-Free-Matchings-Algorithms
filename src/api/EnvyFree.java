package api;


import java.util.ArrayList;
import java.util.Arrays;

public class EnvyFree {
    private UndirectedBipartiteGraph bGraph; /*Bipartite graph: G[X,Y]*/

    public EnvyFree(UndirectedBipartiteGraph bGraph){
        this.bGraph = bGraph;
    }

    /**
     * Method computes an isolated saturated segregation G[X_L, Y_L].
     * @return the computed bipartite graph G[X_L, Y_L].
     */
    public UndirectedBipartiteGraph computeSegregation(){
        UndirectedBipartiteGraph segregatedGraph;

        // Initializing x_0 with X\X_M vertices
        ArrayList<NodeData> X_0 = new ArrayList<>();
        for (NodeData node : bGraph.getDisjointSet_A()){
            if (!bGraph.isVertexInMatches(node) && !X_0.contains(node)){
                X_0.add(node);
            }
        }

        // Computing χ and Y where:
        // χ = (X_0, ..., X_i)
        // Υ = (Υ_1, ..., Υ_i)
        ArrayList<ArrayList<NodeData>> X = new ArrayList<>();
        ArrayList<ArrayList<NodeData>> Y = new ArrayList<>();
        X.add(new ArrayList<>(X_0));

        System.out.println("WHILE LOOP!!!!");
        int i = 1;
        while (true){
            System.out.println(i);
            ArrayList<NodeData> Y_i = compute_Y_i(i, Y, X.get(i-1));
            ArrayList<NodeData> X_i = compute_X_i(i, Y_i);
            if (X_i.isEmpty() || Y_i.isEmpty()){break;}
            Y.add(Y_i);
            X.add(X_i);
            i++;
        }
        System.out.println("END WHILE LOOP!!!!");

        // Construct the graph G[X_L,Y_L] section.
        ArrayList<NodeData> X_union = unionAll(X);
        ArrayList<NodeData> Y_union = unionAll(Y);

        ArrayList<NodeData> X_L = difference(bGraph.getDisjointSet_A(), X_union);
        ArrayList<NodeData> Y_L = difference(bGraph.getDisjointSet_B(), Y_union);

        segregatedGraph = new UndirectedBipartiteGraph(bGraph, X_L, Y_L);
        return segregatedGraph;
    }

    /**
     * Method computes the node's set X_i,
     * via the following formula: N_M(Y_i).
     * @return the nodes set X_i.
     */
    private ArrayList<NodeData> compute_X_i(int i, ArrayList<NodeData> Y_i){
        ArrayList<NodeData> X_i = new ArrayList<>();
        for (NodeData y : Y_i){
            ArrayList<EdgeData> N = bGraph.edgesOut(y.getKey());
            for (EdgeData neighbor : N){
                NodeData destNode = bGraph.getNode(neighbor.getDest());
                if (bGraph.isEdgeInMatch(neighbor) && !X_i.contains(destNode)){
                    X_i.add(destNode);
                }
            }
        }
        return X_i;
    }


    /**
     * Method computes the node's set Y_i,
     * via the following recursive formula: Y_i = N_(G\M)(X_(i-1)) \ (union(Y_j : j < i))
     * @return the nodes set Y_i.
     */
    private ArrayList<NodeData> compute_Y_i(int i, ArrayList<ArrayList<NodeData>> Y, ArrayList<NodeData> X_i_minus_1){
        // union(Y_j : j < i)
        ArrayList<NodeData> union_y = new ArrayList<>();
        for (ArrayList<NodeData> y : Y){
            union_y = union(union_y, y);
        }

        // N_(G\M)(X_(i-1))
        ArrayList<NodeData> N = new ArrayList<>();
        for (NodeData node : X_i_minus_1){
            for (EdgeData neighbor : bGraph.edgesOut(node.getKey())){
                NodeData destNode = bGraph.getNode(neighbor.getDest());
                if (!bGraph.isEdgeInMatch(neighbor) && !N.contains(destNode)){
                    N.add(destNode);
                }
            }
        }
        return difference(N, union_y);
    }

    private ArrayList<NodeData> unionAll(ArrayList<ArrayList<NodeData>> sets){
        ArrayList<NodeData> union = new ArrayList<>();
        for (ArrayList<NodeData> set : sets){
            union = union(union, set);
        }
        return union;
    }

    /**
     * Method unions two sets of nodes.
     * @param firstSet the first set of nodes.
     * @param secondSet the second set of nodes.
     * @return the union of the given sets.
     */
    private ArrayList<NodeData> union(ArrayList<NodeData> firstSet, ArrayList<NodeData> secondSet){
        ArrayList<NodeData> union = new ArrayList<>(firstSet);
        for(NodeData node : secondSet){
            if (!firstSet.contains(node)){
                union.add(node);
            }
        }
        return union;
    }

    /**
     * Method return the difference (A\B) between the two given sets of nodes.
     * @param firstSet the first set of nodes.
     * @param secondSet the second set of nodes.
     * @return the difference of the given sets (firstSet\secondSet).
     */
    private ArrayList<NodeData> difference(ArrayList<NodeData> firstSet, ArrayList<NodeData> secondSet) {
        ArrayList<NodeData> diff = new ArrayList<>(firstSet);
        for (NodeData node : secondSet){
            diff.removeIf(n->n.equals(node));
        }
        return diff;
    }


    /**
     * Method return the intersection (A∩B) between the two given sets of edges.
     * @param firstSet the first set of edges.
     * @param secondSet the second set of edges.
     * @return intersection between the given sets.
     */
    public ArrayList<EdgeData> intersection(ArrayList<EdgeData> firstSet, ArrayList<EdgeData> secondSet){
        ArrayList<EdgeData> intersection = new ArrayList<>();
        for (EdgeData e : firstSet){
            for (EdgeData e_sec : secondSet) {
                if (e.equals(e_sec) && !intersection.contains(e)) {
                    intersection.add(e);
                }
            }
        }
        return intersection;
    }
}