package api;

import java.util.*;

/**
 * Class brief explanation:
 * This class operates DFS for a given graph,
 * and checks whether the graph is connected.
 */
public class BFS {

    private DirectedWeightedGraph g;
    private UndirectedBipartiteGraph bg;
    private Queue<Integer> q;
    private HashMap<Integer, Integer> parent;
    private ArrayList<NodeData> augmentPath;


    /**
     * Tags for the nodes to check if visited or not visited.
     */
    public enum Tags{
        VISITED(1),
        UNVISITED(0);
        private final int value;
        Tags(int value) {this.value = value;}
    }

    /**
     * Constructor performs bfs traversal from an unsaturated vertex in A,
     * to an unsaturated vertex in B, then it finds the augmenting path.
     * @param g a directed graph.
     * @param bg an undirected bipartite graph.
     */
    public BFS(DirectedWeightedGraph g, UndirectedBipartiteGraph bg){
        this.g = g;
        this.bg = bg;
        q = new ArrayDeque<>();
        setTags(Tags.UNVISITED.value);
        parent = new HashMap<>();
        augmentPath = new ArrayList<>();

        // Traverse all unsaturated vertices from set A
        for (NodeData v : bg.getDisjointSet_A()){
            clear();
            if (!bg.isVertexInMatches(v)){
                bfs(v);
                Iterator<NodeData> nodes = g.nodeIter();
                while (nodes.hasNext()) {
                    NodeData curr = nodes.next();
                    // check if there is an augmenting path
                    if (checkVisitedInMatchComplete(curr, bg.getDisjointSet_B())) {
                        augmentPath = getAugmentingPath(v.getKey(), curr.getKey());
                        return;
                    }
                }
            }
        }
    }

    /**
     * Method clears the properties.
     */
    public void clear(){
        q = new ArrayDeque<>();
        setTags(Tags.UNVISITED.value); // set all vertices to be unvisited for next BFS traversal.
        parent = new HashMap<>();
        augmentPath = new ArrayList<>();
    }

    /**
     * Method checks if the given node is in the complement of the given group.
     * @param v a given node
     * @param group a given set.
     * @return true iff v is in the complement of group in terms of in match or not, o.w., returns false.
     */
    public boolean checkVisitedInMatchComplete(NodeData v, ArrayList<NodeData> group){
        return  v.getTag() == Tags.VISITED.value
                && !bg.isVertexInMatches(v)
                &&  bg.isVertexInGroup(v, group);
    }

    /**
     * Performs BFS traversal which starts from the vertex v.
     * @param root a given vertex index.
     *
    // 1     BFS(root):
    // 2      label root as explored
    // 3      Q.enqueue(root)
    // 4          visited[u] ← true     ▹White vertex u has just been discovered.
    // 5      while Q is not empty do:
    // 6          v ← Q.dequeue()
    // 7          for each v ∈ Adj[u]  ▹Explore edge(u, v).
    // 8              if v is not labeled as explored then:
    // 9                  visited[u] ← true
    // 10                  Q.enqueue(w)
     */
    public void bfs(NodeData root){
        parent = new HashMap<>(); // initialize parents map
        root.setTag(Tags.VISITED.value);
        q.add(root.getKey());
        while(!q.isEmpty()) {
            Integer curr = q.remove();
            Iterator<EdgeData> neighbours = g.edgeIter(curr);
            if (neighbours == null) {return;}

            // Traverse neighbors of current node
            while (neighbours.hasNext()) {
                EdgeData currEdge = neighbours.next();
                int curr_id = currEdge.getDest();
                NodeData currN = g.getNode(curr_id);
                if (currN.getTag() == Tags.UNVISITED.value){
                    currN.setTag(Tags.VISITED.value);
                    parent.put(curr_id, curr);
                    q.add(curr_id);
                }
            }
        }

    }
    /**
     * The method sets the augmenting path, using the parent property of each nodeCompare.
     * The method sort of going back to the linked parent of the node, until it gets
     * to the ancestor (root), and for each nodeCompare the method adds at the BEGINNING the NodeData
     * object which belongs to the current nodeCompare.
     *
     * @implNote At the end, we will get the objects list with the following order: {src, ..., dest}.
     */
    public ArrayList<NodeData> getAugmentingPath(int src, int dest){
        ArrayList<NodeData> path = new ArrayList<>();
        int curr_parent = dest;

        while(curr_parent!=src){
            //System.out.println(curr_parent);
            path.add(0,g.getNode(curr_parent));
            curr_parent = parent.get(curr_parent);
        }
        //System.out.println(src);
        path.add(0,g.getNode(src));
        return path;
    }

    /**
     * This method sets the tags.
     * @param value a given value
     */
    public void setTags(int value){
        Iterator<NodeData> nodes = g.nodeIter();
        while(nodes.hasNext()){nodes.next().setTag(value);}
    }

    public ArrayList<NodeData> getAugmentPath() {return augmentPath;}
}
