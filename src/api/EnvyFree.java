package api;


import java.util.ArrayList;

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
        // Initializing x_0 with X\X_M vertices
        ArrayList<NodeData> X_0 = new ArrayList<>();
        for (NodeData node : bGraph.getDisjointSet_A()){
            if (!bGraph.isNodeInMatches(node)){
                X_0.add(node);
            }
        }

        ArrayList<ArrayList<NodeData>> X = new ArrayList<>();
        ArrayList<ArrayList<NodeData>> Y = new ArrayList<>();

        X.add(new ArrayList<>(X_0));


        return null;
    }

    /**
     * Compute χ = (X_0, ..., X_k).
     * @return χ nodes list.
     */
    private ArrayList<NodeData> computeX(){
        return null;
    }

    /**
     * Compute Υ = (Υ_1, ..., Υ_k).
     * @return Υ nodes list.
     */
    private ArrayList<NodeData> computeY(){
        return null;
    }

    /**
     * Method computes the node's set X_i,
     * via the following formula: N_M(Y_i).
     * @return the nodes set X_i.
     */
    private ArrayList<NodeData> compute_X_i(int i, ArrayList<NodeData> Y_i){
        ArrayList<NodeData> X_i = new ArrayList<>();
        for (NodeData y : Y_i){
            ArrayList<NodeData> N = bGraph.edgesOut(y.getKey());
            for (NodeData neighbor : N){
                if (bGraph.isNodeInMatches(neighbor)){
                    X_i.add(neighbor);
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
            for (NodeData neighbor : bGraph.edgesOut(node.getKey())){
                if (!bGraph.isNodeInMatches(neighbor)){
                    N.add(neighbor);
                }
            }
        }

        return difference(N, union_y);
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
     * Method return the difference (A\B) between the two given sets.
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
}