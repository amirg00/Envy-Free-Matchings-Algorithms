package api;


import java.util.ArrayList;

public class EnvyFree {
    private UndirectedBipartiteGraph bGraph;

    public EnvyFree(UndirectedBipartiteGraph bGraph){
        this.bGraph = bGraph;
    }

    /**
     * Method computes an isolated saturated segregation G[X_L, Y_L].
     * @return the computed bipartite graph G[X_L, Y_L].
     */
    public UndirectedBipartiteGraph computeSegregation(){

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
    private ArrayList<NodeData> compute_X_i(int i){
        return null;
    }


    /**
     * Method computes the node's set Y_i,
     * via the following recursive formula: Y_i = N_(G\M)(X_(i-1)) \ (union(Y_j : j < i))
     * @return the nodes set Y_i.
     */
    private ArrayList<NodeData> compute_Y_i(int i){
        return null;
    }

    /**
     * Method unions two sets of nodes.
     * @param firstSet the first set of nodes.
     * @param secondSet the second set of nodes.
     * @return the union of the given sets.
     */
    private ArrayList<NodeData> union(ArrayList<NodeData> firstSet, ArrayList<NodeData> secondSet){
        return null;
    }

    /**
     * Method return the difference (A\B) between the two given sets.
     * @param firstSet the first set of nodes.
     * @param secondSet the second set of nodes.
     * @return the difference of the given sets (firstSet\secondSet).
     */
    private ArrayList<NodeData> difference(ArrayList<NodeData> firstSet, ArrayList<NodeData> secondSet) {
        return null;
    }

}
